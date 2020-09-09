package com.cloudwalk.livenesscameraproject.manager;

import com.cloudwalk.livenesscameraproject.constants.CacheConstant;
import com.cloudwalk.livenesscameraproject.utils.MMKVTool;

/**
 * 缓存管理(取缔 sp)
 */
public class CacheManager {
    private CacheManager() {
    }

    public static CacheManager getInstance() {
        return SingletonHolder.sInstance;
    }

    //静态内部类
    private static class SingletonHolder {
        private static final CacheManager sInstance = new CacheManager();
    }

    public void saveCameraWidth(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_CAMERA_WIDTH, value);
    }

    public int loadCameraWidth(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_CAMERA_WIDTH, defaultValue);
    }

    public void saveCameraHeight(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_CAMERA_HEIGHT, value);
    }

    public int loadCameraHeight(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_CAMERA_HEIGHT, defaultValue);
    }

    public void saveRgbPid(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_RGB_PID, value);
    }

    public int loadRgbPid(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_RGB_PID, defaultValue);
    }

    public void saveRgbVid(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_RGB_VID, value);
    }

    public int loadRgbVid(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_RGB_VID, defaultValue);
    }

    public void saveIrPid(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_IR_PID, value);
    }

    public int loadIrPid(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_IR_PID, defaultValue);
    }

    public void saveIrVid(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_IR_VID, value);
    }

    public int loadIrVid(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_IR_VID, defaultValue);
    }

    public void saveRotation(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_ROTATION, value);
    }

    public int loadRotation(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_ROTATION, defaultValue);
    }

    public void saveMirror(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_MIRROR, value);
    }

    public boolean loadMirror(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_MIRROR, defaultValue);
    }

    public void saveIsSaveDebugImage(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_SAVE_DEBUG_IMAGE, value);
    }

    public boolean loadIsSaveDebugImage(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_SAVE_DEBUG_IMAGE, defaultValue);
    }

    public void saveDebugImagePath(String value) {
        MMKVTool.saveString(CacheConstant.KEY_DEBUG_IMAGE_PATH, value);
    }

    public String loadDebugImagePath(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_DEBUG_IMAGE_PATH, defaultValue);
    }

    public void saveConfigFilePath(String value) {
        MMKVTool.saveString(CacheConstant.KEY_CONFIG_FILEPATH, value);
    }

    public String loadConfigFilePath(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_CONFIG_FILEPATH, defaultValue);
    }

    public void saveFaceDetModelPath(String value) {
        MMKVTool.saveString(CacheConstant.KEY_FACE_DETMODEL_PATH, value);
    }

    public String loadFaceDetModelPath(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_FACE_DETMODEL_PATH, defaultValue);
    }

    public void saveKeyFacePointModelPath(String value) {
        MMKVTool.saveString(CacheConstant.KEY_FACE_POINT_MODEL_PATH, value);
    }

    public String loadKeyFacePointModelPath(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_FACE_POINT_MODEL_PATH, defaultValue);
    }

    public void saveFaceQualityFile(String value) {
        MMKVTool.saveString(CacheConstant.KEY_FACE_QUALITY_FILE, value);
    }

    public String loadFaceQualityFile(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_FACE_QUALITY_FILE, defaultValue);
    }

    public void saveFaceRecogFile(String value) {
        MMKVTool.saveString(CacheConstant.KEY_FACE_RECOG_FILE, value);
    }

    public String loadFaceRecogFile(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_FACE_RECOG_FILE, defaultValue);
    }

    public void saveFaceLivenessFile(String value) {
        MMKVTool.saveString(CacheConstant.KEY_FACE_LIVENESS_FILE, value);
    }

    public String loadFaceLivenessFile(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_FACE_LIVENESS_FILE, defaultValue);
    }

    public void saveIsMultiThread(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_MULTITHREAD, value);
    }

    public boolean loadIsMultiThread(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_MULTITHREAD, defaultValue);
    }

    public void saveTrackPreviewHeight(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_TRACK_PREVIEWHEIGHT, value);
    }

    public int loadTrackPreviewHeight(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_TRACK_PREVIEWHEIGHT, defaultValue);
    }

    public void saveTrackPreviewWidth(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_TRACK_PREVIEWWIDTH, value);
    }

    public int loadTrackPreviewWidth(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_TRACK_PREVIEWWIDTH, defaultValue);
    }

    public void saveIsOpenFaceTrack(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_OPEN_FACETRACK, value);
    }

    public boolean loadIsOpenFaceTrack(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_OPEN_FACETRACK, defaultValue);
    }

    /**
     * **************************** livenessParamSettingActivity ************************************
     */
    public void saveIsInit(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_INIT, value);
    }

    public boolean loadIsInit(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_INIT, defaultValue);
    }

    public void saveRoiX(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_ROI_X, value);
    }

    public int loadRoiX(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_ROI_X, defaultValue);
    }

    public void saveRoiY(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_ROI_Y, value);
    }

    public int loadRoiY(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_ROI_Y, defaultValue);
    }

    public void saveRoiWidth(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_ROI_WIDTH, value);
    }

    public int loadRoiWidth(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_ROI_WIDTH, defaultValue);
    }

    public void saveRoiHeight(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_ROI_HEIGHT, value);
    }

    public int loadRoiHeight(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_ROI_HEIGHT, defaultValue);
    }

    public void saveMinFace(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_MINFACE, value);
    }

    public int loadMinFace(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_MINFACE, defaultValue);
    }

    public void saveMaxFace(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_MAXFACE, value);
    }

    public int loadMaxFace(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_MAXFACE, defaultValue);
    }

    public void saveMaxFaceNum(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_MAX_FACENUM, value);
    }

    public int loadMaxFaceNum(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_MAX_FACENUM, defaultValue);
    }

    public void savePerformLevel(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_PERFORM_LEVEL, value);
    }

    public int loadPerformLevel(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_PERFORM_LEVEL, defaultValue);
    }

    public void saveIsCompare(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_IS_COMPARE, value);
    }

    public int loadIsCompare(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_IS_COMPARE, defaultValue);
    }

    public void saveIsOpenLiveness(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_IS_OPEN_LIVENESS, value);
    }

    public int loadIsOpenLiveness(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_IS_OPEN_LIVENESS, defaultValue);
    }

    public void saveIsOpenQuality(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_IS_OPEN_QUALITY, value);
    }

    public int loadIsOpenQuality(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_IS_OPEN_QUALITY, defaultValue);
    }

    public void savePitchMax(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_PITCH_MAX, value);
    }

    public float loadPitchMax(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_PITCH_MAX, defaultValue);
    }

    public void savePitchMin(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_PITCH_MIN, value);
    }

    public float loadPitchMin(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_PITCH_MIN, defaultValue);
    }

    public void saveYawMax(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_YAW_MAX, value);
    }

    public float loadYawMax(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_YAW_MAX, defaultValue);
    }

    public void saveYawMin(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_YAW_MIN, value);
    }

    public float loadYawMin(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_YAW_MIN, defaultValue);
    }

    public void saveRollMax(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_ROLL_MAX, value);
    }

    public float loadRollMax(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_ROLL_MAX, defaultValue);
    }

    public void saveRollMin(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_ROLL_MIN, value);
    }

    public float loadRollMin(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_ROLL_MIN, defaultValue);
    }

    public void saveClarity(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_CLARITY, value);
    }

    public float loadClarity(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_CLARITY, defaultValue);
    }

    public void saveSkin(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_SKIN, value);
    }

    public float loadSkin(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_SKIN, defaultValue);
    }

    public void saveConfidence(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_CONFIDENCE, value);
    }

    public float loadConfidence(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_CONFIDENCE, defaultValue);
    }

    public void saveEyeOpen(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_EYE_OPEN, value);
    }

    public float loadEyeOpen(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_EYE_OPEN, defaultValue);
    }

    public void saveMouthOpen(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_MOUTH_OPEN, value);
    }

    public float loadMouthOpen(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_MOUTH_OPEN, defaultValue);
    }

    public void saveOcclucsion(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_OCCLUCSION, value);
    }

    public float loadOcclucsion(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_OCCLUCSION, defaultValue);
    }

    public void saveBrightness(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_BRIGHTNESS, value);
    }

    public float loadBrightness(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_BRIGHTNESS, defaultValue);
    }

    public void saveDarkness(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_DARKNESS, value);
    }

    public float loadDarkness(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_DARKNESS, defaultValue);
    }

    public void saveBlackSpec(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_BLACKSPEC, value);
    }

    public float loadBlackSpec(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_BLACKSPEC, defaultValue);
    }

    public void saveSunglass(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_SUNGLASS, value);
    }

    public float loadSunglass(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_SUNGLASS, defaultValue);
    }

    public void saveProceduremask(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_PROCEDUREMASK, value);
    }

    public float loadProceduremask(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_PROCEDUREMASK, defaultValue);
    }

    public void saveDefaultFace(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_DEFAULT_FACE, value);
    }

    public int loadDefaultFace(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_DEFAULT_FACE, defaultValue);
    }

    public void saveOpenFaceBeauty(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_OPEN_FACE_BEAUTY, value);
    }

    public int loadOpenFaceBeauty(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_OPEN_FACE_BEAUTY, defaultValue);
    }

    public void saveFaceBeautyOp(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_FACE_BEAUTY_OP, value);
    }

    public int loadFaceBeautyOp(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_FACE_BEAUTY_OP, defaultValue);
    }

    public void saveFaceBeautyLift(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_FACE_BEAUTY_FACELift, value);
    }

    public float loadFaceBeautyLift(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_FACE_BEAUTY_FACELift, defaultValue);
    }

    public void saveFaceBeautySmooth(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_FACE_BEAUTY_SMOOTH, value);
    }

    public float loadFaceBeautySmooth(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_FACE_BEAUTY_SMOOTH, defaultValue);
    }

    public void saveFaceBeautyWhiten(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_FACE_BEAUTY_WHITEN, value);
    }

    public float loadFaceBeautyWhiten(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_FACE_BEAUTY_WHITEN, defaultValue);
    }

    /**
     * ********************** CameraConfigActivity ********************************************
     */
    public void saveCameraType(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_CAMERA_TYPE, value);
    }

    public int loadCameraType(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_CAMERA_TYPE, defaultValue);
    }

    public void saveCameraSelectIndex(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_CAMERA_SELECT_INDEX, value);
    }

    public int loadCameraSelectIndex(int defaultIndex) {
        return MMKVTool.loadInt(CacheConstant.KEY_CAMERA_SELECT_INDEX, defaultIndex);
    }

    public void saveIsCameraParamsInit(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_CAMERA_PARAMS_INIT, value);
    }

    public boolean loadIsCameraParamsInit(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_CAMERA_PARAMS_INIT, defaultValue);
    }

    public void saveBoolAutoKeyValue(String key , boolean value) {
        MMKVTool.saveBoolean(key, value);
    }

    public boolean loadBoolAutoKeyValue(String key , boolean defaultValue) {
        return MMKVTool.loadBoolean(key, false);
    }

    public void saveIntAutoKeyValue(String key, int value) {
        MMKVTool.saveInt(key, value);
    }

    /**
     * *************************** RenderSettingActivity **************************************
     */
    public void saveIsPreview(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_PREVIEW, value);
    }

    public boolean loadIsPreview(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_PREVIEW, defaultValue);
    }

    public void saveLivenessTips(String value) {
        MMKVTool.saveString(CacheConstant.KEY_LIVENESS_TIPS, value);
    }

    public String loadLivenessTips(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_LIVENESS_TIPS, defaultValue);
    }

    public void saveFontSize(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_FONTSIZE, value);
    }

    public int loadFontSize(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_FONTSIZE, defaultValue);
    }

    public void saveFontColor(String value) {
        MMKVTool.saveString(CacheConstant.KEY_FONTCOLOR, value);
    }

    public String loadFontColor(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_FONTCOLOR, defaultValue);
    }

    public void saveIsPreviewInit(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_PREVIEW_INIT, value);
    }

    public boolean loadIsPreviewInit(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_PREVIEW_INIT, defaultValue);
    }

    /**
     * ************************ ConfigSettingActivity ************************************
     */
    public void saveIsShowPreviewBgImg(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_SHOW_PREVIEW_BGIMG, value);
    }

    public boolean loadIsShowPreviewBgImg(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_SHOW_PREVIEW_BGIMG, defaultValue);
    }

    public void savePreviewImagePath(String value) {
        MMKVTool.saveString(CacheConstant.KEY_PREVIEW_IMAGEPATH, value);
    }

    public String loadPreviewImagePath(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_PREVIEW_IMAGEPATH, defaultValue);
    }

    public void saveFaceMaskImagePath(String value) {
        MMKVTool.saveString(CacheConstant.KEY_FACE_MASK_IMAGEPATH, value);
    }

    public String loadFaceMaskImagePath(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_FACE_MASK_IMAGEPATH, defaultValue);
    }

    public void saveDefineSaveFormat(String value) {
        MMKVTool.saveString(CacheConstant.KEY_DEFINE_SAVEFORMAT, value);
    }

    public String loadDefineSaveFormat(String defaultValue) {
        return MMKVTool.loadString(CacheConstant.KEY_DEFINE_SAVEFORMAT, defaultValue);
    }

    public void saveIsShowFaceMask(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_SHOW_FACEMASK, value);
    }

    public boolean loadIsShowFaceMask(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_SHOW_FACEMASK, defaultValue);
    }

    public void saveBestFaceRatio(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_BEST_FACERATIO, value);
    }

    public float loadBestFaceRatio(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_BEST_FACERATIO, defaultValue);
    }

    public void saveIsConfigInit(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_CONFIG_INIT, value);
    }

    public boolean loadIsConfigInit(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_CONFIG_INIT, defaultValue);
    }

    /**
     * ***************************** OtherFuncSettingActivity *********************************
     * @param value
     */
    public void saveIsOtherSettingsInit(boolean value) {
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_OTHERSETTINGS_INIT, value);
    }

    public boolean loadIsOtherSettingsInit(boolean defaultValue) {
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_OTHERSETTINGS_INIT, defaultValue);
    }

    /**
     * ********************************* LivenessCheckParamsAvtivity **************************
     */
    public void saveKeyM(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_M, value);
    }

    public int loadKeyM(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_M, defaultValue);
    }

    public void saveKeyN(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_N, value);
    }

    public int loadKeyN(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_N, defaultValue);
    }

    public void saveKeyS(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_S, value);
    }

    public int loadKeyS(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_S, defaultValue);
    }

    public void saveKeyId(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_ID, value);
    }

    public int loadKeyId(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_ID, defaultValue);
    }

    /**
     * ****************************** FeatureSettingsActivity ********************************
     */
    public void saveFeatureImageGetType(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_FEATURE_IMAGE_GETTYPE, value);
    }

    public int loadFeatureImageGetType(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_FEATURE_IMAGE_GETTYPE, defaultValue);
    }

    public void saveFeatureThreshold(float value) {
        MMKVTool.saveFloat(CacheConstant.KEY_FEATURE_THRESHOLD, value);
    }

    public float loadFeatureThreshold(float defaultValue) {
        return MMKVTool.loadFloat(CacheConstant.KEY_FEATURE_THRESHOLD, defaultValue);
    }

    public void saveCameraSelectType(int value) {
        MMKVTool.saveInt(CacheConstant.KEY_CAMERA_SELECT_TYPE, value);
    }

    public int loadCameraSelectType(int defaultValue) {
        return MMKVTool.loadInt(CacheConstant.KEY_CAMERA_SELECT_TYPE, defaultValue);
    }

    public void saveIsOpenScanner(Boolean isOpenScanner){
        MMKVTool.saveBoolean(CacheConstant.KEY_IS_OPEN_SCANNER, isOpenScanner);
    }

    public boolean loadIsOpenScanner(boolean defaultValue){
        return MMKVTool.loadBoolean(CacheConstant.KEY_IS_OPEN_SCANNER, defaultValue);
    }

}
