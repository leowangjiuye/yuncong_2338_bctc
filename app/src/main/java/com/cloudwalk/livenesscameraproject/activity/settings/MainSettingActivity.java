package com.cloudwalk.livenesscameraproject.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudwalk.livenesscameraproject.BuildConfig;
import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;

import cn.cloudwalk.midware.engine.CWGeneralApi;

public class MainSettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvLivenessSettings, mTvConfigSettings, mTvCameraSettings, mTvPreviewSettings, mTvOtherSettings, mTvFeatureLibManage, mTvImageRegister, mTvFeatureSetting,mTvScanSetting;
    private ImageView mIvBack;
    private TextView mTvVersion;
    private TextView mTvLivenessCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        initListener();
    }

    private void initView() {
        mTvFeatureSetting = findViewById(R.id.tv_feature_setting);
        mTvLivenessSettings = (TextView) findViewById(R.id.tv_liveness_settings);
        mTvConfigSettings = (TextView) findViewById(R.id.tv_config_settings);
        mTvCameraSettings = (TextView) findViewById(R.id.tv_camera_settings);
        mTvPreviewSettings = (TextView)  findViewById(R.id.tv_preview_settings);
        mTvOtherSettings = (TextView) findViewById(R.id.tv_other_settings);
        mIvBack = (ImageView) findViewById(R.id.iv_set_back);
        mTvLivenessCheck = findViewById(R.id.tv_live_check_settings);
        mTvFeatureLibManage = findViewById(R.id.tv_feature_store_manage);
        mTvImageRegister = findViewById(R.id.tv_feature_image_register);
        mTvVersion = findViewById(R.id.tv_version);
        mTvScanSetting = findViewById(R.id.tv_scan_setting);
        mTvVersion.setText(TextUtils.isEmpty(CWGeneralApi.getInstance().cwGetVersion()) ? "版本号: 未初始化" : String.format("版本号: \n%1$s", CWGeneralApi.getInstance().cwGetVersion()));
        if (!"1".equals(BuildConfig.MIDWARE_1VN_ENABLE)) {
            mTvFeatureSetting.setVisibility(View.GONE);
            mTvImageRegister.setVisibility(View.GONE);
            mTvFeatureLibManage.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        mTvLivenessSettings.setOnClickListener(this);
        mTvCameraSettings.setOnClickListener(this);
        mTvPreviewSettings.setOnClickListener(this);
        mTvConfigSettings.setOnClickListener(this);
        mTvOtherSettings.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvLivenessCheck.setOnClickListener(this);
        mTvFeatureLibManage.setOnClickListener(this);
        mTvImageRegister.setOnClickListener(this);
        mTvFeatureSetting.setOnClickListener(this);
        mTvScanSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_liveness_settings:
                startActivity(LivenessParamSettingsActivity.class);
                break;
            case R.id.tv_camera_settings:
                startActivity(CameraConfigActivity.class);
                break;
            case R.id.tv_preview_settings:
                startActivity(RenderSettingsActivity.class);
                break;
            case R.id.tv_config_settings:
                startActivity(ConfigSettingsActivity.class);
                break;
            case R.id.tv_other_settings:
                startActivity(OtherFuncSettingsActivity.class);
                break;
            case R.id.tv_live_check_settings:
                startActivity(LivenessStrategyParamsActivity.class);
                break;
            case R.id.iv_set_back:
                finish();
                break;
            case R.id.tv_feature_store_manage:
                startActivity(FaceLibraryListActivity.class);
                finish();
                break;
            case R.id.tv_feature_image_register:
                startActivity(ImageRegisterActivity.class);
                finish();
                break;
            case R.id.tv_feature_setting:
                startActivity(FeatureSettingsActivity.class);
                finish();
                break;
            case R.id.tv_scan_setting:
                startActivity(ScanSettingActivity.class);
                break;
        }
    }

    private void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
