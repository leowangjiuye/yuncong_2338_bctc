package com.cloudwalk.livenesscameraproject.activity.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import com.cloudwalk.cwlivenesscamera.Camera.Size;
import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.adapter.TextAdapter;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.utils.FileUtil;
import com.cloudwalk.livenesscameraproject.utils.Logger;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.view.CameraParamsLaytout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.cloudwalk.midware.camera.entity.CWCameraInfos;
import cn.cloudwalk.midware.engine.CWApi;
import cn.cloudwalk.midware.engine.CWCameraConfig;
import cn.cloudwalk.midware.engine.CWCameraInfo;
import cn.cloudwalk.midware.engine.CWGeneralApi;
import cn.cloudwalk.midware.engine.CWLivenessConfig;
import cn.cloudwalk.midware.engine.CWPreivewConfig;

public class CameraConfigActivity extends BaseActivity {

    private static final String TAG = "CameraConfigActivity";
    private Spinner spinnerPreviewSizes, spinnerOpenTyps, spinnerCameraTypes;
    private Map<String, List<Size>> map;        //当可见光相机PID&VID设置错误时无法获取
    private CameraConfigActivity mContext;
    private String[] types = {"PID/VID"};
    private String[] cameraTypes = {"", "UVC", "V4L2"};
    private int mCameraWidth, mCameraHeight;
    private GridView mGridViewCameraParams;
    private String[] mCameraParamsNameArray;

    private LinkedHashMap<String, Pair<Boolean, Integer>> paramsPairHashMap = new LinkedHashMap<>();
    private EditText mEtRgbPid;
    private EditText mEtRgbVid;
    private EditText mEtIrPid;
    private EditText mEtIrVid;
    private static final String BESTFACE_PATH = "/sdcard/cloudwalk/最佳人脸/";
    private static final String ROOT_PATH = "/sdcard/cloudwalk/";
    private ArrayList<CWCameraInfos> cameraInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_para_set);
        mCameraParamsNameArray = getResources().getStringArray(R.array.array_camera_params);
        mContext = this;
        map = CWGeneralApi.getInstance().cwGetCameraSupportedResolutions();

        initView();
        initParams();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mEtRgbPid = (EditText) findViewById(R.id.et_rgb_pid);
        mEtRgbVid = (EditText) findViewById(R.id.et_rgb_vid);
        mEtIrPid = (EditText) findViewById(R.id.et_ir_pid);
        mEtIrVid = (EditText) findViewById(R.id.et_ir_vid);

        mGridViewCameraParams = (GridView) findViewById(R.id.gv_camera_params);
        ImageView imageView = findViewById(R.id.iv_camera_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView mBtnSave = findViewById(R.id.tv_camera_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCameraConfig();
            }
        });

        spinnerCameraTypes = findViewById(R.id.sp_camera_type);
        TextAdapter cameraTypesAdapter = new TextAdapter(cameraTypes, CameraConfigActivity.this);
        spinnerCameraTypes.setAdapter(cameraTypesAdapter);
        spinnerCameraTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spinnerCameraTypes.setSelection(1);
//                    Toast.makeText(CameraConfigActivity.this, "暂不支持原生相机", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPreviewSizes = (Spinner) findViewById(R.id.sp_camera_previewSize);
        CameraSizesAdapter cameraSizesAdapter = new CameraSizesAdapter(map.get("VIS_SIZES"));
        spinnerPreviewSizes.setAdapter(cameraSizesAdapter);
        spinnerPreviewSizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCameraWidth = map.get("VIS_SIZES").get(position).getWidth();
                mCameraHeight = map.get("VIS_SIZES").get(position).getHeight();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOpenTyps = (Spinner) findViewById(R.id.sp_camera_openCamera);
        TextAdapter openTypesAdapter = new TextAdapter(types, CameraConfigActivity.this);
        spinnerOpenTyps.setAdapter(openTypesAdapter);
    }

    private void saveCameraConfig() {

        //Author YCWB0151 修改相机分辨率时重置缓存ROI
        if (CacheManager.getInstance().loadCameraWidth(640) != mCameraWidth
                || CacheManager.getInstance().loadCameraHeight(480) != mCameraHeight) {
            CacheManager.getInstance().saveRoiX(0);
            CacheManager.getInstance().saveRoiY(0);
            CacheManager.getInstance().saveRoiWidth(0);
            CacheManager.getInstance().saveRoiHeight(0);
        }

        CacheManager.getInstance().saveCameraWidth(mCameraWidth);
        CacheManager.getInstance().saveCameraHeight(mCameraHeight);
        int rgbPid, rgbVid, irPid, irVid;
        try {
            rgbPid = Integer.parseInt(mEtRgbPid.getText() + "");
            rgbVid = Integer.parseInt(mEtRgbVid.getText() + "");
            irPid = Integer.parseInt(mEtIrPid.getText() + "");
            irVid = Integer.parseInt(mEtIrVid.getText() + "");
        } catch (NumberFormatException e) {
            Toast.makeText(CameraConfigActivity.this, "输入值格式不符",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        CacheManager.getInstance().saveRgbPid(rgbPid);
        CacheManager.getInstance().saveRgbVid(rgbVid);
        CacheManager.getInstance().saveIrPid(irPid);
        CacheManager.getInstance().saveIrVid(irVid);
        int cameraType = spinnerCameraTypes.getSelectedItemPosition();
        CacheManager.getInstance().saveCameraType(cameraType);
        //保存参数
        List<CWCameraInfo> deviceList = CWGeneralApi.getInstance().cwEnumCameras(CameraConfigActivity.this);
        boolean isErrorId;
        int cameraFlag = 0;
        for (CWCameraInfo info : deviceList) {
            if ((info.getPid() == rgbPid
                    && info.getVid() == rgbVid)
                    || (info.getPid() == irPid
                    && info.getVid() == irVid)) {
                cameraFlag++;
            }
        }
        isErrorId = cameraFlag <= 1;
        for (int i = 0; i < mCameraParamsNameArray.length; i++) {
            String name = mCameraParamsNameArray[i];
            View view1 = mGridViewCameraParams.getChildAt(i);
            CameraParamsLaytout cameraParamsLaytout = (CameraParamsLaytout) view1.findViewById(R.id.params_layout);

            boolean isAuto = cameraParamsLaytout.getOpen();
            int value;
            try {
                value = cameraParamsLaytout.getValue();
            } catch (NumberFormatException e) {
                Toast.makeText(CameraConfigActivity.this, "输入值格式不符",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            try {
                int key = 0;
                switch (name) {
                    case "亮度":
                        key = CWGeneralApi.PU_BRIGHTNESS;
                        break;
                    case "对比度":
                        if (isAuto) {
                            key = CWGeneralApi.PU_CONTRAST_AUTO;
                            value = CWGeneralApi.YC_AUTO_ENABLE;
                        } else {
                            CWGeneralApi.getInstance().cwSetCameraParameter(CWGeneralApi.PU_CONTRAST_AUTO, CWGeneralApi.YC_AUTO_DISABLE);
                            key = CWGeneralApi.PU_CONTRAST;
                        }
                        break;
                    case "色调":
                        if (isAuto) {
                            key = CWGeneralApi.PU_HUE_AUTO;
                            value = CWGeneralApi.YC_AUTO_ENABLE;
                        } else {
                            CWGeneralApi.getInstance().cwSetCameraParameter(CWGeneralApi.PU_HUE_AUTO, CWGeneralApi.YC_AUTO_DISABLE);
                            key = CWGeneralApi.PU_HUE;
                        }
                        break;
                    case "饱和度":
                        key = CWGeneralApi.PU_SATURATION;
                        break;
                    case "锐度":
                        key = CWGeneralApi.PU_SHARPNESS;
                        break;
                    case "伽玛值":
                        key = CWGeneralApi.PU_GAMMA;
                        break;
                    case "白平衡":
                        if (isAuto) {
                            key = CWGeneralApi.PU_WB_TEMP_AUTO;
                            value = CWGeneralApi.YC_AUTO_ENABLE;
                        } else {
                            CWGeneralApi.getInstance().cwSetCameraParameter(CWGeneralApi.PU_WB_TEMP_AUTO, CWGeneralApi.YC_AUTO_DISABLE);
                            key = CWGeneralApi.PU_WB_TEMP;
                        }
                        break;
                    case "逆光值":
                        key = CWGeneralApi.PU_BACKLIGHT;
                        break;
                    case "增益":
                        key = CWGeneralApi.PU_GAIN;
                        break;
                    case "倾斜":
                        key = CWGeneralApi.CTRL_PANTILT_ABS;
                        break;
                    case "滚动":
                        key = CWGeneralApi.CTRL_ROLL_ABS;
                        break;
                    case "缩放":
                        key = CWGeneralApi.CTRL_ZOOM_ABS;
                        break;
                    case "曝光值":
                        if (isAuto) {
                            key = CWGeneralApi.CTRL_AE;
                            value = CWGeneralApi.YC_AUTO_ENABLE;
                        } else {
                            CWGeneralApi.getInstance().cwSetCameraParameter(CWGeneralApi.CTRL_AE, CWGeneralApi.YC_AUTO_DISABLE);
                            key = CWGeneralApi.CTRL_AE_ABS;
                        }
                        break;
                    case "光圈值":
                        key = CWGeneralApi.CTRL_IRIS_ABS;
                        break;
                    case "焦点值":
                        key = CWGeneralApi.CTRL_FOCUS_ABS;
                        break;
                }
                CWGeneralApi.getInstance().cwSetCameraParameter(key, value);
            } catch (IllegalStateException e) {
                if (isErrorId) {
                    Toast.makeText(CameraConfigActivity.this.getApplicationContext(), "相机连接错误,请输入正确PID&VID初始化", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if (isErrorId) {
            Toast.makeText(CameraConfigActivity.this.getApplicationContext(), "请输入正确PID&VID", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    private void initParams() {
        boolean isCameraParamsInit = CacheManager.getInstance().loadIsCameraParamsInit(false);

        mEtRgbPid.setText(CacheManager.getInstance().loadRgbPid(0xC053) + "");
        mEtRgbVid.setText(CacheManager.getInstance().loadRgbVid(0x0C45) + "");
        mEtIrPid.setText(CacheManager.getInstance().loadIrPid(0xB051) + "");
        mEtIrVid.setText(CacheManager.getInstance().loadIrVid(0x0C45) + "");
        if (!isCameraParamsInit) {
            //first time
            CacheManager.getInstance().saveCameraType(1);
            spinnerCameraTypes.setSelection(1);

            mCameraWidth = 640;
            mCameraHeight = 480;
            CacheManager.getInstance().saveCameraWidth(mCameraWidth);
            CacheManager.getInstance().saveCameraHeight(mCameraHeight);
            CacheManager.getInstance().saveIsCameraParamsInit(true);
            try {
                CacheManager.getInstance().saveRgbPid(Integer.parseInt(mEtRgbPid.getText().toString()));
                CacheManager.getInstance().saveRgbVid(Integer.parseInt(mEtRgbVid.getText().toString()));
                CacheManager.getInstance().saveIrPid(Integer.parseInt(mEtIrPid.getText().toString()));
                CacheManager.getInstance().saveIrVid(Integer.parseInt(mEtIrVid.getText().toString()));
            } catch (Exception e) {
                Logger.d("数据类型出错了： ", "camera config error!");
                e.printStackTrace();
            }
            spinnerPreviewSizes.setSelection(0);
        } else {
            //not first time
            int cameraType = CacheManager.getInstance().loadCameraType(1);
            spinnerCameraTypes.setSelection(cameraType);
            int cameraWidth = CacheManager.getInstance().loadCameraWidth(640);
            int cameraHeight = CacheManager.getInstance().loadCameraHeight(480);

            if (map.get("VIS_SIZES") != null) {
                for (int i = 0; i < map.get("VIS_SIZES").size(); i++) {
                    if (map.get("VIS_SIZES").get(i).getWidth() == cameraWidth && map.get("VIS_SIZES").get(i).getHeight() == cameraHeight) {
                        spinnerPreviewSizes.setSelection(i);
                    }
                }
            } else {
                mCameraWidth = cameraWidth;
                mCameraHeight = cameraHeight;
            }
        }

        for (int i = 0; i < mCameraParamsNameArray.length; i++) {
            String name = mCameraParamsNameArray[i];
            int key = 0;
            boolean auto = false;
            switch (name) {
                case "亮度":
                    key = CWGeneralApi.PU_BRIGHTNESS;
                    break;
                case "对比度":
                    key = CWGeneralApi.PU_CONTRAST;
                    auto = CWGeneralApi.getInstance().cwGetCameraParameter(CWGeneralApi.PU_CONTRAST_AUTO) == CWApi.YC_AUTO_ENABLE;
                    break;
                case "色调":
                    key = CWGeneralApi.PU_HUE;
                    auto = CWGeneralApi.getInstance().cwGetCameraParameter(CWGeneralApi.PU_HUE_AUTO) == CWApi.YC_AUTO_ENABLE;
                    break;
                case "饱和度":
                    key = CWGeneralApi.PU_SATURATION;
                    break;
                case "锐度":
                    key = CWGeneralApi.PU_SHARPNESS;
                    break;
                case "伽玛值":
                    key = CWGeneralApi.PU_GAMMA;
                    break;
                case "白平衡":
                    key = CWGeneralApi.PU_WB_TEMP;
                    auto = CWGeneralApi.getInstance().cwGetCameraParameter(CWGeneralApi.PU_WB_TEMP_AUTO) == CWApi.YC_AUTO_ENABLE;
                    break;
                case "逆光值":
                    key = CWGeneralApi.PU_BACKLIGHT;
                    break;
                case "增益":
                    key = CWGeneralApi.PU_GAIN;
                    break;
                case "倾斜":
                    key = CWGeneralApi.CTRL_PANTILT_ABS;
                    break;
                case "滚动":
                    key = CWGeneralApi.CTRL_ROLL_ABS;
                    break;
                case "缩放":
                    key = CWGeneralApi.CTRL_ZOOM_ABS;
                    break;
                case "曝光值":
                    key = CWGeneralApi.CTRL_AE_ABS;
                    auto = CWGeneralApi.getInstance().cwGetCameraParameter(CWGeneralApi.CTRL_AE) == CWApi.YC_AUTO_ENABLE;
                    break;
                case "光圈值":
                    key = CWGeneralApi.CTRL_IRIS_ABS;
                    break;
                case "焦点值":
                    key = CWGeneralApi.CTRL_FOCUS_ABS;
                    break;
            }
            int value = CWGeneralApi.getInstance().cwGetCameraParameter(key);
            paramsPairHashMap.put(name, new Pair<Boolean, Integer>(auto, value));
        }
        CameraParamsAdapter adapter = new CameraParamsAdapter(paramsPairHashMap);
        mGridViewCameraParams.setAdapter(adapter);
        setListViewHeightBasedOnChildren(mGridViewCameraParams);
    }

    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 2;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    class CameraParamsAdapter extends BaseAdapter {

        private LinkedHashMap<String, Pair<Boolean, Integer>> mParamsHashmap;

        public CameraParamsAdapter(LinkedHashMap<String, Pair<Boolean, Integer>> paramsPairHashMap) {
            this.mParamsHashmap = paramsPairHashMap;
        }

        public void setList(LinkedHashMap<String, Pair<Boolean, Integer>> paramsPairHashMap) {
            this.mParamsHashmap = paramsPairHashMap;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mParamsHashmap != null) {
                return mParamsHashmap.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return mParamsHashmap.get(mCameraParamsNameArray[position]);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_cameraparams_adapter, null);
                viewHolder.cameraParamsLaytout = convertView.findViewById(R.id.params_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.cameraParamsLaytout.setTitle(mCameraParamsNameArray[position]);
            if (mParamsHashmap != null) {
                viewHolder.cameraParamsLaytout.setValue(mParamsHashmap.get(mCameraParamsNameArray[position]).second);
                viewHolder.cameraParamsLaytout.setOpen(mParamsHashmap.get(mCameraParamsNameArray[position]).first);
            }
            return convertView;
        }

        class ViewHolder {
            CameraParamsLaytout cameraParamsLaytout;
        }
    }

    class CameraSizesAdapter extends BaseAdapter {

        private final List<Size> mSizeList;

        public CameraSizesAdapter(List<Size> sizes) {
            this.mSizeList = sizes;
        }

        @Override
        public int getCount() {
            if (mSizeList != null) {
                return mSizeList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner_item, null);
                viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvItem.setText(mSizeList.get(position).getWidth() + "x" + mSizeList.get(position).getHeight());
            return convertView;
        }

        class ViewHolder {
            TextView tvItem;
        }
    }
}
