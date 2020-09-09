package com.hoho.android.usbserial.util;

/**
 * 串口
 */

public class SerialPortCode {
    /**
     * 1200波特率
     */
    public static final int BPS_1200 = 0x01;
    /**
     * 2400波特率
     */
    public static final int BPS_2400 = 0x02;
    /**
     * 4800波特率
     */
    public static final int BPS_4800 = 0x03;
    /**
     * 9600波特率
     */
    public static final int BPS_9600 = 0x04;
    /**
     * 14400波特率
     */
    public static final int BPS_14400 = 0x05;
    /**
     * 28800波特率
     */
    public static final int BPS_28800 = 0x06;
    /**
     * 19200波特率
     */
    public static final int BPS_19200 = 0x07;
    /**
     * 57600波特率
     */
    public static final int BPS_57600 = 0x08;
    /**
     * 115200波特率
     */
    public static final int BPS_115200 = 0x09;
    /**
     * 38400波特率
     */
    public static final int BPS_38400 = 0x0A;


    /**
     * 无效验（缺省）
     */
    public static final int PAR_NOPAR = 'N';
    /**
     * 偶效验
     */
    public static final int PAR_EVEN = 'E';
    /**
     * 奇效验
     */
    public static final int PAR_ODD = 'O';


    /**
     * 7 位数据位
     */
    public static final int DBS_7 = 0x07;
    /**
     * 偶8 位数据位（缺省）
     */
    public static final int DBS_8 = 0x08;
    /**
     * 打开串口错
     */
    public static final int ERROR_OPEN = 0xff11;
    /**
     * 偶串口写数据错
     */
    public static final int ERROR_WRITE_DATA = 0xff12;
    /**
     * 超时
     */
    public static final int ERROR_TIMEOUT = 0xff13;
    /**
     * 接收数据错(如长度等)
     */
    public static final int ERROR_RECV_DATA = 0xff14;
    /**
     * 其他
     */
    public static final int ERROR_OTHER = 0xff15;





    /*SM2 公钥密钥ID*/
    public static final int _VENDOR_SM2_PK = 620;
    public static final int _EPP_SM2_SIG_SK = 621;
    public static final int _EPP_SM2_SIG_PK = 622;
    public static final int _EPP_SM2_EXC_SK = 623;
    public static final int _EPP_SM2_EXC_PK = 624;
    public static final int _EPP_SM2_CRYPT_SK = 625; //初始私钥POS
    public static final int _EPP_SM2_CRYPT_PK = 626; //初始公钥POS
    public static final int _HOST_SM2_EXC_PK = 627; //公钥签名POS
    public static final int _HOST_SM2_SIG_PK = 628; //公钥TOPS
    public static final int _HOST_SM2_SIG_PK_DLL = 629; //根公钥
    public static final int _HOST_SM2_PK = 630; //公钥签名TOPS
    public static final int KEK = 631; //主密钥
}
