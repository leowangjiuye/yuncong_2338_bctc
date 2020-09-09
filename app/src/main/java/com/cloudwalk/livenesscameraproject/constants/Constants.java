package com.cloudwalk.livenesscameraproject.constants;

public class Constants {
    //活体参数
    public static final int ROI_X = 0;
    public static final int ROI_Y = 0;
    public static final int ROI_WIDTH = 0;
    public static final int ROI_HEIGHT = 0;
    public static final int FACE_MIN = 50;
    public static final int FACE_MAX = 100;
    public static final int FACE_NUM_MAX = 10;
    public static final int PERFORM_LEVEL = 100;
    public static final boolean IS_NIR_COMPARE = true;
    public static final boolean IS_OPEN_LIVENESS = true;
    public static final boolean IS_OPEN_QUALITY = true;
    public static final int PITCH_MAX = 30;
    public static final int PITCH_MIN = -30;
    public static final int YAX_MAX = 30;
    public static final int YAX_MIN = -30;
    public static final int ROLL_MAX = 30;
    public static final int ROLL_MIN = -30;
    public static final float CLARITY = 0.5f;
    public static final float SKIN = 0.35f;
    public static final float CONFIDENCE = 0.55f;
    public static final float EYE_OPEN = 0.5f;
    public static final float MOUTH_OPEN = 0.5f;
    public static final float OCCLUSION = 0.5f;
    public static final float HIGH_BRTGHTNESS = 0.5f;
    public static final float LOW_BRTGHTNESS = 0.5f;
    public static final float BLACK_SPEC = 0.5f;
    public static final float SUN_GLASS = 0.5f;
    //相机参数
    public static final int CAMERA_WIDTH = 640;
    public static final int CAMERA_HEIGHT = 480;
    public static final int DISPLAY_WIDTH = 640;
    public static final int DISPLAY_HEIGHT = 480;
    //其他功能
    public static final boolean IS_TRACK = false;
    public static final int TRACK_WIDTH = 640;
    public static final int TRACK_HEIGHT = 480;
    //配置文件
    public static final String SP_KEY_LIVENESS_MODEL_PATH = "faceRecogFile";

}
