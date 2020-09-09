package com.cloudwalk.livenesscameraproject.constants;

public class CacheConstant {
    public static final String KEY_CAMERA_WIDTH = "cameraWidth";  //分辨率宽
    public static final String KEY_CAMERA_HEIGHT = "cameraHeight"; //分辨率高
    public static final String KEY_RGB_PID = "rgbPid"; // 相机PID
    public static final String KEY_RGB_VID = "rgbVid"; // 相机VID
    public static final String KEY_IR_PID = "irPid"; // irPid
    public static final String KEY_IR_VID = "irVid"; // irVid
    public static final String KEY_ROTATION = "rotation"; // 旋转角度
    public static final String KEY_MIRROR = "mirror"; // 是否显示镜像
    public static final String KEY_IS_SAVE_DEBUG_IMAGE = "isSaveDebugImage"; // 是否保存debug图片
    public static final String KEY_DEBUG_IMAGE_PATH = "debugImagePath"; // debug图片路径
    public static final String KEY_CONFIG_FILEPATH = "configFilePath"; // configFile
    public static final String KEY_FACE_DETMODEL_PATH = "faceDetModelPath"; // 人脸检测器模型路径
    public static final String KEY_FACE_POINT_MODEL_PATH = "keyFacePointModelPath"; // 人脸关键点模型路径
    public static final String KEY_FACE_QUALITY_FILE = "faceQualityFile"; //
    public static final String KEY_FACE_RECOG_FILE = "faceRecogFile"; //
    public static final String KEY_FACE_LIVENESS_FILE = "faceLivenessFile"; //
    public static final String KEY_IS_MULTITHREAD = "isMultiThread"; //
    public static final String KEY_TRACK_PREVIEWHEIGHT = "trackPreviewHeight"; //
    public static final String KEY_TRACK_PREVIEWWIDTH = "trackPreviewWidth"; //
    public static final String KEY_IS_OPEN_FACETRACK = "isOpenFaceTrack"; //

    /**
     * ******************************** livenessParamSettingActivity *******************************
     */
    public static final String KEY_IS_INIT = "isInit";   // livenessParamSettingActivity 页面是否保存过

    public static final String KEY_ROI_X = "roix";
    public static final String KEY_ROI_Y = "roiy";
    public static final String KEY_ROI_WIDTH = "roiWidth";
    public static final String KEY_ROI_HEIGHT = "roiHeight";
    public static final String KEY_MINFACE = "minFace";
    public static final String KEY_MAXFACE = "maxFace";
    public static final String KEY_MAX_FACENUM = "maxFaceNum";
    public static final String KEY_PERFORM_LEVEL = "performLevel";
    public static final String KEY_IS_COMPARE = "isCompare";
    public static final String KEY_IS_OPEN_LIVENESS = "isOpenLiveness";
    public static final String KEY_IS_OPEN_QUALITY = "isOpenQuality";
    public static final String KEY_PITCH_MAX = "pitchMax";
    public static final String KEY_PITCH_MIN = "pitchMin";
    public static final String KEY_YAW_MAX = "yawMax";
    public static final String KEY_YAW_MIN = "yawMin";
    public static final String KEY_ROLL_MAX = "rollMax";
    public static final String KEY_ROLL_MIN = "rollMin";
    public static final String KEY_CLARITY = "clarity";
    public static final String KEY_SKIN = "skin";
    public static final String KEY_CONFIDENCE = "confidence";
    public static final String KEY_EYE_OPEN = "eyeOpen";
    public static final String KEY_MOUTH_OPEN = "mouthOpen";
    public static final String KEY_OCCLUCSION = "occlucsion";
    public static final String KEY_BRIGHTNESS = "brightness";
    public static final String KEY_DARKNESS = "darkness";
    public static final String KEY_BLACKSPEC = "blackSpec";
    public static final String KEY_SUNGLASS = "sunglass";
    public static final String KEY_PROCEDUREMASK = "proceduremask";
    public static final String KEY_DEFAULT_FACE = "defaultFace";
    public static final String KEY_OPEN_FACE_BEAUTY = "openFaceBeauty";
    public static final String KEY_FACE_BEAUTY_OP = "faceBeautyOp";
    public static final String KEY_FACE_BEAUTY_FACELift = "faceBeautyFaceLift";
    public static final String KEY_FACE_BEAUTY_SMOOTH = "faceBeautySmooth";
    public static final String KEY_FACE_BEAUTY_WHITEN = "faceBeautyWhiten";



    /**
     * ************************** CameraConfigActivity ***********************************
     */
    public static final String KEY_CAMERA_TYPE = "cameraType";
    public static final String KEY_IS_CAMERA_PARAMS_INIT = "isCameraParamsInit";

    /**
     * ************************* RenderSettingActivity ***********************************
     */
    public static final String KEY_IS_PREVIEW_INIT = "isPreviewInit";  //RenderSettingActivity 是否保存过
    public static final String KEY_IS_PREVIEW = "isPreview";  //是否显示预览
    public static final String KEY_LIVENESS_TIPS = "livenessTips";
    public static final String KEY_FONTSIZE = "fontsize";
    public static final String KEY_FONTCOLOR = "fontcolor";



    /**
     * ************************ ConfigSettingActivity ************************************
     */
    public static final String KEY_IS_CONFIG_INIT = "isConfigInit";  // ConfigSettingActivity是否保存过
    public static final String KEY_IS_SHOW_PREVIEW_BGIMG = "isShowPreviewBgImg";  //是否显示预览图片
    public static final String KEY_PREVIEW_IMAGEPATH = "previewImagePath"; //预览图片路径
    public static final String KEY_FACE_MASK_IMAGEPATH = "faceMaskImagePath";
    public static final String KEY_DEFINE_SAVEFORMAT = "defineSaveFormat";
    public static final String KEY_IS_SHOW_FACEMASK = "isShowFaceMask";
    public static final String KEY_BEST_FACERATIO = "bestFaceRatio";

    /**
     * *********************** OtherFuncSettingActivity ***********************************
     */
    public static final String KEY_IS_OTHERSETTINGS_INIT = "isOtherSettingsInit";  //OtherFuncSettingActivity页面是否保存过

    /**
     * ********************** LivenessCheckParamActivity **********************************
     */
    public static final String KEY_M = "key_m";  //活体连续次数
    public static final String KEY_N = "key_n"; //活体成功次数
    public static final String KEY_S = "key_s"; //活体检测时间范围
    public static final String KEY_ID = "key_id"; //活体检测时间范围

    /**
     * *********************** FeatureSettingsActivity ************************************
     */
    public static final String KEY_FEATURE_IMAGE_GETTYPE = "featureImageGetType";
    public static final String KEY_FEATURE_THRESHOLD = "featureThreshold";
    public static final String KEY_CAMERA_SELECT_TYPE = "cameraSelectType";
    public static final String KEY_CAMERA_SELECT_INDEX = "cameraSelectIndex";
    /**
     * *********************** ScanSettingsActivity ************************************
     */
    public static final String KEY_IS_OPEN_SCANNER = "isOpenScanner";

}
