package com.cloudwalk.livenesscameraproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.locks.ReentrantLock;

public class RectView extends View {
    private Paint mPaint;
    private Context mContext;
    ReentrantLock mLock = new ReentrantLock();
    final int MAX_RECT_SIZE = 20;//上限个数
    Rect[] mRects = new Rect[MAX_RECT_SIZE];
    private int mWidth;
    private int mHeight;
    private int rectLeft;
    private int rectRight;
    private int rectTop;
    private int rectBottom;

    public RectView(Context context) {
        super(context);
    }

    public RectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3f);
        mPaint.setAlpha(180);


    }


    public void setRects(Rect rect) {
        try {
            mLock.lock();
            if (rect == null) {
                this.rectLeft = 0;
                this.rectTop = 0;
                this.rectRight = 0;
                this.rectBottom = 0;
            } else {
                this.rectLeft = rect.left;
                this.rectTop = rect.top;
                this.rectRight = rect.right;
                this.rectBottom = rect.bottom;
            }


//            int size = Math.min(mRects.length, rects.size());
//            for (int index = 0; index < mRects.length; ++index) {
//                if (index < size) {
//                    Rect rect = rects.get(index);
//                    Log.i("recthere", rect.toString());
//                    this.rectLeft = rect.left;
//                    this.rectTop = rect.top;
//                    this.rectRight = rect.right;
//                    this.rectBottom = rect.bottom;
//
////                    mRects[index].left = rect.left;
////                    mRects[index].top = rect.top;
////                    mRects[index].right = rect.right;
////                    mRects[index].bottom = rect.bottom;
//                } else {
//                    this.rectLeft = 0;
//                    this.rectTop = 0;
//                    this.rectRight = 0;
//                    this.rectBottom = 0;
////                    mRects[index].left = 0;
////                    mRects[index].top = 0;
////                    mRects[index].right = 0;
////                    mRects[index].bottom = 0;
//                }
//            }
        } finally {
            mLock.unlock();
        }
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.mWidth = w;
        this.mHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mWidth <= 0 || mHeight <= 0) {
            return;
        }
        try {
            mLock.lock();
//            if (mRects.length > 0) {
//                for (int i = 0; i < mRects.length; ++i) {
//                    Rect rect = mRects[i];
//                    if (rect.width() <= 0 || rect.height() <= 0) {
//                        break;
//                    }
//
//                    canvas.drawRect(rect, mPaint);
//                    invalidate();
//                }

            canvas.drawRect(new Rect(rectLeft, rectTop, rectRight, rectBottom), mPaint);
            invalidate();
//            }

        } finally {
            mLock.unlock();
        }


    }
}
