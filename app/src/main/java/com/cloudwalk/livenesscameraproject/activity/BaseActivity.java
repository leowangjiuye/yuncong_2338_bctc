package com.cloudwalk.livenesscameraproject.activity;

import android.content.Context;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cloudwalk.livenesscameraproject.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }

        if(ev.getAction()==MotionEvent.ACTION_DOWN)
        {
            View view = getCurrentFocus();
            boolean tmp = true;

            //判断点击区域是否在控件内部，不在就隐藏，在就显示
            if(view!=null&&view instanceof EditText)
            {
                int [] outLocation = {0,0};
                view.getLocationInWindow(outLocation);
                int i = outLocation[0] + view.getHeight();
                int j = outLocation[1] + view.getWidth();

                float touchX = ev.getX();
                float touchY = ev.getY();

                if(touchX>outLocation[0]&&touchX<j&&touchY>outLocation[1]&&touchY<i)
                    tmp = false;
                else
                    tmp = true;
            }
            else
                tmp = true;

            if(view!=null&&tmp)
            {
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager !=null)
                {
                    inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(),0);
                }
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
