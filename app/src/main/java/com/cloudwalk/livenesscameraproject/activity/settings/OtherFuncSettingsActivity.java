package com.cloudwalk.livenesscameraproject.activity.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.view.SlideButton;

public class OtherFuncSettingsActivity extends BaseActivity {

    private static final String TAG = "OtherFuncSettingsActivity";
    private TextView mTvTrackPreviewWidth, mTvTrackPreviewHeight;
    private SlideButton mSBtnIsOpenFaceTrack;
    private boolean isOpenFaceTrack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_para_set);
        initView();
        getParams();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        ImageView imageView = findViewById(R.id.iv_other_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView mBtnSave = findViewById(R.id.tv_other_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CacheManager.getInstance().saveTrackPreviewWidth(Integer.parseInt(mTvTrackPreviewWidth.getText()+""));
                    CacheManager.getInstance().saveTrackPreviewHeight(Integer.parseInt(mTvTrackPreviewHeight.getText()+""));
                }catch (NumberFormatException e) {
                    Toast.makeText(OtherFuncSettingsActivity.this, "输入值格式不符",
                        Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                CacheManager.getInstance().saveIsOpenFaceTrack(mSBtnIsOpenFaceTrack.isOpen());
                finish();
            }
        });

        mTvTrackPreviewWidth = findViewById(R.id.tv_track_preview_width);
        mTvTrackPreviewHeight = findViewById(R.id.tv_track_preview_height);
        mSBtnIsOpenFaceTrack = findViewById(R.id.sb_is_open_face_track);


    }

    private void getParams() {
        boolean isInit = CacheManager.getInstance().loadIsOtherSettingsInit(false);
        if (isInit) {
            //已经初始化了
            isOpenFaceTrack = CacheManager.getInstance().loadIsOpenFaceTrack(mSBtnIsOpenFaceTrack.isOpen());
            int mTrackPreviewWidth = CacheManager.getInstance().loadTrackPreviewWidth(640);
            int mTrackPreviewHeight = CacheManager.getInstance().loadTrackPreviewHeight(480);

            mTvTrackPreviewWidth.setText(mTrackPreviewWidth + "");
            mTvTrackPreviewHeight.setText(mTrackPreviewHeight + "");
            mSBtnIsOpenFaceTrack.setOpen(isOpenFaceTrack);
        } else {
            //没有初始化了
            CacheManager.getInstance().saveIsOpenFaceTrack(false);
            CacheManager.getInstance().saveTrackPreviewWidth(640);
            CacheManager.getInstance().saveTrackPreviewHeight(480);
            CacheManager.getInstance().saveIsOtherSettingsInit(true);

            mTvTrackPreviewWidth.setText("640");
            mTvTrackPreviewHeight.setText("480");
            mSBtnIsOpenFaceTrack.setOpen(false);
        }


    }
}
