package com.cloudwalk.livenesscameraproject.activity.launch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwalk.gldisplay.GLFrameSurface;
import com.cloudwalk.livenesscameraproject.BuildConfig;
import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.activity.settings.MainSettingActivity;
import com.cloudwalk.livenesscameraproject.activity.settings.SelectFolderActivity;
import com.cloudwalk.livenesscameraproject.db.DBManager;
import com.cloudwalk.livenesscameraproject.db.PersonFeatureModel;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.manager.ThreadManager;
import com.cloudwalk.livenesscameraproject.utils.Covert;
import com.cloudwalk.livenesscameraproject.utils.FileUtil;
import com.cloudwalk.livenesscameraproject.utils.FileUtils;
import com.cloudwalk.livenesscameraproject.utils.ImgUtil;
import com.cloudwalk.livenesscameraproject.utils.Logger;
import com.cloudwalk.livenesscameraproject.view.antishake.AntiShakeButton;
import com.hoho.android.usbserial.listener.OnErrorListener;
import com.hoho.android.usbserial.util.SerialPortUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceBeautyParam;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceDetectInfo;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceLivenessInfo;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceParam;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceRecogInfo;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceRect;
import cn.cloudwalk.component.liveness.entity.face.CWLiveImage;
import cn.cloudwalk.midware.camera.entity.CWCameraInfos;
import cn.cloudwalk.midware.camera.usb.USBUtil;
import cn.cloudwalk.midware.engine.CWCameraConfig;
import cn.cloudwalk.midware.engine.CWEngine;
import cn.cloudwalk.midware.engine.CWGeneralApi;
import cn.cloudwalk.midware.engine.CWHIMIApi;
import cn.cloudwalk.midware.engine.CWLivenessConfig;
import cn.cloudwalk.midware.engine.CWPreivewConfig;
import cn.cloudwalk.midware.engine.callback.CWFrameCallback;
import cn.cloudwalk.midware.engine.callback.CWLiveinfoCallback;
import cn.cloudwalk.midware.engine.view.CWRectView;

import static cn.cloudwalk.midware.engine.CWEngine.CW_CAMERA_STATE_OPENED;
import static cn.cloudwalk.midware.engine.CWEngine.CW_FUNC_MULTI_FACES;
import static cn.cloudwalk.midware.engine.utils.YuvToBitmapUtils.I420ToNV21;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_DIST_TOO_CLOSE_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_DIST_TOO_FAR_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_FACE_CLARITY_DET_FAIL_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_IS_LIVE;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_IS_UNLIVE_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_NO_PAIR_FACE_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_POSE_DET_FAIL_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_SKIN_FAILED_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_BLACKSPEC_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_BRIGHTNESS_EXC_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_BRIGHTNESS_INS_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_EYE_CLOSE_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_FACE_LOW_CONF_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_MOUTH_OPEN_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_NO_FACE_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_OCCLUSION_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_PROCEDUREMASK_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_LIV_VIS_SUNGLASS_ERR;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    //demo
    private static final int MSG_SEND_TIP = 10;
    private static final int MSG_SEND_START_LIV_TIP = 11;
    private static final int MSG_SEND_STOP_LIV_TIP = 12;
    private static final int MSG_SEND_IMAGE_FEATURE = 13;
    private static final int MSG_SAVE_BESTFACE = 14;
    private static final int MSG_SAVE_BGRIMAGE = 15;
    private static final int MSG_START_PREVIEW = 16;

    private boolean isShownPreviewBgImage;
    private boolean isLiveEnable;
    private int surfaceWidth, surfaceHeight;
    private static final String BESTFACE_PATH = "/sdcard/cloudwalk/最佳人脸/";
    private static final String CURRENT_IMAGE_PATH = "/sdcard/cloudwalk/当前照片/";
    private static final String ROOT_PATH = "/sdcard/cloudwalk/";
    //人脸比对begin
    private String selectedFilePath;
    private int featureImageGetType = 1;    //获取当前图片来源类型（比对）0-人脸 1-活体
    private boolean isCompare = false;
    private boolean isRegisterFature = false;
    private long compareFaceDetictTimeout = 5000;
    private boolean isSavePassImage = true;
    private boolean isSavefailImage = true;
    private boolean isSavePassAllImage = true;
    private boolean isTakePhoto = false;
    private boolean isInit = false;
    private int timeout;
    private int mCount = -1;
    private boolean isTimeout = false;
    private int failCount = 0;
    private int sucCount = 0;
    private int timeoutCount = 0;
    private String mLastTimeOut;  //记录串口通讯上一次超时时间，防止乱码

    private GLFrameSurface mSrVisView;
    private ImageView mIvSettings, mIvCameraBg;
    private FrameLayout mFlSurface;
    //liveness
    private TextView mTvResult;
    private SafeHandler handler = new SafeHandler(this);
    private CWLiveFaceRect lastLiveRect;
    private CWLiveImage lastLiveImage;
    private List<PersonFeatureModel> featureModels = new ArrayList<>();
    //camera
    private Button mBtnStartPreview, mBtnStopPreview, mBtnStartLiveness;
    private CWRectView mRectView;
    private ImageView mIvFaceMask;
    private Button mBtnRegisterFace, mBtnCompare, mBtnResetLast, mBtnImageCompare;
    private TextView mTvCompareResult, mTvCompareName, mTvScanResult;
    private ImageView mIvLastLiveImage, mIvCompareImage;
    //人脸比对end
    private static Bitmap bitmapLivDet;
    private Bitmap bgFaceMask;
    private Bitmap bgImageBitmap;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private View mTransprantView;
    private TextView txt_livness_status;
    private CheckBox cb_save_pass;
    private CheckBox cb_save_fail;
    private CheckBox cb_save_pass_all;

    private ImageView mBCTCBack;

    private ImageView mCameraSelect;

    /**
     * 活检失败  BCTC接口
     */
    private void sendFailLivness(byte[] bgr) {
        failCount++;
        Log.e("zhangfei", "qqqqqq");
        if (isTimeout) {
            return;
        }
        Log.e("zhangfei", "wwwwww");
        handler.removeCallbacks(mCounter);
        byte[] cmd = "CCCC".getBytes();
        Log.e("zhangfei", "活体检测失败");
        String time = FileUtils.getStringDate();

        bctcHandle.sendEmptyMessage(0);

        try {
            SerialPortUtil.getInstance().write(cmd, 10);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (isSavefailImage) {
            FileUtils.createMyBitmap(bgr, "/sdcard/image/假体/" + time + "-0.jpg", 480, 640);
        }
    }

    private class SavePictureTask extends AsyncTask<String, Object, Long> {
        byte[] bgr;
        byte[] ir;
        byte[] depth;
        CWLiveImage bestface;

        SavePictureTask(byte[] bgr, byte[] ir, byte[] depth, CWLiveImage bestface) {
            super();
            this.bgr = bgr;
            this.ir = ir;
            this.depth = depth;
            this.bestface = bestface;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Long aLong) {
            super.onCancelled(aLong);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Long doInBackground(String... strings) {
            String time = FileUtils.getStringDate();
            if (isSavePassAllImage) {
                FileUtils.createMyBitmap(bgr, "/sdcard/image/活体/" + time + "-1.jpg", 480, 640);
                FileUtils.createMyBitmap(ir, "/sdcard/image/活体/" + time + "-3.jpg", 480, 640);
                try {
                    FileOutputStream fos = new FileOutputStream("/sdcard/image/活体/" + time + "-4");
                    fos.write(depth);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (null != bestface) {
                    FileUtils.createMyBitmap(bestface.getData(), "/sdcard/image/活体/" + time + "-2.jpg", bestface.getWidth(), bestface.getHeight());
                }
            } else {
                if (isSavePassImage) {
                    FileUtils.createMyBitmap(bgr, "/sdcard/image/活体/" + time + "-1.jpg", 480, 640);
                }
            }
            return null;
        }
    }

    private void sendSucLivness(byte[] bgr, byte[] ir, byte[] depth, CWLiveImage bestface) {
        sucCount++;
        Log.e("zhangfei", "eeeeeeee");
        if (isTimeout) {
            Log.e("zhangfei", "rrrrrr");
            return;
        }
        handler.removeCallbacks(mCounter);
        Log.e("zhangfei", "ttttttt");
        byte[] cmd = "AAAA".getBytes();
//        Toast.makeText(MainActivity.this, "活体检测成功", Toast.LENGTH_SHORT).show();
        Log.e("zhangfei", "活体检测成功");

        bctcHandle.sendEmptyMessage(1);
        try {
            SerialPortUtil.getInstance().write(cmd, 10);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        new SavePictureTask(bgr, ir, depth, bestface).execute();
    }

    private CWLiveinfoCallback mLiveinfoCallback = new CWLiveinfoCallback() {

        @Override
        public void onFaceDetectCallback(int errorCode,
                                         long timestamp,
                                         final CWLiveImage image,
                                         ArrayList<CWLiveFaceDetectInfo> faceDetectInfoList) {
            if (null == image) {
                return;
            }
            Log.e("zhangfei", "face callback");
        }

        @Override
        public void onLivenessCallback(int errorCode,
                                       long timestamp,
                                       final CWLiveImage bgrImage,
                                       CWLiveImage irImage,
                                       CWLiveImage depthImage,
                                       ArrayList<CWLiveFaceDetectInfo> faceDetectInfoList,
                                       ArrayList<CWLiveFaceLivenessInfo> faceLivenessList,
                                       CWLiveImage bestface) {
            if (choice == 0) {
                runOnUiThread(() -> CWHIMIApi.getInstance().cwStopLiveDetect());
            }

            if (faceLivenessList != null && faceLivenessList.size() > 0) {
                Log.e("zhangfei", "onLivenessCallback code=" + faceLivenessList.get(0).getCode() + " timestamp=" + timestamp + ";format = " + (depthImage == null));
                if (faceLivenessList.get(0).getCode() == CW_FACE_LIV_IS_LIVE) {//人脸检测通过
                    if (null != bgrImage && null != irImage && null != depthImage) {
                        sendSucLivness(bgrImage.getData(), irImage.getData(), depthImage.getData(), bestface);
                    } else {
                        sendFailLivness(bgrImage.getData());
                    }
                } else {
                    sendFailLivness(bgrImage.getData());
                }
            } else {
                sendFailLivness(bgrImage.getData());
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler bctcHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    txt_livness_status.setText("非活体");
                    txt_livness_status.setBackgroundResource(R.color.red);
                    break;
                case 1:
                    txt_livness_status.setText("活体");
                    txt_livness_status.setBackgroundResource(R.color.green);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if ("1".equals(BuildConfig.MIDWARE_1VN_ENABLE)) {
            setContentView(R.layout.activity_main_1vn);
            surfaceHeight = dm.heightPixels / 3;
            surfaceWidth = surfaceHeight * 3 / 4;
        } else {
            setContentView(R.layout.activity_main);
            surfaceHeight = dm.heightPixels / 2;
            surfaceWidth = surfaceHeight * 3 / 4;
        }
        initView();
        updateView(CacheManager.getInstance().loadCameraType(1));

        initSerialPort();
    }

    private Runnable mCounter = new Runnable() {
        @Override
        public void run() {
            Log.e("zhangfei", "zhangfei mCount = " + mCount);
            if (mCount <= 1) {
                isTimeout = true;
                failCount++;
                timeoutCount++;
                Log.e("zhangfei", "活体检测超时");
                bctcHandle.sendEmptyMessage(0);
                try {
                    SerialPortUtil.getInstance().write("CCCC".getBytes(), 10);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                handler.removeCallbacks(mCounter);
            } else {
                mCount--;
                isTimeout = false;
                handler.postDelayed(mCounter, 1000);
            }

        }
    };

    private void initSerialPort() {
        //在子线程打开串口，防止可能卡死的情况
        new Thread() {
            @Override
            public void run() {
                //串口工具开始
                try {
                    SerialPortUtil.getInstance().start(MainActivity.this, 10,
                            new OnErrorListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "串口打开成功");
                                    showToast("串口打开成功");
                                }

                                @Override
                                public void onError(int errorCode) {
                                    Log.d(TAG, "串口打开失败，错误码为=" + errorCode);
                                    showToast("串口打开失败");
                                }
                            }, msg -> {
                                String msgStr = new String(msg);
                                Log.d(TAG, "接收到的指令：" + msgStr + "");

                                if (!isInit) {
                                    return;
                                }
                                String messageData = msgStr.substring(6, 8);
                                if (isMessyCode(messageData)) {  //增加字符串乱码判断
                                    Log.e("zhangfei unicode error", messageData);
                                    if (TextUtils.isEmpty(mLastTimeOut)) {
                                        mLastTimeOut = "04";
                                    }
                                    timeout = Integer.parseInt(mLastTimeOut);
                                } else {
                                    timeout = Integer.parseInt(messageData);
                                    mLastTimeOut = messageData;
                                }
                                isTimeout = false;
                                if (timeout == 0) {//接受到的指令超时为00
                                    Log.e("zhangfei", "活体检测超时");
                                    try {
                                        SerialPortUtil.getInstance().write("CCCC".getBytes(), 10);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mCount = timeout;
                                    handler.post(mCounter);
                                    if (choice == 0) {
                                        runOnUiThread(() -> {
                                            CWHIMIApi.getInstance().cwStartCamera();
                                            CWHIMIApi.getInstance().cwStartLiveDetect(1);
                                        });

                                    } else {  // TODO: 2020/9/8 串口指令调用接口
                                        CWGeneralApi.getInstance().setStartPushFrame(true);
                                    }
                                }
                            });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        CWEngine.getInstance().cwSetLiveInfoCallback(mLiveinfoCallback);
        resetParams();
        featureImageGetType = CacheManager.getInstance().loadFeatureImageGetType(1);
        CacheManager.getInstance().saveFeatureImageGetType(featureImageGetType);
        featureModels = DBManager.getInstance().getPersonFeatureModelDao().loadAll();
    }

    private int choice = 1;
    private final String[] items = {"2328", "2338"};

    /**
     * Camera select dialog
     */
    private void showSingSelect() {
        choice = 0;
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.camera)
                .setTitle("相机")
                .setSingleChoiceItems(items, choice, (dialogInterface, i) -> choice = i)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    Toast.makeText(MainActivity.this, "你选择了" + items[choice], Toast.LENGTH_LONG).show();
                    CWEngine.getInstance().cwUninit();
                    CWEngine.getInstance().releaseSrc();
                    ThreadManager.getThreadPool().execute(() -> {
                        initConfig();
                    });
                })
                .create()
                .show();
    }


    private void initView() {
        CWGeneralApi.getInstance().cwSetErrorCallback((error, info) -> Log.e("errorCode=  ", error + "   " + info));

        //BCTC view
        mBCTCBack = findViewById(R.id.bctc_back);
        mBCTCBack.setOnClickListener(v -> finish());
        mCameraSelect = findViewById(R.id.camera_select);
        mCameraSelect.setOnClickListener(v -> showSingSelect());

        txt_livness_status = findViewById(R.id.txt_livness_status);
        cb_save_pass = findViewById(R.id.cb_save_pass);
        cb_save_pass.setOnCheckedChangeListener((buttonView, isChecked) -> isSavePassImage = isChecked);
        cb_save_fail = findViewById(R.id.cb_save_fail);
        cb_save_fail.setOnCheckedChangeListener((buttonView, isChecked) -> isSavefailImage = isChecked);
        cb_save_pass_all = findViewById(R.id.cb_save_pass_all);
        cb_save_pass_all.setOnCheckedChangeListener((buttonView, isChecked) -> isSavePassAllImage = isChecked);
        //1vn相关
        mBtnResetLast = findViewById(R.id.btn_reset);
        mBtnRegisterFace = findViewById(R.id.btn_register_face);
        mBtnCompare = findViewById(R.id.btn_compare);
        mBtnImageCompare = findViewById(R.id.btn_image_compare);
        mIvLastLiveImage = findViewById(R.id.iv_last_live);
        mIvCompareImage = findViewById(R.id.iv_compare_result);
        mTvCompareResult = findViewById(R.id.tv_score);
        mTvCompareName = findViewById(R.id.tv_feature_name);
        mTransprantView = findViewById(R.id.transparentView);
        mTransprantView.setClickable(true);   //抢夺焦点
        mTransprantView.setFocusable(true);
        mTvScanResult = findViewById(R.id.tv_scan_result);

        if ("1".equals(BuildConfig.MIDWARE_1VN_ENABLE)) {
            mBtnResetLast.setOnClickListener(v -> {
                if (CWEngine.getInstance().cwGetCameraStatus() != CW_CAMERA_STATE_OPENED) {
                    showToast("请打开相机");
                    return;
                } else {
                    updateDetect(true);
                }
                lastLiveImage = null;
                lastLiveRect = null;
                mIvLastLiveImage.setImageDrawable(getResources().getDrawable(R.drawable.black));
            });
            mBtnRegisterFace.setOnClickListener(v -> {
                if (lastLiveImage == null) {
                    showToast("暂无活体图像");
                    return;
                }
                if (isRegisterFature) {
                    showToast("注册人脸信息中");
                    return;
                }
                isRegisterFature = true;
                final CWLiveImage image = lastLiveImage;
                final CWLiveFaceRect rect = lastLiveRect;
                ThreadManager.getThreadPool().execute(() -> {
                    isRegisterFature = true;
                    long tmp = System.currentTimeMillis();
                    CWLiveFaceRecogInfo cwLiveFaceRecogInfo = CWEngine.getInstance().cwGetFaceFeature(image,
                            rect != null ? rect : null, 1, CWEngine.getInstance().cwGetFeatureLength());
                    Logger.e(TAG, "注册当前图片 cwGetFaceFeature  time: " + (System.currentTimeMillis() - tmp));
                    if (cwLiveFaceRecogInfo != null) {
                        PersonFeatureModel model = new PersonFeatureModel();
                        model.setName(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
                        model.setFeature_data(cwLiveFaceRecogInfo.getFeature_data());
                        try {
                            Bitmap bitmap = ImgUtil.byteArrayBGRToBitmap(image.getData(), image.getWidth(), image.getHeight());
                            String filePath = "/sdcard/cloudwalk/feature/";
                            cn.cloudwalk.midware.engine.utils.FileUtil.isFolderExists(filePath);
                            File file2 = new File(filePath + System.currentTimeMillis() + ".jpg");
                            FileOutputStream out = new FileOutputStream(file2);
                            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out)) {
                                out.flush();
                                out.close();
                            }
                            model.setPath(file2.getPath());
                        } catch (Exception e) {
                            showToast("存储特征图片失败");
                            isRegisterFature = false;
                            e.printStackTrace();
                            return;
                        }
                        tmp = System.currentTimeMillis();
                        DBManager.getInstance().getPersonFeatureModelDao().insertOrReplaceInTx(model);
                        Logger.e(TAG, "feature Save to Lib  time: " + (System.currentTimeMillis() - tmp));
                        showToast("注册成功");
                        featureModels.add(model);
                    } else {
                        showToast("注册失败");
                    }
                    isRegisterFature = false;
                });
            });
            mBtnCompare.setOnClickListener(v -> {
                if (isCompare) {
                    showToast("人脸比对中");
                    return;
                }
                isCompare = true;
                mIvCompareImage.setImageDrawable(getResources().getDrawable(R.drawable.black));
                mTvCompareResult.setText("");
                mTvCompareName.setText("");
                if (CWEngine.getInstance().cwGetCameraStatus() != CW_CAMERA_STATE_OPENED) {
                    showToast("请打开相机");
                    isCompare = false;
                    return;
                } else {
                    updateDetect(true);
                }
                lastLiveImage = null;
                lastLiveRect = null;
                mIvLastLiveImage.setImageDrawable(getResources().getDrawable(R.drawable.black));
            });
            mBtnImageCompare.setOnClickListener(v -> {
                if (isCompare) {
                    showToast("人脸比对中");
                    return;
                }
                updateDetect(false);
                mIvCompareImage.setImageDrawable(getResources().getDrawable(R.drawable.black));
                mTvCompareResult.setText("");
                mTvCompareName.setText("");
                Intent intent = new Intent(MainActivity.this, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "selectedFilePath");
                startActivityForResult(intent, 0);
            });
        }

        isShownPreviewBgImage = CacheManager.getInstance().loadIsShowPreviewBgImg(true);
        mFlSurface = findViewById(R.id.fl_surface);
        AntiShakeButton mBtnCaptureImage = (AntiShakeButton) findViewById(R.id.btn_capture_image);
        mBtnCaptureImage.setOnClickListener(v -> {
            if (!isInit) {
                showToast("初始化未完成");
            }
            isTakePhoto = true;
        });

        mIvFaceMask = (ImageView) findViewById(R.id.iv_face_mask);

        mRectView = (CWRectView) findViewById(R.id.rectview);
        mSrVisView = (GLFrameSurface) findViewById(R.id.sr_vis_view);
        mTvResult = (TextView) findViewById(R.id.tv_liveness_result);
        mIvCameraBg = (ImageView) findViewById(R.id.iv_camera_bg);
        if (isShownPreviewBgImage) {
            mIvCameraBg.setBackgroundResource(R.drawable.img_camera_bg);
        }
        mIvSettings = (ImageView) findViewById(R.id.iv_setttings);
        mIvSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainSettingActivity.class)));

        mBtnStartPreview = (Button) findViewById(R.id.btn_start_preview);
        mBtnStartPreview.setOnClickListener(v -> updatePreview(true));
        mBtnStopPreview = (Button) findViewById(R.id.btn_stop_preview);
        mBtnStopPreview.setOnClickListener(v -> updatePreview(false));
        mBtnStartLiveness = (Button) findViewById(R.id.btn_start_liveness);
        mBtnStartLiveness.setOnClickListener(v -> updateDetect(!isLiveEnable));

        findViewById(R.id.btn_start_bctc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(1001);
            }
        });

        findViewById(R.id.btn_stop_bctc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWGeneralApi.getInstance().setStartPushFrame(false);
                handler.removeMessages(1001);
                handler.removeMessages(1002);
            }
        });
        CacheManager.getInstance().saveCameraSelectIndex(3);
        CacheManager.getInstance().saveCameraType(4);
        updateView(4);
        requestPermission();
    }

    private void takePicture(final byte[] image, final int width, final int height) {
        ThreadManager.getThreadPool().execute(() -> {
            byte[] visByte = new byte[(width * height * 3) / 2];
            I420ToNV21(image, visByte, width, height);
            float angle = CacheManager.getInstance().loadRotation(90); //默认旋转角度为90度
            float sx = CacheManager.getInstance().loadMirror(false) ? -1 : 1;
            String time = FileUtils.getStringDate();
            String path = "/sdcard/image/当前图片/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            cn.cloudwalk.midware.engine.utils.ImgUtil.saveYuvImage(path + time + ".jpg", visByte,
                    width,
                    height, 80, Bitmap.CompressFormat.PNG, angle, sx, 1);
        });
    }

    private void processFaceMask() {
        boolean isShownFaceMask = CacheManager.getInstance().loadIsShowFaceMask(false);
        if (isShownFaceMask) {
            mIvFaceMask.setVisibility(View.VISIBLE);
        } else {
            mIvFaceMask.setVisibility(View.GONE);
            return;
        }
        int mCameraType = CacheManager.getInstance().loadCameraType(1);
        if (!TextUtils.isEmpty(CacheManager.getInstance().loadFaceMaskImagePath(""))) {
            String faceMaskPath = CacheManager.getInstance().loadFaceMaskImagePath("");
            if (mCameraType == 3) {
                bgFaceMask = ImgUtil.zoomPic(faceMaskPath, surfaceHeight, surfaceWidth, Bitmap.Config.ARGB_8888);
            } else {
                bgFaceMask = ImgUtil.zoomPic(faceMaskPath, surfaceWidth, surfaceHeight, Bitmap.Config.ARGB_8888);
                if (null != bgFaceMask) {
                    mIvFaceMask.setImageBitmap(bgFaceMask);
                }
            }
        } else {
            if (mCameraType == 3) {
                mIvFaceMask.setImageResource(R.drawable.bg_face_mask_land);
            } else {
                mIvFaceMask.setImageResource(R.drawable.bg_face_mask_port);
            }

        }
    }

    private ArrayList<CWCameraInfos> cameraInfoList = new ArrayList<>();

    private void resetParams() {
        isShownPreviewBgImage = CacheManager.getInstance().loadIsShowPreviewBgImg(true);
        processFaceMask();

        //预览背景图
        if (CWEngine.getInstance().cwGetCameraStatus() != CW_CAMERA_STATE_OPENED) {
            if (isShownPreviewBgImage) {
                mIvCameraBg.setVisibility(View.VISIBLE);
            } else {
                mIvCameraBg.setVisibility(View.GONE);
            }
        } else {
            mIvCameraBg.setVisibility(View.GONE);
        }

        String previewImagePath = CacheManager.getInstance().loadPreviewImagePath("");

        if (isShownPreviewBgImage) {
            if (!TextUtils.isEmpty(previewImagePath)) {
                int cameraType = CacheManager.getInstance().loadCameraType(1);
                int width = surfaceWidth, height = surfaceHeight;
                if (cameraType == 3) {
                    width = surfaceHeight;
                    height = surfaceWidth;
                }
                bgImageBitmap = ImgUtil.zoomPic(previewImagePath, width, height, Bitmap.Config.ARGB_8888);
                if (null != bgImageBitmap) {
                    mIvCameraBg.setImageBitmap(bgImageBitmap);
                }
            } else {
                if (bgImageBitmap != null) {
                    mIvCameraBg.setImageBitmap(null);
                    bgImageBitmap.recycle();
                    bgImageBitmap = null;
                }
                mIvCameraBg.setBackgroundResource(R.drawable.img_camera_bg);
            }
        }


        //摄像头预览开关sp
        boolean isPreview = CacheManager.getInstance().loadIsPreview(true);
        if (isPreview) {
            CWEngine.getInstance().cwEnablePreview(true);
        } else {
            CWEngine.getInstance().cwEnablePreview(false);
        }
        //预览角度和镜像sp
        int rotation = CacheManager.getInstance().loadRotation(90);
        Logger.d(TAG, "rotation=" + rotation);
        CWEngine.getInstance().cwSetPreviewRotate(rotation);

        boolean isMirror = CacheManager.getInstance().loadMirror(true);
        Logger.d(TAG, "isMirror=" + isMirror);
        CWEngine.getInstance().cwSetPreviewMirror(isMirror);

    }

    private void initConfig() {
        cameraInfoList.clear();
        //创建存图文件夹
        FileUtil.isFolderExists(ROOT_PATH);
        //最佳人脸及全景存图(默认)
        FileUtil.isFolderExists(BESTFACE_PATH);
        //当前拍照
        FileUtil.isFolderExists(CURRENT_IMAGE_PATH);

        CWCameraConfig cwCameraConfig = CWCameraConfig.getInstance();
        int cameraType = CacheManager.getInstance().loadCameraType(1);
        cameraType = choice == 0 ? 3 : 4;
        cwCameraConfig.setCameraType(cameraType);

        CWCameraInfos cameraInfosVis = new CWCameraInfos();
        CWCameraInfos cameraInfosNis = new CWCameraInfos();
        if (cameraType == 4) {
            cameraInfosVis.setCameraWidth(640);
            cameraInfosVis.setCameraHeight(480);
            cameraInfosVis.setType(0);
            cameraInfosVis.setPid(13195);
            cameraInfosVis.setVid(12968);

            cameraInfosNis.setCameraWidth(800);
            cameraInfosNis.setCameraHeight(960);
            cameraInfosNis.setType(1);
            cameraInfosNis.setPid(13194);
            cameraInfosNis.setVid(12968);
        } else {
            cameraInfosVis.setCameraWidth(CacheManager.getInstance().loadCameraWidth(640));
            cameraInfosVis.setCameraHeight(CacheManager.getInstance().loadCameraHeight(480));
            cameraInfosVis.setType(0);
            Logger.d(TAG, "CacheManager.getInstance().loadRgbPid(\"rgbPid\", 0xC053)-------" + CacheManager.getInstance().loadRgbPid(0xC053));
            Logger.d(TAG, "CacheManager.getInstance().loadRgbVid(0x0C45)(\"rgbVid\", 0x0C45)-------" + CacheManager.getInstance().loadRgbVid(0x0C45));
            cameraInfosVis.setPid(CacheManager.getInstance().loadRgbPid(0xC053));//0xB051
            cameraInfosVis.setVid(CacheManager.getInstance().loadRgbVid(0x0C45));

            cameraInfosNis.setCameraWidth(CacheManager.getInstance().loadCameraWidth(640));
            cameraInfosNis.setCameraHeight(CacheManager.getInstance().loadCameraHeight(480));
            cameraInfosNis.setType(1);
            Logger.d(TAG, "CacheManager.getInstance().loadIrPid(0xB051)(\"irPid\", 0xB051)-------" + CacheManager.getInstance().loadIrPid(0xB051));
            Logger.d(TAG, "CacheManager.getInstance().loadIrVid(\"irVid\", 0x0C45)-------" + CacheManager.getInstance().loadIrVid(0x0C45));
            cameraInfosNis.setPid(CacheManager.getInstance().loadIrPid(0xB051));//0xC053
            cameraInfosNis.setVid(CacheManager.getInstance().loadIrVid(0x0C45));
        }
        cameraInfoList.add(cameraInfosVis);
        cameraInfoList.add(cameraInfosNis);

        CWPreivewConfig cwVisPreivewConfig = CWPreivewConfig.getInstance();
        cwVisPreivewConfig.setAngle(CacheManager.getInstance().loadRotation(90));
        cwVisPreivewConfig.setMirror(CacheManager.getInstance().loadMirror(true));
        cwVisPreivewConfig.setSurfaceView(mSrVisView);
        cwVisPreivewConfig.setCwRectView(mRectView);
        cwCameraConfig.setCameraInfos(cameraInfoList);

        CWLivenessConfig cwLivenessConfig = CWLivenessConfig.getInstance();
        /* 添加背景图和存图路径 */
        Logger.d(TAG, "isSaveDebugImage=" + CacheManager.getInstance().loadIsSaveDebugImage(false));
        cwLivenessConfig.setDefendSave(CacheManager.getInstance().loadIsSaveDebugImage(false));
        cwLivenessConfig.setDefendFile(TextUtils.isEmpty(CacheManager.getInstance().loadDebugImagePath("")) ?
                null : CacheManager.getInstance().loadDebugImagePath(""));
        if ("1".equals(BuildConfig.MIDWARE_1VN_ENABLE)) {
            cwLivenessConfig.setConfigFile("/sdcard/assets/matrix_para.xml");
        } else {
            cwLivenessConfig.setConfigFile("/sdcard/assets/matrix_para_liv_only.xml");
        }
        cwLivenessConfig.setFaceDetectFile("/sdcard/assets/face_3_27_dpn");
        cwLivenessConfig.setFaceKeyPointDetectFile("/sdcard/assets/kpt_model_20200311.dpn");
        cwLivenessConfig.setFaceQualityFile("/sdcard/assets/faceanalyze_20200603.dpn");
        cwLivenessConfig.setFaceRecogFile("/sdcard/assets/dp_mask_200305/CWR_Config3.0_1_1.xml");
//        Logger.d(TAG, cwLivenessConfig.getFaceLivenessFile());
        cwLivenessConfig.setStrategyId(CacheManager.getInstance().loadKeyId(3));  //默认第四种策略
        cwLivenessConfig.setLivenessMinutes(CacheManager.getInstance().loadKeyS(0));
        cwLivenessConfig.setLivenessCount(CacheManager.getInstance().loadKeyM(0));
        cwLivenessConfig.setLivenesSuccessCount(CacheManager.getInstance().loadKeyN(0));

        cwLivenessConfig.setModelMode(0);
        switch (cameraType) {
            case 0:
            case 1:
            case 2:
                cwLivenessConfig.setLivenessMode(2);
                cwLivenessConfig.setFaceLivenessFile("/sdcard/assets/nirLiveness_model_20200618.dpn");
                break;
            case 3:
            case 4:
                //结构光单独模型
                cwLivenessConfig.setLivenessMode(3);
                cwLivenessConfig.setFaceLivenessFile("/sdcard/assets/structured200506.dpn");
                break;
        }
        cwLivenessConfig.setLicenseType(4);
        cwLivenessConfig.setMultiThread(false);
        cwLivenessConfig.setTrackPreviewHeight(CacheManager.getInstance().loadTrackPreviewHeight(640));
        cwLivenessConfig.setTrackPreviewWidth(CacheManager.getInstance().loadTrackPreviewWidth(480));
        cwLivenessConfig.setAvalableSpace(200);
        cwLivenessConfig.setFaceRatio(CacheManager.getInstance().loadBestFaceRatio(1.5f));
        cwLivenessConfig.setDefendSaveFormat(CacheManager.getInstance().loadDefineSaveFormat("jpg"));

        long timestamp1 = System.currentTimeMillis();
        CWEngine.getInstance().setLogLevel(cn.cloudwalk.midware.engine.utils.Logger.WARN, ",");
        long ret = CWEngine.getInstance().cwInit(MainActivity.this, cwCameraConfig, cwLivenessConfig, cwVisPreivewConfig);
        Logger.d(TAG, "cwInit ret=" + ret);
        mTransprantView.setFocusable(false);
        mTransprantView.setClickable(false);
        if (ret == 0L) {
            isInit = false;
            showToast("初始化失败");
            Log.e("xjk init", " failed");
        } else {
            isInit = true;
            showToast("初始化成功");
            Log.e("xjk init", " succeed");

            handler.sendEmptyMessageDelayed(MSG_START_PREVIEW, 100);
        }
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String content = "总次数：" + (sucCount + failCount) + " 通过次数：" + sucCount + " 失败次数：" + failCount + " 该次活检通通过率：" + (float) ((float) sucCount / (float) (failCount + sucCount)) + ";超时次数：" + timeoutCount;
            Log.d(TAG, "活体检测结果：" + content);
            FileUtils.writeTxtToFile(content, "/sdcard/image/", "活体通过率统计.txt");

            updatePreview(false);

            if (bitmapLivDet != null && !bitmapLivDet.isRecycled()) {
                mIvLastLiveImage.setImageBitmap(null);
                bitmapLivDet.recycle();
                bitmapLivDet = null;
            }

            if (bitmap3 != null && !bitmap3.isRecycled()) {
                mIvLastLiveImage.setImageBitmap(null);
                bitmap3.recycle();
                bitmap3 = null;
            }

            if (bitmap4 != null && !bitmap4.isRecycled()) {
                mIvCompareImage.setImageBitmap(null);
                bitmap4.recycle();
                bitmap4 = null;
            }

            if (bgFaceMask != null && !bgFaceMask.isRecycled()) {
                mIvCameraBg.setImageBitmap(null);
                bgFaceMask.recycle();
                bgFaceMask = null;
            }

            if (bgImageBitmap != null && !bgImageBitmap.isRecycled()) {
                mIvCameraBg.setImageBitmap(null);
                bgImageBitmap.recycle();
                bgImageBitmap = null;
            }

            if (pushImageRunable != null) {  //author : xujunke 从线程队列中取消pushImage线程对象
                ThreadManager.getThreadPool().cancel(pushImageRunable);
            }

            if (compareRunable != null) {  //author : xujunke  从线程队列中取消compare线程对象
                ThreadManager.getThreadPool().cancel(compareRunable);
            }

            CWEngine.getInstance().cwUninit();
            CWEngine.getInstance().releaseSrc();
            if (USBUtil.getInstance() != null) {
                USBUtil.getInstance().unInit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        updateDetect(false);
    }

    private void showTips(int errorCode) {
        Message message = Message.obtain();
        message.what = MSG_SEND_TIP;
        Bundle bundle = new Bundle();
        switch (errorCode) {
            case CW_FACE_LIV_IS_LIVE:
                message.what = MSG_SEND_START_LIV_TIP;
                break;
            case CW_FACE_LIV_IS_UNLIVE_ERR:
                bundle.putString("tips", "非活体!");
                break;
            case CW_FACE_LIV_DIST_TOO_FAR_ERR:
                bundle.putString("tips", "请离远一点!");
                break;
            case CW_FACE_LIV_DIST_TOO_CLOSE_ERR:
                bundle.putString("tips", "请离近一点!");
                break;
            case CW_FACE_LIV_SKIN_FAILED_ERR:
                bundle.putString("tips", "人脸肤色检测未通过!");
                break;
            case CW_FACE_LIV_NO_PAIR_FACE_ERR:
                bundle.putString("tips", "可见光和红外人脸不匹配!");
                break;
            case CW_FACE_LIV_VIS_NO_FACE_ERR:
                bundle.putString("tips", "可见光输入没有人脸!");
                break;
            case CW_FACE_LIV_POSE_DET_FAIL_ERR:
                bundle.putString("tips", "请正对摄像头!");
                break;
            case CW_FACE_LIV_FACE_CLARITY_DET_FAIL_ERR:
                bundle.putString("tips", "请调整角度或距离确保照片清晰!");
                break;
            case CW_FACE_LIV_VIS_EYE_CLOSE_ERR:
                bundle.putString("tips", "请睁眼!");
                break;
            case CW_FACE_LIV_VIS_MOUTH_OPEN_ERR:
                bundle.putString("tips", "请不要张嘴!");
                break;
            case CW_FACE_LIV_VIS_BRIGHTNESS_EXC_ERR:
                bundle.putString("tips", "人脸照片过亮!");
                break;
            case CW_FACE_LIV_VIS_BRIGHTNESS_INS_ERR:
                bundle.putString("tips", "人脸照片过暗!");
                break;
            case CW_FACE_LIV_VIS_FACE_LOW_CONF_ERR:
                bundle.putString("tips", "检测到人脸置信度过低!");
                break;
            case CW_FACE_LIV_VIS_OCCLUSION_ERR:
                bundle.putString("tips", "请不要遮挡您的脸部!");
                break;
            case CW_FACE_LIV_VIS_BLACKSPEC_ERR:
                bundle.putString("tips", "请取下您的眼镜");
                break;
            case CW_FACE_LIV_VIS_SUNGLASS_ERR:
                bundle.putString("tips", "请取下您的墨镜");
                break;
            case CW_FACE_LIV_VIS_PROCEDUREMASK_ERR:
                bundle.putString("tips", "请摘下您的口罩");
                break;
            case CW_FUNC_MULTI_FACES:
                bundle.putString("tips", "有多张人脸!");
                break;
        }
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void setDetectorParams() {
        boolean isInit = CacheManager.getInstance().loadIsInit(true);
        if (!isInit) {
            try {
                CWLiveFaceParam cwLiveFaceParam = CWGeneralApi.getInstance().cwGetDetectorParams();
                Logger.d(TAG, cwLiveFaceParam == null ? "null" : CWEngine.getInstance().cwGetDetectorParams().toString());
                if (null == cwLiveFaceParam) {
                    return;
                }
                cwLiveFaceParam.setRoi_x(CacheManager.getInstance().loadRoiX(0));
                cwLiveFaceParam.setRoi_y(CacheManager.getInstance().loadRoiY(0));
                cwLiveFaceParam.setRoi_width(CacheManager.getInstance().loadRoiWidth(0));
                cwLiveFaceParam.setRoi_height(CacheManager.getInstance().loadRoiHeight(0));
                int height = CacheManager.getInstance().loadCameraHeight(480);
                int minFace = CacheManager.getInstance().loadMinFace(80);
                int maxFace = CacheManager.getInstance().loadMaxFace(400);
                if (CacheManager.getInstance().loadDefaultFace(1) == 1 && height > 0) {
                    float ration = height / 480f;
                    minFace = (int) (80 * ration);
                    maxFace = (int) (400 * ration);
                }
                cwLiveFaceParam.setMin_face_size(minFace);
                cwLiveFaceParam.setMax_face_size(maxFace);
                CacheManager.getInstance().saveMinFace(minFace);
                CacheManager.getInstance().saveMaxFace(maxFace);

                cwLiveFaceParam.setMax_face_num_perImg(CacheManager.getInstance().loadMaxFaceNum(1));
                cwLiveFaceParam.setPerfmonLevel(CacheManager.getInstance().loadPerformLevel(5));
                cwLiveFaceParam.setNir_face_compare(CacheManager.getInstance().loadIsCompare(0));
                cwLiveFaceParam.setOpen_liveness(CacheManager.getInstance().loadIsOpenLiveness(1));
                cwLiveFaceParam.setOpen_quality(CacheManager.getInstance().loadIsOpenQuality(1));
                cwLiveFaceParam.setPitch_max(CacheManager.getInstance().loadPitchMax(20.0f));
                cwLiveFaceParam.setPitch_min(CacheManager.getInstance().loadPitchMin(-20.0f));

                Logger.d("zxy", "zxy yaw max is " + CacheManager.getInstance().loadYawMax(20.0f));
                cwLiveFaceParam.setYaw_max(CacheManager.getInstance().loadYawMax(20.0f));

                Logger.d("zxy", "zxy yaw min is " + CacheManager.getInstance().loadYawMin(-20.0f));
                cwLiveFaceParam.setYaw_min(CacheManager.getInstance().loadYawMin(-20.0f));
                cwLiveFaceParam.setRoll_max(CacheManager.getInstance().loadRollMax(20.0f));
                cwLiveFaceParam.setRoll_min(CacheManager.getInstance().loadRollMin(-20.0f));
                cwLiveFaceParam.setClarity(CacheManager.getInstance().loadClarity(0.00004f));
                cwLiveFaceParam.setSkinscore(CacheManager.getInstance().loadSkin(0.35f));
                cwLiveFaceParam.setConfidence(CacheManager.getInstance().loadConfidence(0.55f));
                cwLiveFaceParam.setEyeopen(CacheManager.getInstance().loadEyeOpen(-1f));
                cwLiveFaceParam.setMouthopen(CacheManager.getInstance().loadMouthOpen(-1f));
                cwLiveFaceParam.setOcclusion(CacheManager.getInstance().loadOcclucsion(-1f));

                cwLiveFaceParam.setHight_brightness_threshold(CacheManager.getInstance().loadBrightness(-1f));
                cwLiveFaceParam.setLow_brightness_threshold(CacheManager.getInstance().loadDarkness(-1f));
                cwLiveFaceParam.setBlackspec(CacheManager.getInstance().loadBlackSpec(-1f));
                cwLiveFaceParam.setSunglass(CacheManager.getInstance().loadSunglass(-1f));
                cwLiveFaceParam.setProceduremask(CacheManager.getInstance().loadProceduremask(0.5f));
                Logger.d("zxy", "zxy glass is " + CacheManager.getInstance().loadBlackSpec(-1f));
                //Logger.d("zxy", "zxy sunglass is "+CacheManager.getInstance().loadSunglass(-1f));
                cwLiveFaceParam.setOpen_face_beauty(CacheManager.getInstance().loadOpenFaceBeauty(0));
                CWLiveFaceBeautyParam beautyParam = cwLiveFaceParam.getFace_beauty_param();
                if (beautyParam == null) {
                    beautyParam = new CWLiveFaceBeautyParam();
                }
                beautyParam.setBeauty_op(CacheManager.getInstance().loadFaceBeautyOp(6));
                beautyParam.setWhiten_intensity(CacheManager.getInstance().loadFaceBeautyWhiten(1));
                beautyParam.setSmooth_intensity(CacheManager.getInstance().loadFaceBeautySmooth(1));
                beautyParam.setFace_lift_intensity(CacheManager.getInstance().loadFaceBeautyLift(1));
                cwLiveFaceParam.setFace_beauty_param(beautyParam);
                CWGeneralApi.getInstance().cwSetDetectorParams(cwLiveFaceParam);
                CWGeneralApi.getInstance().cwSetDetectorParams(cwLiveFaceParam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            }
        } else {
            initConfig();
            resetParams();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    initConfig();
                    resetParams();

                }
                break;
        }
    }


    private class SafeHandler extends Handler {

        private WeakReference<MainActivity> mWeakReference;

        public SafeHandler(MainActivity activity) {
            mWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_SAVE_BESTFACE:
                        CWLiveImage bestface = (CWLiveImage) msg.obj;
                        if (null != bestface) {
                            Bitmap bitmapBestface = Covert.BGRToBitmap(bestface.getData(), bestface.getWidth(),
                                    bestface.getHeight());
                            Covert.saveToJpeg(bitmapBestface, BESTFACE_PATH + "最佳人脸.jpg", 100);
                            if (bitmapBestface != null && !bitmapBestface.isRecycled()) {
                                bitmapBestface.recycle();
                            }
                        }
                        break;
                    case MSG_SAVE_BGRIMAGE:
                        CWLiveImage bgrImage = (CWLiveImage) msg.obj;
                        if (null != bgrImage) {
                            Bitmap bitmapBestface = Covert.BGRToBitmap(bgrImage.getData(), bgrImage.getWidth(),
                                    bgrImage.getHeight());
                            Covert.saveToJpeg(bitmapBestface, BESTFACE_PATH + "全景图.jpg", 100);
                            if (bitmapBestface != null && !bitmapBestface.isRecycled()) {
                                bitmapBestface.recycle();
                            }
                        }
                        break;
                    case MSG_SEND_IMAGE_FEATURE:
                        CWLiveImage image = (CWLiveImage) msg.obj;
                        activity.bitmapLivDet = ImgUtil.byteArrayBGRToBitmap(image.getData(), image.getWidth(), image.getHeight());
                        activity.mIvLastLiveImage.setImageBitmap(bitmapLivDet);
                        break;
                    case MSG_SEND_STOP_LIV_TIP:
                        activity.mTvResult.setText("");
                        break;
                    case MSG_SEND_START_LIV_TIP:
                        if (activity.isLiveEnable && CWEngine.getInstance().cwGetCameraStatus() == CW_CAMERA_STATE_OPENED) {
                            activity.mTvResult.setText("活体");//
                            activity.mTvResult.setTextSize(CacheManager.getInstance().loadFontSize(15));
                            activity.mTvResult.setTextColor(Color.parseColor(CacheManager.getInstance().loadFontColor("#FFFFFF")));
                        }
                        break;
                    case MSG_SEND_TIP:
                        if (activity.isLiveEnable && CWEngine.getInstance().cwGetCameraStatus() == CW_CAMERA_STATE_OPENED) {
                            String tipsInfo = msg.getData().getString("tips");
                            activity.mTvResult.setText(tipsInfo);
                            activity.mTvResult.setTextSize(CacheManager.getInstance().loadFontSize(15));
                            activity.mTvResult.setTextColor(Color.parseColor(CacheManager.getInstance().loadFontColor("#FFFFFF")));
                        }
                        break;
                    case MSG_START_PREVIEW:
                        updatePreview(true);
                        break;
                    case 1001:      //模拟BCTC发送指令
                        CWGeneralApi.getInstance().setStartPushFrame(true);
                        handler.sendEmptyMessageDelayed(1002, 50);
                        break;
                    case 1002:
                        CWGeneralApi.getInstance().setStartPushFrame(true);
                        handler.sendEmptyMessageDelayed(1001, 50);
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {

        if (bitmapLivDet != null && !bitmapLivDet.isRecycled()) {
            mIvLastLiveImage.setImageBitmap(null);
            bitmapLivDet.recycle();
            bitmapLivDet = null;
        }

        if (bitmap3 != null && !bitmap3.isRecycled()) {
            mIvLastLiveImage.setImageBitmap(null);
            bitmap3.recycle();
            bitmap3 = null;
        }

        if (bitmap4 != null && !bitmap4.isRecycled()) {
            mIvCompareImage.setImageBitmap(null);
            bitmap4.recycle();
            bitmap4 = null;
        }

        if (bgFaceMask != null && !bgFaceMask.isRecycled()) {
            mIvCameraBg.setImageBitmap(null);
            bgFaceMask.recycle();
            bgFaceMask = null;
        }

        if (bgImageBitmap != null && !bgImageBitmap.isRecycled()) {
            mIvCameraBg.setImageBitmap(null);
            bgImageBitmap.recycle();
            bgImageBitmap = null;
        }

        if (pushImageRunable != null) {  //author : xujunke 从线程队列中取消pushImage线程对象
            ThreadManager.getThreadPool().cancel(pushImageRunable);
        }

        if (compareRunable != null) {  //author : xujunke  从线程队列中取消compare线程对象
            ThreadManager.getThreadPool().cancel(compareRunable);
        }

        CWEngine.getInstance().cwUninit();
        CWEngine.getInstance().releaseSrc();
        if (USBUtil.getInstance() != null) {
            USBUtil.getInstance().unInit();
        }
        SerialPortUtil.getInstance().cancel();
        super.onDestroy();
    }

    private void updateView(int mCameraType) {
        switch (mCameraType) {
            case 0:
            case 1:
            case 2:
            case 4:
                ViewGroup.LayoutParams fl_lp = mFlSurface.getLayoutParams();
                fl_lp.width = surfaceWidth;
                fl_lp.height = surfaceHeight;
                mFlSurface.setLayoutParams(fl_lp);
                processFaceMask();
                break;
            case 3:
                ViewGroup.LayoutParams fl_lp_hm = mFlSurface.getLayoutParams();
                fl_lp_hm.width = surfaceHeight;
                fl_lp_hm.height = surfaceWidth;
                mFlSurface.setLayoutParams(fl_lp_hm);
                processFaceMask();
                break;
        }
    }

    private void updateDetect(final boolean isEnable) {
        runOnUiThread(() -> {
            setDetectorParams();
            if (isEnable) {
                if (CWEngine.getInstance().cwGetCameraStatus() != CW_CAMERA_STATE_OPENED) {
                    return;
                }
                CWEngine.getInstance().cwSetLiveInfoCallback(mLiveinfoCallback);
                Logger.d("ANR_cwStartLiveDetect", "start");
                CWEngine.getInstance().cwStartLiveDetect(0);
                Logger.d("ANR_cwStartLiveDetect", "end");
                mBtnStartLiveness.setText("关闭活体");
                mBtnStartLiveness.setBackgroundColor(Color.RED);
                isLiveEnable = true;
            } else {
                //比对状态关闭
                isCompare = false;
                isLiveEnable = false;
                mBtnStartLiveness.setText("开始活体");
                mBtnStartLiveness.setBackgroundColor(Color.BLUE);
                Logger.d("ANR_cwStopLiveDetect", "start");
                CWEngine.getInstance().cwStopLiveDetect();
                handler.sendEmptyMessage(MSG_SEND_STOP_LIV_TIP);
            }
        });
    }

    private void updatePreview(final boolean isEnable) {
        runOnUiThread(() -> {
            if (isEnable) {
                mIvCameraBg.setVisibility(View.GONE);
                CWEngine.getInstance().cwSetLiveInfoCallback(mLiveinfoCallback);
                CWEngine.getInstance().cwStartCamera();
            } else {
                updateDetect(false);
                if (isShownPreviewBgImage) {
                    mIvCameraBg.setVisibility(View.VISIBLE);
                }
                CWEngine.getInstance().cwStopCamera();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                selectedFilePath = data.getStringExtra("path");
                ThreadManager.getThreadPool().execute(pushImageRunable);
            }
        }
    }

    private final Object pushObj = new Object();
    private Runnable pushImageRunable = new Runnable() {

        @Override
        public void run() {
            if (isCompare) {
                showToast("人脸比对中");
                return;
            }
            if (TextUtils.isEmpty(selectedFilePath)) {
                showToast("请选择文件");
                return;
            }
            final File file = new File(selectedFilePath);
            if (!file.exists()) {
                showToast("选择的文件不存在");
                return;
            }
            if (file.isDirectory() || !((file.getName().contains("png")
                    || file.getName().contains("jpg")
                    || file.getName().contains("jpeg")))) {
                showToast("暂只支持jpg/png格式文件");
            }
            isCompare = true;
            runOnUiThread(() -> {
                try {
                    bitmap3 = BitmapFactory.decodeStream(new FileInputStream(file.getPath()));
                    mIvLastLiveImage.setImageBitmap(bitmap3);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            byte[] vis;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;//这个参数设置为true才有效，
                FileInputStream in = new FileInputStream(file);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int i = 0;
                byte[] b = new byte[1024];
                while ((i = in.read(b)) != -1) {
                    out.write(b, 0, i);
                }
                vis = out.toByteArray();
                in.close();
                out.close();
                CWLiveImage image = new CWLiveImage();
                image.setData(vis);
                image.setFormat(6);
                image.setWidth(options.outWidth);
                image.setHeight(options.outHeight);
                image.setTimestamp(System.currentTimeMillis());
                image.setData_length(vis.length);
                boolean result = CWEngine.getInstance().cwSingleLiveDetect(image, 0);
                if (!result) {
                    showToast("获取图片特征失败");
                    isCompare = false;
                } else {
                    //适配单线程时序
                    if (CacheManager.getInstance().loadIsMultiThread(true)) {
                        synchronized (pushObj) {
                            try {
                                long tmp = System.currentTimeMillis();
                                pushObj.wait(compareFaceDetictTimeout);
                                if (System.currentTimeMillis() - tmp >= compareFaceDetictTimeout) {
                                    showToast("无匹配人脸");
                                    isCompare = false;
                                    Logger.e(TAG, "time out");
                                }
                            } catch (Exception e) {
                                isCompare = false;
                                Logger.e(TAG, "wait fail");
                                showToast("比对失败");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                showToast("比对失败");
                isCompare = false;
                e.printStackTrace();
            }
        }
    };

    public Runnable compareRunable = new Runnable() {
        @Override
        public void run() {
            isCompare = true;
            Logger.i("loadLib", "featureLibCount: " + DBManager.getInstance().getPersonFeatureModelDao().count());
            if (featureModels.size() < 1 || featureModels.size() != DBManager.getInstance().getPersonFeatureModelDao().count()) {
                featureModels = DBManager.getInstance().getPersonFeatureModelDao().loadAll();
                if (featureModels.size() < 1) {
                    showToast("当前特征库没有数据");
                    isCompare = false;
                    return;
                }
            }
            final CWLiveFaceRecogInfo currentInfo = CWEngine.getInstance().cwGetFaceFeature(lastLiveImage, lastLiveRect, 1, CWEngine.getInstance().cwGetFeatureLength());
            if (currentInfo == null) {
                showToast("获取当前图片特征失败");
                isCompare = false;
                return;
            }
            int len = 0;
            for (PersonFeatureModel model : featureModels) {
                len += model.getFeature_data().length;
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(len);
            byteBuffer.position(0);
            for (PersonFeatureModel model : featureModels) {
                byteBuffer.put(model.getFeature_data());
            }
            byteBuffer.position(0);
            final byte[] b1 = new byte[len];
            byteBuffer.get(b1);
            final float[] score = CWEngine.getInstance().cwComputeSimilarity(currentInfo.getFeature_data(), 1, b1, featureModels.size(), CWEngine.getInstance().cwGetFeatureLength());
            float minScore = CacheManager.getInstance().loadFeatureThreshold(0.8f);
            int index = -1;
            for (int i = 0; i < score.length; i++) {
                float f = score[i];
                if (f >= minScore) {
                    if (index >= 0 && f < score[index]) {
                        continue;
                    }
                    index = i;
                }
            }
            if (index != -1) {
                final PersonFeatureModel m = featureModels.get(index);
                if (m.getPath() != null) {
                    final int finalIndex = index;
                    runOnUiThread(() -> {
                        mTvCompareResult.setText("分值:" + String.format("%.1f", score[finalIndex] * 100));
                        mTvCompareName.setText(m.getName());
                        try {
                            if (bitmap4 != null && !bitmap4.isRecycled()) {
                                mIvCompareImage.setImageBitmap(null);
                                bitmap4.recycle();
                                bitmap4 = null;
                            }
                            FileInputStream fis = new FileInputStream(m.getPath());
                            bitmap4 = BitmapFactory.decodeStream(fis);
                            mIvCompareImage.setImageBitmap(bitmap4);
                        } catch (Exception e) {
                            showToast("特征库读取图片失败");
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                showToast("无匹配人脸");
            }
            isCompare = false;
        }
    };

    private void showToast(final String msg) {
        runOnUiThread(() -> {
            if (!isFinishing()) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 判断是否为乱码
     *
     * @param strName
     * @return
     */
    public boolean isMessyCode(String strName) {
        try {
            if (!isNumber(strName)) {
                Log.e("xjk unicode error  ", strName);
                return true;
            }
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();

            int length = (ch != null) ? ch.length : 0;
            for (int i = 0; i < length; i++) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + ch[i];
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isNumber(String str) {
        return str.matches("[0-9]+");
    }
}

