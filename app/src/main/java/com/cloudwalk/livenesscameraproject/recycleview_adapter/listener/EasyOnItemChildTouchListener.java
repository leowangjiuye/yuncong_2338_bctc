package com.cloudwalk.livenesscameraproject.recycleview_adapter.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * item触摸事件回调
 */
public interface EasyOnItemChildTouchListener {
    boolean onTouch(View v, MotionEvent event, int position);
}
