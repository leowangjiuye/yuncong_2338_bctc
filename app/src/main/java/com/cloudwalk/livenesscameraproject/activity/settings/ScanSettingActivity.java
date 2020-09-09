package com.cloudwalk.livenesscameraproject.activity.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.BaseActivity;
import com.cloudwalk.livenesscameraproject.manager.CacheManager;
import com.cloudwalk.livenesscameraproject.view.SlideButton;

public class ScanSettingActivity extends BaseActivity {

    private SlideButton mSlideBtnIsOpenScan;
    private ImageView mIvScan;
    private TextView mTvScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_setting);
        mSlideBtnIsOpenScan = findViewById(R.id.sbtn_is_open_scan);
        mIvScan = findViewById(R.id.iv_scan_back);
        mTvScan = findViewById(R.id.tv_scan_save);

        mIvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheManager.getInstance().saveIsOpenScanner(mSlideBtnIsOpenScan.isOpen());
                finish();
            }
        });

        boolean isOpenScanner = CacheManager.getInstance().loadIsOpenScanner(false);
        mSlideBtnIsOpenScan.setOpen(isOpenScanner);
    }
}
