package com.cloudwalk.livenesscameraproject.activity.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwalk.livenesscameraproject.BuildConfig;
import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.view.SlideButton;

import static com.cloudwalk.livenesscameraproject.constants.Constants.SP_KEY_LIVENESS_MODEL_PATH;


public class ConfigSettingsActivity extends BaseActivity {

    private SlideButton mSlideButtonBgImg;
    private SlideButton mSlideButtonImageSave;
    private ConfigSettingsActivity mContext;
    private EditText mTvLivenessModelPath, mTvBgImagePath, mTvImageSavePath, mTvFaceDetModelPath,
            mTvKeyFacePointModelPath, mTvKeyTrackModelPath, mTvQualityModelPath;

    private TextView mTvSetBgImagePath, mTvSetImageSavePath, mTvSetLivenessModelPath,
            mTvSetFaceDetModelPath, mTvSetKeyFacePointModelPath, mTvSetKeyTrackModelPath, mTvsetQualityModelPath;
    private EditText mTvSetConfigFilePath;
    private TextView mTEtSetConfigFilePath;
    private EditText mTvConfigFilePath;
    private TextView mTvtSetConfigFilePath;
    private SlideButton mSlideButtonIsMultiThread;
    private EditText mEtBestFaceRatio;
    private RadioGroup mRGSaveFormat;
    private SlideButton mSlideButtonFaceMask;
    private EditText mTvFaceMaskSavePath;
    private TextView mTvSetFaceMaskSavePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_para_set);
        mContext = this;
        initView();
        initParams();
    }

    private void initView() {
        ImageView imageView = findViewById(R.id.iv_config_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mBtnSave = findViewById(R.id.tv_config_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CacheManager.getInstance().saveIsShowPreviewBgImg(mSlideButtonBgImg.isOpen());
                CacheManager.getInstance().saveIsSaveDebugImage(mSlideButtonImageSave.isOpen());
                CacheManager.getInstance().saveIsMultiThread(mSlideButtonIsMultiThread.isOpen());
                CacheManager.getInstance().savePreviewImagePath(mTvBgImagePath.getText().toString());
                CacheManager.getInstance().saveDebugImagePath(mTvImageSavePath.getText().toString());
                CacheManager.getInstance().saveFaceMaskImagePath(mTvFaceMaskSavePath.getText().toString());
                CacheManager.getInstance().saveConfigFilePath(mTvConfigFilePath.getText().toString());
                CacheManager.getInstance().saveFaceDetModelPath(mTvLivenessModelPath.getText().toString());
                CacheManager.getInstance().saveKeyFacePointModelPath(mTvFaceDetModelPath.getText().toString());
                CacheManager.getInstance().saveFaceQualityFile(mTvKeyFacePointModelPath.getText().toString());
                CacheManager.getInstance().saveFaceLivenessFile(mTvKeyTrackModelPath.getText().toString());
                CacheManager.getInstance().saveFaceRecogFile(mTvQualityModelPath.getText().toString());
                CacheManager.getInstance().saveDefineSaveFormat(mRGSaveFormat.getCheckedRadioButtonId() == R.id.rb_png ? "png" : "jpg");
                CacheManager.getInstance().saveIsShowFaceMask(mSlideButtonFaceMask.isOpen());

                try {
                    CacheManager.getInstance().saveBestFaceRatio(Float.parseFloat(mEtBestFaceRatio.getText() + ""));
                } catch (NumberFormatException e) {
                    Toast.makeText(ConfigSettingsActivity.this, "最佳人脸占比输入格式不符", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConfigSettingsActivity.this.finish();
            }
        });
        mSlideButtonFaceMask = (SlideButton) findViewById(R.id.sb_is_open_face_mask);
        mSlideButtonBgImg = (SlideButton) findViewById(R.id.sb_is_backpic);

        mSlideButtonImageSave = (SlideButton) findViewById(R.id.sb_is_liveness_image_save);

        mTvBgImagePath = (EditText) findViewById(R.id.tv_background_image_path);
        mTvSetBgImagePath = findViewById(R.id.tv_set_bg_image);
        mTvSetBgImagePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "previewImagePath");
                startActivityForResult(intent, 5);
            }
        });
        mTvImageSavePath = (EditText) findViewById(R.id.tv_image_save_path);
        mTvSetImageSavePath = (TextView) findViewById(R.id.tv_set_image_save_path);
        mTvSetImageSavePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "debugImagePath");
                startActivityForResult(intent, 6);
            }
        });

        mTvFaceMaskSavePath = (EditText) findViewById(R.id.tv_face_mask_save_path);
        mTvSetFaceMaskSavePath = (TextView) findViewById(R.id.tv_set_face_mask_save_path);
        mTvSetFaceMaskSavePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "faceMaskImagePath");
                startActivityForResult(intent, 8);
            }
        });
        mTvConfigFilePath = (EditText) findViewById(R.id.tv_config_file_path);
        mTvtSetConfigFilePath = (TextView) findViewById(R.id.tv_set_config_file);
        mTvtSetConfigFilePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "configFilePath");
                startActivityForResult(intent, 7);
            }
        });

        mTvLivenessModelPath = (EditText) findViewById(R.id.tv_liveness_model_path);
        mTvSetLivenessModelPath = (TextView) findViewById(R.id.tv_set_liveness_model);
        mTvSetLivenessModelPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", SP_KEY_LIVENESS_MODEL_PATH);
                startActivityForResult(intent, 0);
            }
        });
        mTvFaceDetModelPath = (EditText) findViewById(R.id.tv_det_model_path);
        mTvSetFaceDetModelPath = (TextView) findViewById(R.id.tv_set_face_det_model);
        mTvSetFaceDetModelPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "faceDetModelPath");
                startActivityForResult(intent, 1);
            }
        });
        mTvKeyFacePointModelPath = (EditText) findViewById(R.id.tv_key_face_det_model_path);
        mTvSetKeyFacePointModelPath = (TextView) findViewById(R.id.tv_set_key_point_model);
        mTvSetKeyFacePointModelPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "keyFacePointModelPath");
                startActivityForResult(intent, 2);
            }
        });
        mTvKeyTrackModelPath = (EditText) findViewById(R.id.tv_key_track_model_path);
        mTvSetKeyTrackModelPath = (TextView) findViewById(R.id.tv_set_key_track_model);
        mTvSetKeyTrackModelPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "faceQualityFile");
                startActivityForResult(intent, 3);
            }
        });
        mTvQualityModelPath = (EditText) findViewById(R.id.et_quality_model_path);
        mTvsetQualityModelPath = (TextView) findViewById(R.id.tv_set_quality_model);
        mTvsetQualityModelPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectFolderActivity.class);
                intent.putExtra("SP_KEY", "faceLivenessFile");
                startActivityForResult(intent, 4);
            }
        });

        mSlideButtonIsMultiThread = (SlideButton) findViewById(R.id.sb_is_multitread);
        mEtBestFaceRatio = (EditText) findViewById(R.id.et_face_best_ratio);
        mRGSaveFormat = (RadioGroup) findViewById(R.id.rg_save_format);
        if (!"1".equals(BuildConfig.MIDWARE_1VN_ENABLE)) {
            findViewById(R.id.ll_feature).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initParams() {
        boolean isConfigInit = CacheManager.getInstance().loadIsConfigInit(true);
        if (isConfigInit) {
            CacheManager.getInstance().saveIsShowPreviewBgImg(true);
            CacheManager.getInstance().saveIsSaveDebugImage(false);
            CacheManager.getInstance().saveIsShowFaceMask(false);
            CacheManager.getInstance().saveIsMultiThread(true);
            CacheManager.getInstance().savePreviewImagePath("");
            CacheManager.getInstance().saveDebugImagePath("");
            CacheManager.getInstance().saveFaceMaskImagePath("");
            CacheManager.getInstance().saveConfigFilePath("");
            CacheManager.getInstance().saveFaceRecogFile("");
            CacheManager.getInstance().saveFaceDetModelPath("");
            CacheManager.getInstance().saveKeyFacePointModelPath("");
            CacheManager.getInstance().saveFaceQualityFile("");
            CacheManager.getInstance().saveFaceLivenessFile("");
            CacheManager.getInstance().saveBestFaceRatio(1.5f);
            CacheManager.getInstance().saveDefineSaveFormat("jpg");

            mSlideButtonBgImg.setOpen(true);
            mSlideButtonImageSave.setOpen(false);
            mSlideButtonFaceMask.setOpen(false);
            mSlideButtonIsMultiThread.setOpen(true);
            mTvBgImagePath.setText("");
            mTvImageSavePath.setText("");
            mTvFaceMaskSavePath.setText("");
            mTvConfigFilePath.setText("");
            mTvLivenessModelPath.setText("");
            mTvFaceDetModelPath.setText("");
            mTvKeyFacePointModelPath.setText("");
            mTvKeyTrackModelPath.setText("");
            mTvQualityModelPath.setText("");
            mEtBestFaceRatio.setText(1.5f + "");
            mRGSaveFormat.check(R.id.rb_jpg);

            CacheManager.getInstance().saveIsConfigInit(false);
        } else {
            mSlideButtonBgImg.setOpen(CacheManager.getInstance().loadIsShowPreviewBgImg(true));
            mSlideButtonImageSave.setOpen(CacheManager.getInstance().loadIsSaveDebugImage(false));
            mSlideButtonIsMultiThread.setOpen(CacheManager.getInstance().loadIsMultiThread(true));
            mSlideButtonFaceMask.setOpen(CacheManager.getInstance().loadIsShowFaceMask(false));
            mTvBgImagePath.setText(CacheManager.getInstance().loadPreviewImagePath(""));
            mTvImageSavePath.setText(CacheManager.getInstance().loadDebugImagePath(""));
            mTvFaceMaskSavePath.setText(CacheManager.getInstance().loadFaceMaskImagePath(""));
            mTvConfigFilePath.setText(CacheManager.getInstance().loadConfigFilePath(""));
            mTvLivenessModelPath.setText(CacheManager.getInstance().loadFaceDetModelPath(""));
            mTvFaceDetModelPath.setText(CacheManager.getInstance().loadKeyFacePointModelPath(""));
            mTvKeyFacePointModelPath.setText(CacheManager.getInstance().loadFaceQualityFile(""));//keyFacePointModelPath
            mTvKeyTrackModelPath.setText(CacheManager.getInstance().loadFaceLivenessFile(""));
            mTvQualityModelPath.setText(CacheManager.getInstance().loadFaceRecogFile(""));
            mEtBestFaceRatio.setText(CacheManager.getInstance().loadBestFaceRatio(1.5f) + "");
            mRGSaveFormat.check(TextUtils.equals("png" , CacheManager.getInstance().loadDefineSaveFormat("jpg")) ? R.id.rb_png : R.id.rb_jpg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                mTvLivenessModelPath.setText(data.getStringExtra("path"));
            } else if (requestCode == 1) {
                mTvFaceDetModelPath.setText(data.getStringExtra("path"));
            } else if (requestCode == 2) {
                mTvKeyFacePointModelPath.setText(data.getStringExtra("path"));
            } else if (requestCode == 3) {
                mTvKeyTrackModelPath.setText(data.getStringExtra("path"));
            } else if (requestCode == 4) {
                mTvQualityModelPath.setText(data.getStringExtra("path"));
            } else if (requestCode == 5) {
                mTvBgImagePath.setText(data.getStringExtra("path"));
            } else if (requestCode == 6) {
                mTvImageSavePath.setText(data.getStringExtra("path"));
            } else if (requestCode == 7) {
                mTvConfigFilePath.setText(data.getStringExtra("path"));
            } else if (requestCode == 8) {
                mTvFaceMaskSavePath.setText(data.getStringExtra("path"));
            }

        }
    }
}
