package com.hoho.android.usbserial.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.RemoteException;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.listener.OnErrorListener;
import com.hoho.android.usbserial.listener.OnReceiveListener;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;


/**
 * 串口通讯
 */
public class SerialPortUtil {
    private final String TAG = "SerialPortUtil";
    private Context mContext;
    private int timeOut;
    private OnErrorListener mOnErrorListener;
    private static UsbSerialPort sPort = null;
    private SerialInputOutputManager mSerialIoManager;
    private byte[] byteAll;

    private int mBps;//波特率
    private int mPar;//校验
    private int mDbs;//数据位
    private ByteBuffer mReadBuffer = null;
    private boolean startReadPort = true;//开始接受串口消息
    private int[] o_Cmd = {0};
    OnReceiveListener mOnReceiverListener;

    public static SerialPortUtil getInstance() {
        return SingletonManager.instance;
    }

    public static class SingletonManager {
        private static final SerialPortUtil instance = new SerialPortUtil();
    }

    /**
     * 开始与控件通讯
     */
    public void start(Context context, int timeout, OnErrorListener onErrorListener,
                      OnReceiveListener onReceiveListener) throws RemoteException {
        mContext = context;
        timeOut = timeout;
        mOnErrorListener = onErrorListener;
        this.mOnReceiverListener = onReceiveListener;
        tryGetUsbPermission();
    }

    private void openSerial() {
        boolean isInit = init(SerialPortCode.BPS_9600, SerialPortCode.PAR_NOPAR, SerialPortCode.DBS_8);
        boolean isOpen = open();

        if (isInit && isOpen) {
            startReadPort = true;
            new ReceiverThread().start();
            mOnErrorListener.onSuccess();
            Log.e(TAG, "open onSuccess");
        } else {
            int errorCode = 0;
            errorCode = SerialPortCode.ERROR_OPEN;//打开串口错
            Log.e(TAG, "open onError");
            mOnErrorListener.onError(errorCode);
        }
    }

    /**
     * 取消通讯
     */
    public boolean cancel() {
        Log.e(TAG, "start");
        boolean isClearInputBuffer = clearInputBuffer();
        boolean isClose = close();
        return isClearInputBuffer && isClose;
    }

    /**
     * 接受数据
     */
    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            while (startReadPort) {
                try {
                    Thread.sleep(100);
                    read(new byte[1024], timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    int errorCode = SerialPortCode.ERROR_OTHER;//其他
                    mOnErrorListener.onError(errorCode);
                }
            }
        }
    }

    private static UsbManager mUsbManager;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private void tryGetUsbPermission() {
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        mContext.registerReceiver(mUsbPermissionActionReceiver, filter);
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);

        int index = 0;
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            if (mUsbManager.hasPermission(usbDevice)) {
//                afterGetUsbPermission(usbDevice);
                index++;
                Log.e("zhangfei2", "index = " + index + " usbinfo " + " pid " + usbDevice.getProductId() + " vid " + usbDevice.getVendorId());
            } else {
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
            if (index == mUsbManager.getDeviceList().size()) {
                openSerial();
            }
        }
    }

    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    Log.e("zhangfei1", "size = " + mUsbManager.getDeviceList().size());
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (null != usbDevice) {
//                            afterGetUsbPermission(usbDevice);
                            openSerial();
                        }
                    } else {
                        Log.e("zhangfei", String.valueOf("Permission denied for device" + usbDevice));
                    }
                }
            }
        }
    };

    /**************************************************************************************************/
    /**
     * 打开串口
     */
    private boolean open() {
        final UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            return false;
        }
        UsbSerialDriver driver = null;
        for (int i = 0; i < availableDrivers.size(); i++) {
            driver = availableDrivers.get(i);
        }

        assert usbManager != null;
        assert driver != null;
        UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
        if (connection == null) {
            Log.e(TAG, "Opening device failed");
            return false;
        }

        try {
            sPort = driver.getPorts().get(0);
            sPort.open(connection);
            sPort.setParameters(mBps, mDbs, UsbSerialPort.STOPBITS_1, mPar);
        } catch (IOException e) {
            Log.e(TAG, "Error open device: " + e.getMessage(), e);
            try {
                sPort.close();
            } catch (IOException e2) {
                return false;
            }
            sPort = null;
            return false;
        }
        return true;
    }

    /**
     * 关闭串口
     */
    private boolean close() {
        startReadPort = false;
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
        mContext.unregisterReceiver(mUsbPermissionActionReceiver);
        if (sPort != null) {
            try {
                sPort.close();
            } catch (IOException e) {
                e.printStackTrace();
                int errorCode = SerialPortCode.ERROR_OTHER;//其他
                mOnErrorListener.onError(errorCode);
                return false;
            }
            sPort = null;
        }
        return true;
    }

    /**
     * 初始化串口
     *
     * @param bps 波特率
     * @param par 校验
     * @param dbs 数据位
     */
    private boolean init(int bps, int par, int dbs) {
        mBps = getBps(bps);
        mPar = getmPar(par);
        mDbs = dbs;
        return !(0 == mBps && 0 == mPar && 0 == mDbs);
    }

    /**
     * 从串口读数据
     */
    private int read(byte[] buffer, int timeout) throws RemoteException {
        if (sPort == null) {
            return 0;
        }
        int len = 0;
        mReadBuffer = ByteBuffer.wrap(buffer);
        try {
            len = sPort.read(mReadBuffer.array(), timeout);
            if (len > 0) {
                Log.e(TAG, "read data len=" + len);
                byte[] data = new byte[len];
                mReadBuffer.get(data, 0, len);
                Log.e(TAG, "read data =" + new String(data, "GBK"));

                if (null == byteAll) {
                    byteAll = data;
                } else {
                    byteAll = byteMerger(byteAll, data);
                    Log.e(TAG, "read GBK data = " + new String(byteAll, "GBK"));
                }

//                String hex = bytesToHexString(byteAll);
//
//                Log.e(TAG, "read bytesToHexString = " + hex);
//                if (byteAll.length >= 8) {
                // 超过指令长度就会被回调到这
                String msgStr = new String(byteAll);
                if (msgStr.length() >= 8) {
                    if (msgStr.contains("101099")) {
                        //有这个指令的字符串
                        //先找到这个指令的开始index
                        int startIndex = msgStr.indexOf("101099");
//                        byte[] data2 = new byte[8];
//                        System.arraycopy(byteAll, startIndex, data2, 0, 7);
//                        byteAll = data2;
                        if (8 <= byteAll.length - startIndex) {
                            byte[] tempByte = Arrays.copyOfRange(byteAll, startIndex, startIndex + 8);
                            String msgStr1 = new String(tempByte);
                            Log.d("data", "onMessage=" + msgStr1);
                            this.mOnReceiverListener.onMessage(tempByte);
                            byteAll = Arrays.copyOfRange(byteAll, startIndex + 8, byteAll.length);
                            String msgStr2 = new String(byteAll);
                            Log.d("data", ".last byteAll=" + msgStr2);
                        }
                    } else {
                        byteAll = null;
                    }


//                    }else {
//                        Log.d("sj", "还没到8位："+ msgStr);
//                    }

                    //每次当缓存超过8个字节做一次命令回调
//                    byteAll = Arrays.copyOfRange(byteAll, 8, byteAll.length);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            int errorCode = SerialPortCode.ERROR_TIMEOUT;//超时
            mOnErrorListener.onError(errorCode);
        }
        return len;
    }

    /**
     * 往串口发数据
     */
    public int write(byte[] data, int timeout) throws RemoteException {
        if (sPort == null) {
            return -1;
        }
        int len = -1;
        try {
            len = sPort.write(data, timeout);
        } catch (IOException e) {
            e.printStackTrace();
            int errorCode = SerialPortCode.ERROR_WRITE_DATA;//串口写数据错
            mOnErrorListener.onError(errorCode);
        }
        if (len == -1) {
            int errorCode = SerialPortCode.ERROR_TIMEOUT;//超时
            mOnErrorListener.onError(errorCode);
        }
        return len;
    }

    /**
     * 清空接收缓冲区
     */
    private boolean clearInputBuffer() {
        if (null != mReadBuffer) {
            mReadBuffer.clear();
        }
        if (null != byteAll) {
            byteAll = null;
        }
        return true;
    }

    /**
     * 检查缓冲区是否为空
     */
    private boolean isBufferEmpty(boolean input) {
        return !(null != mReadBuffer && mReadBuffer.hasArray());
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @return HexString
     */
    private String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * hex转string
     */
    private static String Hex2Str(byte[] hexByteIn) {
        int len = hexByteIn.length;
        String restult = "";
        for (int i = 0; i < len; i++) {
            restult += String.format("%02x", hexByteIn[i]);
        }
        return restult;
    }

    /**
     * 内容拼接为整条指令
     */
    private byte[] splicingInstructions(String srcStr) {
        byte[] bytes = null;
        try {
            byte[] srcValue = srcStr.getBytes("UTF-8");
            byte[] transferValue = new byte[srcValue.length + 4];
            transferValue[0] = 0x02;
            transferValue[1] = (byte) ((srcValue.length >> 8) & 0xFF);
            transferValue[2] = (byte) (srcValue.length & 0xFF);
            System.arraycopy(srcValue, 0, transferValue, 3, srcValue.length);
            transferValue[transferValue.length - 1] = 0x03;
            bytes = transferValue;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 合并byte数据
     */
    private byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }


    /**
     * 转换波特率
     */
    private int getBps(int bpsTag) {
        int bps = 0;
        switch (bpsTag) {
            case 0x01:
                bps = 1200;
                break;
            case 0x02:
                bps = 2400;
                break;
            case 0x03:
                bps = 4800;
                break;
            case 0x04:
                bps = 9600;
                break;
            case 0x05:
                bps = 14400;
                break;
            case 0x06:
                bps = 28800;
                break;
            case 0x07:
                bps = 19200;
                break;
            case 0x08:
                bps = 57600;
                break;
            case 0x09:
                bps = 115200;
                break;
            case 0x0A:
                bps = 38400;
                break;
        }
        return bps;
    }

    /**
     * 转换校验
     */
    private int getmPar(int parTag) {
        int par = 0;
        switch (parTag) {
            case 'N':
                par = UsbSerialPort.PARITY_NONE;
                break;
            case 'E':
                par = UsbSerialPort.PARITY_EVEN;
                break;
            case 'O':
                par = UsbSerialPort.PARITY_ODD;
                break;
        }
        return par;
    }
}
