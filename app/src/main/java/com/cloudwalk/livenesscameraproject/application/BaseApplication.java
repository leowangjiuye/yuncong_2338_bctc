package com.cloudwalk.livenesscameraproject.application;

import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_MINMAX_ERR;
import static cn.cloudwalk.midware.live.user.CWLiveness.CW_FACE_ROI_ERR;

import android.app.Application;

import android.os.Handler;
import android.widget.Toast;

import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceParam;
import cn.cloudwalk.midware.engine.CWGeneralApi;
import cn.cloudwalk.midware.engine.callback.CWErrorCallback;

import com.cloudwalk.livenesscameraproject.db.DBManager;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.utils.Logger;

public class BaseApplication extends Application {

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        t = System.currentTimeMillis();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
//        CrashReport.initCrashReport(getApplicationContext(), "78ec76cd20", true);
        DBManager.initDataBase(this);
        CWGeneralApi.getInstance().cwSetErrorCallback(new CWErrorCallback() {
            @Override
            public void cwOnError(int i, String s) {
                String tip = s;
                if (i == CWGeneralApi.CW_CAMERA_OPEN_FAIL) {

                } else if (i == CW_FACE_ROI_ERR) {
                    tip = "设置ROI参数失败";
                    CWLiveFaceParam cwLiveFaceParam = CWGeneralApi.getInstance().cwGetDetectorParams();
                    int roix = cwLiveFaceParam.getRoi_x();
                    int roiy = cwLiveFaceParam.getRoi_y();
                    int roiWidth = cwLiveFaceParam.getRoi_width();
                    int roiHeight = cwLiveFaceParam.getRoi_height();
                    CacheManager.getInstance().saveRoiX(roix);
                    CacheManager.getInstance().saveRoiY(roiy);
                    CacheManager.getInstance().saveRoiWidth(roiWidth);
                    CacheManager.getInstance().saveRoiHeight(roiHeight);
                } else if (i == CW_FACE_MINMAX_ERR) {
                    tip = "设置最大最小人脸失败";
//                    showToast(tip);
                }
                Logger.e("-----cwSetError", "error: " + i + "  tip: " + tip);
            }
        });
    }

    private long t;
    private void showToast(final String tip) {
        if (System.currentTimeMillis() - t > 2000) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BaseApplication.this, tip, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
