package com.cloudwalk.livenesscameraproject.activity.settings;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.launch.MainActivity;
import com.cloudwalk.livenesscameraproject.adapter.TextAdapter;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.utils.FileUtil;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.view.SlideButton;

import java.util.ArrayList;

import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceBeautyParam;
import cn.cloudwalk.component.liveness.entity.face.CWLiveFaceParam;
import cn.cloudwalk.midware.camera.entity.CWCameraInfos;
import cn.cloudwalk.midware.engine.CWCameraConfig;
import cn.cloudwalk.midware.engine.CWEngine;
import cn.cloudwalk.midware.engine.CWGeneralApi;
import cn.cloudwalk.midware.engine.CWLivenessConfig;
import cn.cloudwalk.midware.engine.CWPreivewConfig;
import cn.cloudwalk.midware.engine.utils.Logger;

public class LivenessParamSettingsActivity extends BaseActivity {

    private static final String TAG = "LivenessSetActivity";
    private EditText mEtROIX, mEtROIY, mEtROIWidth, mEtROIHeight, mEtMinFace, mEtMaxFace,
            mEtMaxFaceMount, mEtPerformLevel, mEtPitchMax, mEtPitchMin, mEtYawMax, mEtYawMin,
            mEtRollMax, mEtRollMin, mEtClarity, mEtSkin, mEtConfidence, mEtOpenEye, mEtOpenMouth,
            mEtOcclusion, mEtBrightness, mEtdarkness, mEtBlackspec, mEtSunglass, mEtProceduremask,
            mEtFaceBeautyLift, mEtFaceBeautySmooth, mEtFaceBeautyWhiten;
    private SlideButton mSlideBtnIsCompare, mSlideBtnIsOpenLiveness, mSlideBtnIsOpenQuality,
            mSlideBtnDefaultFace, mSlideBtnFaceBeauty;
    private Spinner mBeautySpinner;
    private TextView mBtnSave;
    private static final String BESTFACE_PATH = "/sdcard/cloudwalk/最佳人脸/";
    private static final String ROOT_PATH = "/sdcard/cloudwalk/";
    private ArrayList<CWCameraInfos> cameraInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveness_para_set);
        initView();
//        initConfig();
        getParams();

    }

    private void initConfig() {
        //  mEtRgbPid.setText(SPUtils.getInstance().getValue("rgbPid", 0xC053) + "");
        //            mEtRgbVid.setText(SPUtils.getInstance().getValue("rgbVid", 0x0C45) + "");
        //            mEtIrPid.setText(SPUtils.getInstance().getValue("irPid", 0xB051) + "");
        //            mEtIrVid.setText(SPUtils.getInstance().getValue("irVid", 0x0C45) + "");
//        List<CWCameraInfo> cameraList = CWEngine.getInstance().cwEnumCameras(this);
//        for (int i = 0; i < cameraList.size(); i++) {
//            Logger.d("cameraInfoList", cameraList.get(i).toString());
//        }
        //创建存图文件夹
        FileUtil.isFolderExists(ROOT_PATH);
        //最佳人脸及全景存图(默认)
        FileUtil.isFolderExists(BESTFACE_PATH);

        CWCameraConfig cwCameraConfig = CWCameraConfig.getInstance();
        cwCameraConfig.setCameraType(1);

        CWCameraInfos cameraInfosVis = new CWCameraInfos();
        cameraInfosVis.setCameraWidth(CacheManager.getInstance().loadCameraWidth(640));
        cameraInfosVis.setCameraHeight(CacheManager.getInstance().loadCameraHeight(480));
        cameraInfosVis.setType(0);
        Logger.d(TAG, "CacheManager.getInstance().loadRgbPid(\"rgbPid\", 0xC053)-------" + CacheManager.getInstance().loadRgbPid(0xC053));
        Logger.d(TAG, "CacheManager.getInstance().loadRgbVid(\"rgbVid\", 0x0C45)-------" + CacheManager.getInstance().loadRgbVid(0x0C45));
        cameraInfosVis.setPid(CacheManager.getInstance().loadRgbPid(0xC053));//0xB051
        cameraInfosVis.setVid(CacheManager.getInstance().loadRgbVid(0x0C45));
        cameraInfoList.add(cameraInfosVis);

        CWCameraInfos cameraInfosNis1 = new CWCameraInfos();
        cameraInfosNis1.setCameraWidth(CacheManager.getInstance().loadCameraWidth(640));
        cameraInfosNis1.setCameraHeight(CacheManager.getInstance().loadCameraHeight(480));
        cameraInfosNis1.setType(1);
        Logger.d(TAG, "CacheManager.getInstance().loadIrPid(\"irPid\", 0xB051)-------" + CacheManager.getInstance().loadIrPid(0xB051));
        Logger.d(TAG, "CacheManager.getInstance().loadIrVid(\"irVid\", 0x0C45)-------" + CacheManager.getInstance().loadIrVid(0x0C45));
        cameraInfosNis1.setPid(CacheManager.getInstance().loadIrPid(0xB051));//0xC053
        cameraInfosNis1.setVid(CacheManager.getInstance().loadIrVid(0x0C45));
        cameraInfoList.add(cameraInfosNis1);


        CWPreivewConfig cwVisPreivewConfig = CWPreivewConfig.getInstance();
        cwVisPreivewConfig.setAngle(CacheManager.getInstance().loadRotation(90));
        cwVisPreivewConfig.setMirror(CacheManager.getInstance().loadMirror(true));
        cwVisPreivewConfig.setSurfaceView(null);
        cwCameraConfig.setCameraInfos(cameraInfoList);

        CWLivenessConfig cwLivenessConfig = CWLivenessConfig.getInstance();
        /* 添加背景图和存图路径 */
        Logger.d(TAG, "isSaveDebugImage=" + CacheManager.getInstance().loadIsSaveDebugImage(false));
        cwLivenessConfig.setDefendSave(CacheManager.getInstance().loadIsSaveDebugImage(false));
        cwLivenessConfig.setDefendFile(TextUtils.isEmpty(CacheManager.getInstance().loadDebugImagePath("")) ?
                null : CacheManager.getInstance().loadDebugImagePath(""));
        cwLivenessConfig.setConfigFile(TextUtils.isEmpty(CacheManager.getInstance().loadConfigFilePath("")) ?
                null : CacheManager.getInstance().loadConfigFilePath(""));
        cwLivenessConfig.setFaceDetectFile(TextUtils.isEmpty(CacheManager.getInstance().loadFaceDetModelPath("")) ?
                null : CacheManager.getInstance().loadFaceDetModelPath(""));
        cwLivenessConfig.setFaceKeyPointDetectFile(TextUtils.isEmpty(CacheManager.getInstance().loadKeyFacePointModelPath("")) ?
                null : CacheManager.getInstance().loadKeyFacePointModelPath(""));
        cwLivenessConfig.setFaceQualityFile(TextUtils.isEmpty(CacheManager.getInstance().loadFaceQualityFile("")) ?
                null : CacheManager.getInstance().loadFaceQualityFile(""));
//        Logger.d(TAG, SPUtils.getInstance().getValue("faceRecogFile", ""));
        cwLivenessConfig.setFaceRecogFile(TextUtils.isEmpty(CacheManager.getInstance().loadFaceRecogFile("")) ?
                null : CacheManager.getInstance().loadFaceRecogFile(""));
//        Logger.d(TAG, cwLivenessConfig.getFaceLivenessFile());

        cwLivenessConfig.setFaceLivenessFile(TextUtils.isEmpty(CacheManager.getInstance().loadFaceLivenessFile("")) ?
                null : CacheManager.getInstance().loadFaceLivenessFile(""));
        cwLivenessConfig.setModelMode(0);
        cwLivenessConfig.setLivenessMode(1);
        cwLivenessConfig.setMultiThread(CacheManager.getInstance().loadIsMultiThread(true));


        cwLivenessConfig.setAvalableSpace(200);

        cwLivenessConfig.setTrackPreviewHeight(CacheManager.getInstance().loadTrackPreviewHeight(640));
        cwLivenessConfig.setTrackPreviewWidth(CacheManager.getInstance().loadTrackPreviewWidth(480));
        CWEngine.getInstance().cwInit(this, cwCameraConfig, cwLivenessConfig, cwVisPreivewConfig);

//        //打开是否进行人脸追踪
        CWEngine.getInstance().cwEnableFaceTrack(CacheManager.getInstance().loadIsOpenFaceTrack(false));
    }

    private void initView() {
        ImageView imageView = findViewById(R.id.iv_liveness_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mEtROIX = findViewById(R.id.et_roi_x);
        mEtROIY = findViewById(R.id.et_roi_y);
        mEtROIWidth = findViewById(R.id.et_roi_width);
        mEtROIHeight = findViewById(R.id.et_roi_height);

        mEtMinFace = findViewById(R.id.et_min_face);
        mEtMaxFace = findViewById(R.id.et_max_face);
        mEtMaxFaceMount = findViewById(R.id.et_max_face_mount);
        mEtPerformLevel = findViewById(R.id.et_detect_performance_level);

        mSlideBtnDefaultFace = findViewById(R.id.sbtn_default_face);
        mSlideBtnIsCompare = findViewById(R.id.sbtn_is_compare);
        mSlideBtnIsOpenLiveness = findViewById(R.id.sbtn_is_open_liveness);
        mSlideBtnIsOpenQuality = findViewById(R.id.sbtn_is_open_quality_detect);
        mSlideBtnFaceBeauty = findViewById(R.id.sbtn_is_open_face_beauty);

        mEtPitchMax = findViewById(R.id.et_pitch_max);
        mEtPitchMin = findViewById(R.id.et_pitch_min);
        mEtYawMax = findViewById(R.id.et_yaw_max);
        mEtYawMin = findViewById(R.id.et_yaw_min);
        mEtRollMax = findViewById(R.id.et_roll_max);
        mEtRollMin = findViewById(R.id.et_roll_min);

        mEtClarity = findViewById(R.id.et_clarity_threshold);
        mEtSkin = findViewById(R.id.et_skin_threshold);
        mEtConfidence = findViewById(R.id.et_confidence);
        mEtOpenEye = findViewById(R.id.et_open_eye_threshold);
        mEtOpenMouth = findViewById(R.id.et_open_mouth_threshold);
        mEtOcclusion = findViewById(R.id.et_occlusion_threshold);
        mEtBrightness = findViewById(R.id.et_brightness_threshold);
        mEtdarkness = findViewById(R.id.et_darkness_threshold);
        mEtBlackspec = findViewById(R.id.et_blackspec);
        mEtSunglass = findViewById(R.id.et_sunglass);
        mEtProceduremask = findViewById(R.id.et_proceduremask);
        mBeautySpinner = findViewById(R.id.sp_beauty_op);
        // 美颜选项，1：瘦脸，2：磨皮，3:瘦脸+磨皮， 4：美白，5：瘦脸+美白，6：磨皮+美白，7：瘦脸+磨皮+美白，8：瘦脸 + 大眼功能，耗时略微增加，
        // 16：更好的美白效果，但是耗时会有不小的增加
        String[] beautyOpStrs = {"瘦脸", "磨皮", "瘦脸+磨皮", "美白", "瘦脸+美白", "磨皮+美白", "瘦脸+磨皮+美白", "瘦脸+大眼(耗时增加)", "更好的美白(耗时高)"};
        TextAdapter beautyAdapter = new TextAdapter(beautyOpStrs, LivenessParamSettingsActivity.this);
        mBeautySpinner.setAdapter(beautyAdapter);
        mEtFaceBeautyLift = findViewById(R.id.et_face_lift);
        mEtFaceBeautySmooth = findViewById(R.id.et_smooth);
        mEtFaceBeautyWhiten = findViewById(R.id.et_whiten);

        mBtnSave = findViewById(R.id.tv_liveness_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                  auther: xujunke   修改roi默认值问题，先过中间件接口，如果roi设置失败则不保存mmkv值
                 */
                try {
                    CWLiveFaceParam cwLiveFaceParam = CWEngine.getInstance().cwGetDetectorParams();
                    if (cwLiveFaceParam == null) {
                        Toast.makeText(LivenessParamSettingsActivity.this, "活体参数设置失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cwLiveFaceParam.setRoi_x(Integer.parseInt(mEtROIX.getText() + ""));
                    cwLiveFaceParam.setRoi_y(Integer.parseInt(mEtROIY.getText() + ""));
                    cwLiveFaceParam.setRoi_width(Integer.parseInt(mEtROIWidth.getText() + ""));
                    cwLiveFaceParam.setRoi_height(Integer.parseInt(mEtROIHeight.getText() + ""));
                    int minFaceSize = Integer.parseInt(mEtMinFace.getText() + "");
                    int maxFaceSize = Integer.parseInt(mEtMaxFace.getText() + "");
                    int defaultFaceSizeEnable = mSlideBtnDefaultFace.isOpen() ? 1 : 0;
                    int height = CacheManager.getInstance().loadCameraHeight(480);
                    if (defaultFaceSizeEnable == 1 && height > 0) {
                        float ration = height / 480f;
                        minFaceSize = (int) (80 * ration);
                        maxFaceSize = (int) (400 * ration);
                    }
                    cwLiveFaceParam.setMin_face_size(minFaceSize);
                    cwLiveFaceParam.setMax_face_size(maxFaceSize);
                    cwLiveFaceParam.setMax_face_num_perImg(Integer.parseInt(mEtMaxFaceMount.getText() + ""));
                    cwLiveFaceParam.setPerfmonLevel(Integer.parseInt(mEtPerformLevel.getText() + ""));
                    cwLiveFaceParam.setNir_face_compare(mSlideBtnIsCompare.isOpen() ? 1 : 0);
                    cwLiveFaceParam.setOpen_liveness(mSlideBtnIsOpenLiveness.isOpen() ? 1 : 0);
                    cwLiveFaceParam.setOpen_quality(mSlideBtnIsOpenQuality.isOpen() ? 1 : 0);
                    cwLiveFaceParam.setPitch_max(Float.valueOf(mEtPitchMax.getText() + ""));
                    cwLiveFaceParam.setPitch_min(Float.valueOf(mEtPitchMin.getText() + ""));
                    cwLiveFaceParam.setYaw_max(Float.valueOf(mEtYawMax.getText() + ""));
                    cwLiveFaceParam.setYaw_min(Float.valueOf(mEtYawMin.getText() + ""));
                    cwLiveFaceParam.setRoll_max(Float.valueOf(mEtRollMax.getText() + ""));
                    cwLiveFaceParam.setRoll_min(Float.valueOf(mEtRollMin.getText() + ""));
                    cwLiveFaceParam.setClarity(Float.valueOf(mEtClarity.getText() + ""));
                    cwLiveFaceParam.setSkinscore(Float.valueOf(mEtSkin.getText() + ""));
                    cwLiveFaceParam.setConfidence(Float.valueOf(mEtConfidence.getText() + ""));
                    cwLiveFaceParam.setEyeopen(Float.valueOf(mEtOpenEye.getText() + ""));
                    cwLiveFaceParam.setMouthopen(Float.valueOf(mEtOpenMouth.getText() + ""));
                    cwLiveFaceParam.setOcclusion(Float.valueOf(mEtOcclusion.getText() + ""));
                    cwLiveFaceParam.setHight_brightness_threshold(Float.valueOf(mEtBrightness.getText() + ""));
                    cwLiveFaceParam.setLow_brightness_threshold(Float.valueOf(mEtdarkness.getText() + ""));
                    cwLiveFaceParam.setBlackspec(Float.valueOf(mEtBlackspec.getText() + ""));
                    cwLiveFaceParam.setSunglass(Float.valueOf(mEtSunglass.getText() + ""));
                    cwLiveFaceParam.setProceduremask(Float.valueOf(mEtProceduremask.getText() + ""));
                    cwLiveFaceParam.setOpen_face_beauty(mSlideBtnFaceBeauty.isOpen() ? 1 : 0);

                    int beautySelIndex = mBeautySpinner.getSelectedItemPosition();
                    int beautyOp = beautySelIndex == 8 ? 16 : (beautySelIndex + 1);
                    CWLiveFaceBeautyParam beautyParam = cwLiveFaceParam.getFace_beauty_param();
                    if (beautyParam == null) {
                        beautyParam = new CWLiveFaceBeautyParam();
                    }
                    beautyParam.setBeauty_op(beautyOp);
                    beautyParam.setFace_lift_intensity(Float.valueOf(mEtFaceBeautyLift.getText() + ""));
                    beautyParam.setSmooth_intensity(Float.valueOf(mEtFaceBeautySmooth.getText() + ""));
                    beautyParam.setWhiten_intensity(Float.valueOf(mEtFaceBeautySmooth.getText() + ""));
                    cwLiveFaceParam.setFace_beauty_param(beautyParam);

                    Logger.d("ANR_cwSetDetectorParams", "start");
                    boolean result = CWEngine.getInstance().cwSetDetectorParams(cwLiveFaceParam);
                    Logger.d("ANR_cwSetDetectorParams", "end");
                    if (result) {
                        Toast.makeText(LivenessParamSettingsActivity.this, "活体参数设置成功！", Toast.LENGTH_SHORT).show();
                        CacheManager.getInstance().saveRoiX(Integer.parseInt(mEtROIX.getText() + ""));
                        CacheManager.getInstance().saveRoiY(Integer.parseInt(mEtROIY.getText() + ""));
                        CacheManager.getInstance().saveRoiWidth(Integer.parseInt(mEtROIWidth.getText() + ""));
                        CacheManager.getInstance().saveRoiHeight(Integer.parseInt(mEtROIHeight.getText() + ""));
                        CacheManager.getInstance().saveMinFace(minFaceSize);
                        CacheManager.getInstance().saveMaxFace(maxFaceSize);
                        CacheManager.getInstance().saveDefaultFace(defaultFaceSizeEnable);
                        CacheManager.getInstance().saveMaxFaceNum(Integer.parseInt(mEtMaxFaceMount.getText() + ""));
                        CacheManager.getInstance().savePerformLevel(Integer.parseInt(mEtPerformLevel.getText() + ""));
                        CacheManager.getInstance().saveIsCompare(mSlideBtnIsCompare.isOpen() ? 1 : 0);
                        CacheManager.getInstance().saveIsOpenLiveness(mSlideBtnIsOpenLiveness.isOpen() ? 1 : 0);
                        CacheManager.getInstance().saveIsOpenQuality(mSlideBtnIsOpenQuality.isOpen() ? 1 : 0);
                        CacheManager.getInstance().savePitchMax(Float.valueOf(mEtPitchMax.getText() + ""));
                        CacheManager.getInstance().savePitchMin(Float.valueOf(mEtPitchMin.getText() + ""));
                        CacheManager.getInstance().saveYawMax(Float.valueOf(mEtYawMax.getText() + ""));
                        CacheManager.getInstance().saveYawMin(Float.valueOf(mEtYawMin.getText() + ""));
                        CacheManager.getInstance().saveRollMax(Float.valueOf(mEtRollMax.getText() + ""));
                        CacheManager.getInstance().saveRollMin(Float.valueOf(mEtRollMin.getText() + ""));
                        CacheManager.getInstance().saveClarity(Float.valueOf(mEtClarity.getText() + ""));
                        CacheManager.getInstance().saveSkin(Float.valueOf(mEtSkin.getText() + ""));
                        CacheManager.getInstance().saveConfidence(Float.valueOf(mEtConfidence.getText() + ""));
                        CacheManager.getInstance().saveEyeOpen(Float.valueOf(mEtOpenEye.getText() + ""));
                        CacheManager.getInstance().saveMouthOpen(Float.valueOf(mEtOpenMouth.getText() + ""));
                        CacheManager.getInstance().saveOcclucsion(Float.valueOf(mEtOcclusion.getText() + ""));
                        CacheManager.getInstance().saveBrightness(Float.valueOf(mEtBrightness.getText() + ""));
                        CacheManager.getInstance().saveDarkness(Float.valueOf(mEtdarkness.getText() + ""));
                        CacheManager.getInstance().saveBlackSpec(Float.valueOf(mEtBlackspec.getText() + ""));
                        CacheManager.getInstance().saveSunglass(Float.valueOf(mEtSunglass.getText() + ""));
                        CacheManager.getInstance().saveProceduremask(Float.valueOf(mEtProceduremask.getText() + ""));
                        CacheManager.getInstance().saveOpenFaceBeauty(mSlideBtnFaceBeauty.isOpen() ? 1 : 0);
                        CacheManager.getInstance().saveFaceBeautyOp(beautyOp);
                        CacheManager.getInstance().saveFaceBeautyLift(Float.valueOf(mEtFaceBeautyLift.getText() + ""));
                        CacheManager.getInstance().saveFaceBeautySmooth(Float.valueOf(mEtFaceBeautySmooth.getText() + ""));
                        CacheManager.getInstance().saveFaceBeautyWhiten(Float.valueOf(mEtFaceBeautyWhiten.getText() + ""));
                        finish();
                    } else {
                        Toast.makeText(LivenessParamSettingsActivity.this, "活体参数设置失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(LivenessParamSettingsActivity.this, "输入值格式不符！",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


//                CWLiveFaceParam cwLiveFaceParam = CWEngine.getInstance().cwGetDetectorParams();
//                cwLiveFaceParam.setRoi_x(CacheManager.getInstance().loadRoiX(0));
//                cwLiveFaceParam.setRoi_y(CacheManager.getInstance().loadRoiY(0));
//                cwLiveFaceParam.setRoi_width(CacheManager.getInstance().loadRoiWidth(0));
//                cwLiveFaceParam.setRoi_height(CacheManager.getInstance().loadRoiHeight(0));
//                cwLiveFaceParam.setMin_face_size(CacheManager.getInstance().loadMinFace(0));
//                cwLiveFaceParam.setMax_face_size(CacheManager.getInstance().loadMaxFace(0));
//                cwLiveFaceParam.setMax_face_num_perImg(CacheManager.getInstance().loadMaxFaceNum(0));
//                cwLiveFaceParam.setPerfmonLevel(CacheManager.getInstance().loadPerformLevel(0));
//                cwLiveFaceParam.setNir_face_compare(CacheManager.getInstance().loadIsCompare(1));
//                cwLiveFaceParam.setOpen_liveness(CacheManager.getInstance().loadIsOpenLiveness(1));
//                cwLiveFaceParam.setOpen_quality(CacheManager.getInstance().loadIsOpenQuality(1));
//                cwLiveFaceParam.setPitch_max(CacheManager.getInstance().loadPitchMax(0f));
//                cwLiveFaceParam.setPitch_min(CacheManager.getInstance().loadPitchMin(0f));
//                cwLiveFaceParam.setYaw_max(CacheManager.getInstance().loadYawMax(0f));
//                cwLiveFaceParam.setYaw_min(CacheManager.getInstance().loadYawMin(0f));
//                cwLiveFaceParam.setRoll_max(CacheManager.getInstance().loadRollMax(0f));
//                cwLiveFaceParam.setRoll_min(CacheManager.getInstance().loadRollMin(0f));
//                cwLiveFaceParam.setClarity(CacheManager.getInstance().loadClarity(0f));
//                cwLiveFaceParam.setSkinscore(CacheManager.getInstance().loadSkin(0f));
//                cwLiveFaceParam.setConfidence(CacheManager.getInstance().loadConfidence(0f));
//                cwLiveFaceParam.setEyeopen(CacheManager.getInstance().loadEyeOpen(0f));
//                cwLiveFaceParam.setMouthopen(CacheManager.getInstance().loadMouthOpen(0f));
//                cwLiveFaceParam.setOcclusion(CacheManager.getInstance().loadOcclucsion(0f));
//                cwLiveFaceParam.setHight_brightness_threshold(CacheManager.getInstance().loadBrightness(0f));
//                cwLiveFaceParam.setLow_brightness_threshold(CacheManager.getInstance().loadDarkness(0f));
//                cwLiveFaceParam.setBlackspec(CacheManager.getInstance().loadBlackSpec(0f));
//                cwLiveFaceParam.setSunglass(CacheManager.getInstance().loadSunglass(0f));
//                Logger.d("ANR_cwSetDetectorParams", "start");
//                CWEngine.getInstance().cwSetDetectorParams(cwLiveFaceParam);
//                Logger.d("ANR_cwSetDetectorParams", "end");
//
//
//
//
//
//                finish();
            }
        });
    }

    private void getParams() {
        boolean isInit = CacheManager.getInstance().loadIsInit(true);
        Logger.d(TAG, isInit + "");
        if (isInit) {
            CWLiveFaceParam cwLiveFaceParam = CWEngine.getInstance().cwGetDetectorParams();
            if (cwLiveFaceParam == null) {
                return;
            }
            int roix = cwLiveFaceParam.getRoi_x();
            int roiy = cwLiveFaceParam.getRoi_y();
            int roiWidth = cwLiveFaceParam.getRoi_width();
            int roiHeight = cwLiveFaceParam.getRoi_height();

            int minFace = cwLiveFaceParam.getMin_face_size();
            int maxFace = cwLiveFaceParam.getMax_face_size();
            int defaultFaceSizeEnable = 1;
            int maxFaceNum = cwLiveFaceParam.getMax_face_num_perImg();
            int performLevel = cwLiveFaceParam.getPerfmonLevel();

            int isCompare = cwLiveFaceParam.isNir_face_compare();
            int isOpenLiveness = cwLiveFaceParam.isOpen_liveness();
            int isOpenQuality = cwLiveFaceParam.isOpen_quality();

            float pitchMax = cwLiveFaceParam.getPitch_max();
            float pitchMin = cwLiveFaceParam.getPitch_min();
            float yawMax = cwLiveFaceParam.getYaw_max();
            float yawMin = cwLiveFaceParam.getYaw_min();
            float rollMax = cwLiveFaceParam.getRoll_max();
            float rollMin = cwLiveFaceParam.getRoll_min();

            float clarity = cwLiveFaceParam.getClarity();
            float skin = cwLiveFaceParam.getSkinscore();
            float confidence = cwLiveFaceParam.getConfidence();
            float eyeOpen = cwLiveFaceParam.getEyeopen();
            float mouthOpen = cwLiveFaceParam.getMouthopen();
            float occlucsion = cwLiveFaceParam.getOcclusion();
            float brightness = cwLiveFaceParam.getHight_brightness_threshold();
            float darkness = cwLiveFaceParam.getLow_brightness_threshold();
            float blackSpec = cwLiveFaceParam.getBlackspec();
            float sunglass = cwLiveFaceParam.getSunglass();
            float proceduremask = cwLiveFaceParam.getProceduremask();

            int openFaceBeauty = cwLiveFaceParam.getOpen_face_beauty();
            CWLiveFaceBeautyParam beautyParam = cwLiveFaceParam.getFace_beauty_param();
            if (beautyParam == null) {
                beautyParam = new CWLiveFaceBeautyParam();
            }
            int beautyOp = beautyParam.getBeauty_op();

            mEtROIX.setText(roix + "");
            mEtROIY.setText(roiy + "");
            mEtROIWidth.setText(roiWidth + "");
            mEtROIHeight.setText(roiHeight + "");
            mEtMinFace.setText(minFace + "");
            mEtMaxFace.setText(maxFace + "");
            mSlideBtnDefaultFace.setOpen(defaultFaceSizeEnable == 1 ? true : false);
            mEtMaxFaceMount.setText(maxFaceNum + "");
            mEtPerformLevel.setText(performLevel + "");
            mSlideBtnIsCompare.setOpen(isCompare == 1);
            mSlideBtnIsOpenLiveness.setOpen(isOpenLiveness == 1);
            mSlideBtnIsOpenQuality.setOpen(isOpenQuality == 1);
            mEtPitchMax.setText(pitchMax + "");
            mEtPitchMin.setText(pitchMin + "");
            mEtYawMax.setText(yawMax + "");
            mEtYawMin.setText(yawMin + "");
            mEtRollMax.setText(rollMax + "");
            mEtRollMin.setText(rollMin + "");
            mEtClarity.setText(clarity + "");
            mEtSkin.setText(skin + "");
            mEtConfidence.setText(confidence + "");
            mEtOpenEye.setText(eyeOpen + "");
            mEtOpenMouth.setText(mouthOpen + "");
            mEtOcclusion.setText(occlucsion + "");
            mEtBrightness.setText(brightness + "");
            mEtdarkness.setText(darkness + "");
            mEtBlackspec.setText(blackSpec + "");
            mEtSunglass.setText(sunglass + "");
            mEtProceduremask.setText(proceduremask + "");
            mSlideBtnFaceBeauty.setOpen(openFaceBeauty == 1);
            mBeautySpinner.setSelection(beautyOp == 16 ? 8 : (beautyOp -1));
            mEtFaceBeautyLift.setText(beautyParam.getFace_lift_intensity() + "");
            mEtFaceBeautySmooth.setText(beautyParam.getSmooth_intensity() + "");
            mEtFaceBeautyWhiten.setText(beautyParam.getWhiten_intensity() + "");

            CacheManager.getInstance().saveRoiX(roix);
            CacheManager.getInstance().saveRoiY(roiy);
            CacheManager.getInstance().saveRoiWidth(roiWidth);
            CacheManager.getInstance().saveRoiHeight(roiHeight);
            CacheManager.getInstance().saveMinFace(minFace);
            CacheManager.getInstance().saveMaxFace(maxFace);
            CacheManager.getInstance().saveDefaultFace(defaultFaceSizeEnable);
            CacheManager.getInstance().saveMaxFaceNum(maxFaceNum);
            CacheManager.getInstance().savePerformLevel(performLevel);
            CacheManager.getInstance().saveIsCompare(isCompare);
            CacheManager.getInstance().saveIsOpenLiveness(isOpenLiveness);
            CacheManager.getInstance().saveIsOpenQuality(isOpenQuality);
            CacheManager.getInstance().savePitchMax(pitchMax);
            CacheManager.getInstance().savePitchMin(pitchMin);
            CacheManager.getInstance().saveYawMax(yawMax);
            CacheManager.getInstance().saveYawMin(yawMin);
            CacheManager.getInstance().saveRollMax(rollMax);
            CacheManager.getInstance().saveRollMin(rollMin);
            CacheManager.getInstance().saveClarity(clarity);
            CacheManager.getInstance().saveSkin(skin);
            CacheManager.getInstance().saveConfidence(confidence);
            CacheManager.getInstance().saveEyeOpen(eyeOpen);
            CacheManager.getInstance().saveMouthOpen(mouthOpen);
            CacheManager.getInstance().saveOcclucsion(occlucsion);
            CacheManager.getInstance().saveBrightness(brightness);
            CacheManager.getInstance().saveDarkness(darkness);
            CacheManager.getInstance().saveBlackSpec(blackSpec);
            CacheManager.getInstance().saveSunglass(sunglass);
            CacheManager.getInstance().saveProceduremask(proceduremask);
            CacheManager.getInstance().saveOpenFaceBeauty(openFaceBeauty);
            CacheManager.getInstance().saveFaceBeautyOp(beautyOp);
            CacheManager.getInstance().saveFaceBeautyLift(beautyParam.getFace_lift_intensity());
            CacheManager.getInstance().saveFaceBeautySmooth(beautyParam.getSmooth_intensity());
            CacheManager.getInstance().saveFaceBeautyWhiten(beautyParam.getWhiten_intensity());
            CacheManager.getInstance().saveIsInit(false);
        } else {
            setSPParams();
        }
    }

    private void setSPParams() {
        try {
            CWLiveFaceParam cwLiveFaceParam = CWGeneralApi.getInstance().cwGetDetectorParams();

            cwLiveFaceParam.setRoi_x(CacheManager.getInstance().loadRoiX(0));
            cwLiveFaceParam.setRoi_y(CacheManager.getInstance().loadRoiY(0));
            cwLiveFaceParam.setRoi_width(CacheManager.getInstance().loadRoiWidth(0));
            cwLiveFaceParam.setRoi_height(CacheManager.getInstance().loadRoiHeight(0));
            cwLiveFaceParam.setMin_face_size(CacheManager.getInstance().loadMinFace(80));
            cwLiveFaceParam.setMax_face_size(CacheManager.getInstance().loadMaxFace(400));
            cwLiveFaceParam.setMax_face_num_perImg(CacheManager.getInstance().loadMaxFaceNum(1));
            cwLiveFaceParam.setPerfmonLevel(CacheManager.getInstance().loadPerformLevel(0));
            cwLiveFaceParam.setNir_face_compare(CacheManager.getInstance().loadIsCompare(0));
            cwLiveFaceParam.setOpen_liveness(CacheManager.getInstance().loadIsOpenLiveness(1));
            cwLiveFaceParam.setOpen_quality(CacheManager.getInstance().loadIsOpenQuality(1));
            cwLiveFaceParam.setPitch_max(CacheManager.getInstance().loadPitchMax(20.0f));
            cwLiveFaceParam.setPitch_min(CacheManager.getInstance().loadPitchMin(-20.0f));
            cwLiveFaceParam.setYaw_max(CacheManager.getInstance().loadYawMax(20.0f));
            cwLiveFaceParam.setYaw_min(CacheManager.getInstance().loadYawMin(-20.0f));
            cwLiveFaceParam.setRoll_max(CacheManager.getInstance().loadRollMax(20.0f));
            cwLiveFaceParam.setRoll_min(CacheManager.getInstance().loadRollMin(-20.0f));
            cwLiveFaceParam.setClarity(CacheManager.getInstance().loadClarity(0.5f));
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
            cwLiveFaceParam.setOpen_face_beauty(CacheManager.getInstance().loadOpenFaceBeauty(0));
            int beautyOp = CacheManager.getInstance().loadFaceBeautyOp(6);
            CWLiveFaceBeautyParam beautyParam = cwLiveFaceParam.getFace_beauty_param();
            if (beautyParam == null) {
                beautyParam = new CWLiveFaceBeautyParam();
            }
            beautyParam.setBeauty_op(beautyOp);
            beautyParam.setFace_lift_intensity(CacheManager.getInstance().loadFaceBeautyLift(1));
            beautyParam.setSmooth_intensity(CacheManager.getInstance().loadFaceBeautySmooth(1));
            beautyParam.setWhiten_intensity(CacheManager.getInstance().loadFaceBeautyWhiten(1));
            cwLiveFaceParam.setFace_beauty_param(beautyParam);
            CWEngine.getInstance().cwSetDetectorParams(cwLiveFaceParam);
            CWLiveFaceParam cwLiveFaceParam1 = CWEngine.getInstance().cwGetDetectorParams();
            if (cwLiveFaceParam == null) {
                return;
            }
            Logger.d(TAG, cwLiveFaceParam1 == null ? "null" : cwLiveFaceParam1.toString());
            mEtROIX.setText(cwLiveFaceParam1.getRoi_x() + "");
            mEtROIY.setText(cwLiveFaceParam1.getRoi_y() + "");
            mEtROIWidth.setText(cwLiveFaceParam1.getRoi_width() + "");
            mEtROIHeight.setText(cwLiveFaceParam1.getRoi_height() + "");
            mEtMinFace.setText(cwLiveFaceParam1.getMin_face_size() + "");
            mEtMaxFace.setText(cwLiveFaceParam1.getMax_face_size() + "");
            mSlideBtnDefaultFace.setOpen(CacheManager.getInstance().loadDefaultFace(1) == 1);
            mEtMaxFaceMount.setText(cwLiveFaceParam1.getMax_face_num_perImg() + "");
            mEtPerformLevel.setText(cwLiveFaceParam1.getPerfmonLevel() + "");
            mSlideBtnIsCompare.setOpen(CacheManager.getInstance().loadIsCompare(1) == 1);
            mSlideBtnIsOpenLiveness.setOpen(CacheManager.getInstance().loadIsOpenLiveness(1) == 1);
            mSlideBtnIsOpenQuality.setOpen(CacheManager.getInstance().loadIsOpenQuality(1) == 1);
            mEtPitchMax.setText(cwLiveFaceParam1.getPitch_max() + "");
            mEtPitchMin.setText(cwLiveFaceParam1.getPitch_min() + "");
            mEtYawMax.setText(cwLiveFaceParam1.getYaw_max() + "");
            mEtYawMin.setText(cwLiveFaceParam1.getYaw_min() + "");
            mEtRollMax.setText(cwLiveFaceParam1.getRoll_max() + "");
            mEtRollMin.setText(cwLiveFaceParam1.getRoll_min() + "");
            mEtClarity.setText(cwLiveFaceParam1.getClarity() + "");
            mEtSkin.setText(cwLiveFaceParam1.getSkinscore() + "");
            mEtConfidence.setText(cwLiveFaceParam1.getConfidence() + "");
            mEtOpenEye.setText(cwLiveFaceParam1.getEyeopen() + "");
            mEtOpenMouth.setText(cwLiveFaceParam1.getMouthopen() + "");
            mEtOcclusion.setText(cwLiveFaceParam1.getOcclusion() + "");
            mEtBrightness.setText(cwLiveFaceParam1.getHight_brightness_threshold() + "");
            mEtdarkness.setText(cwLiveFaceParam1.getLow_brightness_threshold() + "");
            mEtBlackspec.setText(cwLiveFaceParam1.getBlackspec() + "");
            mEtSunglass.setText(cwLiveFaceParam1.getSunglass() + "");
            mEtProceduremask.setText(cwLiveFaceParam1.getProceduremask() + "");
            mSlideBtnFaceBeauty.setOpen(cwLiveFaceParam1.getOpen_face_beauty() == 1);
            CWLiveFaceBeautyParam beautyParam1 = cwLiveFaceParam1.getFace_beauty_param();
            if (beautyParam1 == null) {
                beautyParam1 = new CWLiveFaceBeautyParam();
            }
            int beautyOp1 = beautyParam1.getBeauty_op();
            mBeautySpinner.setSelection(beautyOp1 == 16 ? 8 : (beautyOp1 - 1));
            mEtFaceBeautyLift.setText(beautyParam1.getFace_lift_intensity() + "");
            mEtFaceBeautySmooth.setText(beautyParam1.getSmooth_intensity() + "");
            mEtFaceBeautyWhiten.setText(beautyParam1.getWhiten_intensity() + "");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        CWEngine.getInstance().cwUninit();
    }
}
