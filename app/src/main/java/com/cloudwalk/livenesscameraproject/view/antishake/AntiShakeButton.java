package com.cloudwalk.livenesscameraproject.view.antishake;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


/**
 * Created by guyj on 2019/1/29.
 * 描述:
 */

public class AntiShakeButton extends AppCompatButton {

    private long lastClickTime = 0L;
    private long currentClickTime = 0L;
    private final long skipTime = 500L;

    public AntiShakeButton(Context context) {
        super(context);
    }

    public AntiShakeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AntiShakeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime > skipTime) {// 两次点击的时间间隔大于最小限制时间，则触发点击事件
            lastClickTime = currentClickTime;
            return super.performClick();
        } else {// 否则，拦截继续下发的事件
            return false;
        }
    }
}
