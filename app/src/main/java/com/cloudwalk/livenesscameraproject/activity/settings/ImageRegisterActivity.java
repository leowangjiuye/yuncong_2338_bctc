package com.cloudwalk.livenesscameraproject.activity.settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.db.DBManager;
import com.cloudwalk.livenesscameraproject.db.PersonFeatureModel;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.utils.FileUtil;
import com.cloudwalk.livenesscameraproject.utils.ImgUtil;
import com.cloudwalk.livenesscameraproject.utils.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceDetectInfo;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceLivenessInfo;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceParam;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceRecogInfo;
import cn.cloudwalk.component.liveness.entity.face.CWLiveImage;
import cn.cloudwalk.midware.engine.CWEngine;
import cn.cloudwalk.midware.engine.callback.CWLiveinfoCallback;

public class ImageRegisterActivity extends Activity implements View.OnClickListener, CWLiveinfoCallback {

    private String selectedFilePath;
    private TextView loadText, fileInfoText;
    private ProgressBar registerProgress;
    private boolean isPushing = false;
    private final Object object = new Object();


    int okCount = 0;

    private String TAG = "ImageRegisterAct";

    private File lastPushFile;

    //注册时间和
    private long registerAllTime;

    //获取人脸特征时间和
    private long getFaceFeatureAllTime;

    //人脸回调时间和
    private long faceDetectAllTime;

    //获取到人脸特征次数
    private int getFaceFeatureCount = 0;

    //人脸回调次数
    private int faceDetectCount = 0;

    //注册统计开关
    private boolean isStatisticEnable = true;

    private long faceDetectTimeout = 5000;

    //失败文件路径
    private List<String> failList = new ArrayList<>();
    private RegisterAsyncTask registerAsyncTask;

    private static class FaceCallBackModel {
        int errorCode;
        long timestamp;
        CWLiveImage image;
        ArrayList<CWLiveFaceDetectInfo> faceDetectInfomationList;
    }

    private static class RegisterAsyncTask extends AsyncTask {

        private String TAG = "ImageRegisterAsyncTask";
        private final WeakReference<ImageRegisterActivity> weakActivity;

        private RegisterAsyncTask(WeakReference<ImageRegisterActivity> weakActivity) {
            this.weakActivity = weakActivity;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (isCancelled()) {   //如果被中断，则return
                Log.d("AsyncTask", "cancel");
                return false;
            }
            if (objects == null || objects.length < 1 || objects[0] == null || !(objects[0] instanceof FaceCallBackModel)) {
                return false;
            }
            ImageRegisterActivity activity = weakActivity.get();
            FaceCallBackModel faceCallBackModel = (FaceCallBackModel) objects[0];
            Logger.e(TAG, "onFaceDetectCallback");
            if (faceCallBackModel.image == null
                    || faceCallBackModel.faceDetectInfomationList == null
                    || faceCallBackModel.faceDetectInfomationList.size() < 1
                    || faceCallBackModel.faceDetectInfomationList.get(0) == null
                    || faceCallBackModel.faceDetectInfomationList.get(0).getRect() == null) {
                if (!activity.isFinishing() && activity.failList != null) {
                    activity.failList.add(activity.lastPushFile.getPath());
                }
                synchronized (activity.object) {
                    activity.object.notify();
                }
                return false;
            }
            CWLiveFaceDetectInfo info = faceCallBackModel.faceDetectInfomationList.get(0);
            Logger.e(TAG, "onFaceDetectCallback  errorCode: " + faceCallBackModel.errorCode + "face code: " + info.getCode());
            long timestamp_feature = System.currentTimeMillis();
            CWLiveFaceRecogInfo liveFaceRecogInfo = CWEngine.getInstance().cwGetFaceFeature(faceCallBackModel.image, info.getRect(), 1, CWEngine.getInstance().cwGetFeatureLength());
            activity.getFaceFeatureAllTime += (System.currentTimeMillis() - timestamp_feature);
            Logger.e("wlc", "人脸特征耗时：" + (System.currentTimeMillis() - timestamp_feature));
            activity.getFaceFeatureCount++;
            Logger.e(TAG, "cwGetFaceFeature  time: " + (System.currentTimeMillis() - timestamp_feature));
            if (liveFaceRecogInfo != null && liveFaceRecogInfo.getFeature_data() != null) {
                PersonFeatureModel model = new PersonFeatureModel();
                model.setName(activity.lastPushFile != null ? activity.lastPushFile.getName() : new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
                model.setFeature_data(liveFaceRecogInfo.getFeature_data());
                try {
                    Bitmap bitmap = ImgUtil.byteArrayBGRToBitmap(faceCallBackModel.image.getData(), faceCallBackModel.image.getWidth(), faceCallBackModel.image.getHeight());
                    String filePath = "/sdcard/cloudwalk/feature/";
                    cn.cloudwalk.midware.engine.utils.FileUtil.isFolderExists(filePath);
                    File file2 = new File(filePath + System.currentTimeMillis() + ".jpg");
                    FileOutputStream out = new FileOutputStream(file2);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)) {
                        out.flush();
                        out.close();
                    }
                    model.setPath(file2.getPath());
                    long t = System.currentTimeMillis();
                    DBManager.getInstance().getPersonFeatureModelDao().insertOrReplaceInTx(model);
                    Logger.e(TAG, "save to lib  time: " + (System.currentTimeMillis() - t));
                    activity.okCount++;
                    synchronized (activity.object) {
                        activity.object.notify();
                    }
                    return true;
                } catch (Exception e) {
                    activity.showToast("存储特征图片失败");
                }
            }
            activity.failList.add(activity.lastPushFile.getPath());
            synchronized (activity.object) {
                activity.object.notify();
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_register_layout);
        loadText = findViewById(R.id.loadText);
        fileInfoText = findViewById(R.id.fileInfoText);
        registerProgress = findViewById(R.id.registerProgress);
        findViewById(R.id.btnSelector).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
    }

    private List<File> fileList = new ArrayList<>();

    private void startRegister() {
        if (isPushing) {
            showToast("正在处理中");
            return;
        }
        okCount = 0;
        failList.clear();
        updateProgress();
        if (TextUtils.isEmpty(selectedFilePath)) {
            Toast.makeText(this, "请选择文件", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(selectedFilePath);
        if (!file.exists()) {
            Toast.makeText(this, "选择的文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        fileList.clear();
        if (file.isDirectory()) {
            List<File> files = Arrays.asList(file.listFiles());
            for (File f : files) {
                if ((f.getName().contains("png")
                        || f.getName().contains("jpg")
                        || f.getName().contains("jpeg"))) {
                    fileList.add(f);
                }
            }
        } else {
            if ((file.getName().contains("png")
                    || file.getName().contains("jpg")
                    || file.getName().contains("jpeg"))) {
                fileList.add(file);
            } else {
                Toast.makeText(this, "暂只支持jpg/png格式文件", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (fileList.size() > 0) {
            startPushImage();
        } else {
            Toast.makeText(this, "未选择图片文件", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable pushImageRunable = new Runnable() {
        @Override
        public void run() {
            if (isPushing || fileList == null) {
                return;
            }
            isPushing = true;
            final List<File> files = new ArrayList<>(fileList);
            for (File f : files) {
                try {
                    byte[] vis;
                    lastPushFile = f;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;//这个参数设置为true才有效，
                    Bitmap bmp = BitmapFactory.decodeFile(f.getPath(), options);//这里的bitmap是个空
                    FileInputStream in = new FileInputStream(f);
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
                        Logger.e("ImageRegisterActivity", "fail");
                        failList.add(lastPushFile.getPath());
                    } else {
                        //适配单线程时序
                        if (CacheManager.getInstance().loadIsMultiThread(true)) {
                            synchronized (object) {
                                try {
                                    long tmp = System.currentTimeMillis();
                                    object.wait(faceDetectTimeout);
                                    if (System.currentTimeMillis() - tmp >= faceDetectTimeout) {
                                        Logger.e(TAG, "time out: " + (System.currentTimeMillis() - tmp));
                                        failList.add(lastPushFile.getPath());
                                    }
                                } catch (Exception e) {
                                    failList.add(lastPushFile.getPath());
                                    Logger.e(TAG, "object.wait fail");
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    failList.add(lastPushFile.getPath());
                    e.printStackTrace();
                }
                updateProgress();
            }
            saveStatistics();
            isPushing = false;
        }
    };

    private void saveStatistics() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String time = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date());
                String path = "/sdcard/cloudwalk/批量注册统计/" + time + "/";
                FileUtil.isFolderExists(path);
                if (isStatisticEnable && okCount != 0) {
                    registerAllTime = (long) (((double) (System.currentTimeMillis() - registerAllTime)) / fileList.size());
                    getFaceFeatureAllTime = (long) (((double) getFaceFeatureAllTime) / getFaceFeatureCount);
                    faceDetectAllTime = (long) (((double) faceDetectAllTime) / faceDetectCount);
                    String statisticsPath = path + "时间统计.txt";
                    FileUtil.writeStringToFile(time, statisticsPath);
                    FileUtil.writeStringToFile("\n总图片个数: " + fileList.size() + "\n成功数: " + okCount + "\n失败数: " + failList.size() + "\n注册总平均时长: " + registerAllTime + "\n获取人脸特征平均时长: " + getFaceFeatureAllTime + "\n人脸检测回调平均时长: " + faceDetectAllTime + "\n\n", statisticsPath);
                    Logger.e(TAG, "平均注册时长：" + registerAllTime);
                }
                if (failList != null && failList.size() > 0) {
                    String failePath = path + "失败统计.txt";
                    StringBuilder failFileStr = new StringBuilder("失败文件路径:");
                    for (String p : failList) {
                        failFileStr.append("\n").append(p);
                    }
                    FileUtil.writeStringToFile(time, failePath);
                    FileUtil.writeStringToFile(failFileStr.toString(), failePath);
                }
            }
        });
    }

    private void startPushImage() {
        if (isPushing) {
            showToast("正在处理中");
            return;
        }
        okCount = 0;
        if (failList == null) {
            failList = new ArrayList<>();
        } else {
            failList.clear();
        }
        updateProgress();
        CWEngine.getInstance().cwSetLiveInfoCallback(this);
        Logger.e("pushImage", "fileSize: " + fileList.size() + "");
        registerAllTime = System.currentTimeMillis();
        getFaceFeatureAllTime = 0;
        getFaceFeatureCount = 0;
        faceDetectAllTime = 0;
        faceDetectCount = 0;
        lastPushFile = null;
        new Thread(pushImageRunable).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CWLiveFaceParam param = CWEngine.getInstance().cwGetDetectorParams();
        if (param != null) {
            param.setMin_face_size(30);
            param.setMax_face_size(1500);
            param.setRoi_x(0);
            param.setRoi_width(0);
            param.setRoi_y(0);
            param.setRoi_height(0);
            param.setOpen_quality(0);
            param.setOpen_liveness(0);
            param.setMax_face_num_perImg(1);
            CWEngine.getInstance().cwSetDetectorParams(param);
        }else {
            showToast("获取活体检测参数异常");
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateProgress();
        CWLiveFaceParam param = CWEngine.getInstance().cwGetDetectorParams();
        if (param != null) {
            param.setMin_face_size(CacheManager.getInstance().loadMinFace(80));
            param.setMax_face_size(CacheManager.getInstance().loadMaxFace(400));
            param.setRoi_x(CacheManager.getInstance().loadRoiX(0));
            param.setRoi_width(CacheManager.getInstance().loadRoiWidth(0));
            param.setRoi_y(CacheManager.getInstance().loadRoiY(0));
            param.setRoi_height(CacheManager.getInstance().loadRoiHeight(0));
            param.setOpen_quality(CacheManager.getInstance().loadIsOpenQuality(1));
            param.setOpen_liveness(CacheManager.getInstance().loadIsOpenLiveness(1));
            param.setMax_face_num_perImg(CacheManager.getInstance().loadMaxFaceNum(1));
            CWEngine.getInstance().cwSetDetectorParams(param);
        }

        if (registerAsyncTask != null && registerAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {  //author  中断asyntask 后台操作
            registerAsyncTask.cancel(true);

            Log.d("AsyncTask", "cancel");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSelector) {
            if (isPushing) {
                showToast("正在处理中");
                return;
            }
            selectedDir();
        } else if (id == R.id.btnStart) {
            if (isPushing) {
                showToast("正在处理中");
                return;
            }
            startRegister();
        }
    }

    private void selectedDir() {
        Intent intent = new Intent(this, SelectFolderActivity.class);
        intent.putExtra("SP_KEY", "selectedFilePath");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                selectedFilePath = data.getStringExtra("path");
                fileInfoText.setText(selectedFilePath);
            }
        }
    }

    @Override
    public void onFaceDetectCallback(final int errorCode, long timestamp, final CWLiveImage image, final ArrayList<CWLiveFaceDetectInfo> faceDetectInfomationList) {
        long tmp = timestamp;
        if (tmp == 0 && image.getTimestamp() != 0) {
            tmp = image.getTimestamp();
        }
        long faceDetectTmp = System.currentTimeMillis() - tmp;
        if (faceDetectTmp >= faceDetectTimeout) {
            return;
        }
        if (image == null
                || faceDetectInfomationList == null
                || faceDetectInfomationList.size() < 1
                || faceDetectInfomationList.get(0) == null
                || faceDetectInfomationList.get(0).getRect() == null) {
            if (failList != null) {
                failList.add(lastPushFile.getPath());
            }
            synchronized (object) {
                object.notify();
            }
            return;
        }
        Logger.e("wlc", "人脸检测耗时：" + faceDetectTmp);
        faceDetectAllTime += faceDetectTmp;
        faceDetectCount++;
        FaceCallBackModel model = new FaceCallBackModel();
        model.image = image;
        model.errorCode = errorCode;
        model.faceDetectInfomationList = faceDetectInfomationList;
        model.timestamp = timestamp;
        registerAsyncTask = new RegisterAsyncTask(new WeakReference<ImageRegisterActivity>(this));
        registerAsyncTask.execute(model);
    }

    private void updateProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadText.setText(String.format("成功:%d,失败:%d,总数:%d", okCount, failList.size(), fileList.size()));
                int progress = (int) ((okCount + failList.size()) / ((double) (fileList.size())) * 100);
                registerProgress.setProgress(progress);
            }
        });
    }

    @Override
    public void onLivenessCallback(int errorCode, long timestamp, CWLiveImage image, CWLiveImage irImage, CWLiveImage depthImage, ArrayList<CWLiveFaceDetectInfo> faceDetectInfoList, ArrayList<CWLiveFaceLivenessInfo> faceLivenessList, CWLiveImage bestface) {
        Logger.e("ImageRegisterActivity", "onLivenessCallback");
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ImageRegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
