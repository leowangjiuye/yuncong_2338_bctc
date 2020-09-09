package com.cloudwalk.livenesscameraproject.activity.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;

public class FeatureSettingsActivity extends BaseActivity implements View.OnClickListener {

    private RadioGroup mRGFeatureImageType;
    private EditText mEtFaceThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_settings);
        mRGFeatureImageType = findViewById(R.id.rg_get_feature_image_type);
        mEtFaceThreshold = findViewById(R.id.edtFaceThreshold);
        mRGFeatureImageType.check(CacheManager.getInstance().loadFeatureImageGetType(1) == 1 ? R.id.rb_live : R.id.rb_face);
        mEtFaceThreshold.setText(CacheManager.getInstance().loadFeatureThreshold(0.8f) + "");
    }


    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSave) {
            float featureThreshold;
            try {
                featureThreshold = Float.parseFloat(mEtFaceThreshold.getText() + "");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "输入值格式错误", Toast.LENGTH_SHORT).show();
                return;
            }
            CacheManager.getInstance().saveFeatureThreshold(featureThreshold);
            switch (mRGFeatureImageType.getCheckedRadioButtonId()) {
                case R.id.rb_live:
                    CacheManager.getInstance().saveFeatureImageGetType(1);
                    break;
                case R.id.rb_face:
                    CacheManager.getInstance().saveFeatureImageGetType(0);
                    break;
            }
            finish();
        }
    }
}
