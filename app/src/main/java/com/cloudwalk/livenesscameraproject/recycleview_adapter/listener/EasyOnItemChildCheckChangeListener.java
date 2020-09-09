package com.cloudwalk.livenesscameraproject.recycleview_adapter.listener;

import android.widget.CompoundButton;

/**
 * item点击事件回调
 */
public interface EasyOnItemChildCheckChangeListener {
    void onCheckedChanged(CompoundButton childView, int position, boolean isChecked);
}
