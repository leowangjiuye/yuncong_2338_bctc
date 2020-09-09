package com.cloudwalk.livenesscameraproject.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwalk.livenesscameraproject.R;

public class CameraParamsLaytout extends LinearLayout {
    private Context mContext;
    private View mLayout;
    private TextView tvTitle;
    private SlideButton sbIsAuto;
    private TextView etValue;

    public CameraParamsLaytout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CameraParamsLaytout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CameraParamsLaytout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraParamsLaytout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    private void initView() {
        mLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_camera_params_view, this);
        tvTitle = (TextView) mLayout.findViewById(R.id.tv_title);
        sbIsAuto = (SlideButton) mLayout.findViewById(R.id.sb_is_auto);
        sbIsAuto.setOnSlideButtonChangeListener(new SlideButton.OnSlideButtonChangeListener() {
            @Override
            public void onButtonChange(SlideButton view, boolean isOpen) {
                if (isOpen) {
                    etValue.setEnabled(false);
                } else {
                    etValue.setEnabled(true);
                }
            }
        });
        etValue = (TextView) mLayout.findViewById(R.id.et_value);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setOpen(boolean isOpen) {
        if (isOpen) {
            etValue.setEnabled(false);
        } else {
            etValue.setEnabled(true);
        }
        sbIsAuto.setOpen(isOpen);
    }

    public void setValue(int value) {
        etValue.setText(value + "");
    }

    public String getTitle() {
        return tvTitle.getText().toString();
    }

    public boolean getOpen() {
        return sbIsAuto.isOpen();
    }

    public int getValue() {
        return Integer.parseInt(etValue.getText().toString());
    }

}
