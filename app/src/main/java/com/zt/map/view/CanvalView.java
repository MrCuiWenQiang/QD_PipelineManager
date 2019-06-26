package com.zt.map.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zt.map.R;

/**
 * 百度地图上触摸划线View 现只支持单线
 */

public class CanvalView extends View implements View.OnTouchListener {

    private Paint mPaint;

    private float start_X;
    private float start_y;

    private float end_X;
    private float end_y;


    private onDrawListener onDrawListener;

    private boolean isCanvas = true;//是否开启画线模式



    public CanvalView(Context context) {
        this(context, null);
    }

    public CanvalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.qmui_config_color_red));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(start_X, start_y, end_X, end_y, mPaint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (isCanvas ) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    start_X = motionEvent.getX();
                    start_y = motionEvent.getY();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    drawLin(motionEvent);
                    if (onDrawListener != null) {
                        onDrawListener.onMoven(start_X, start_y, motionEvent);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if (onDrawListener != null) {
                        onDrawListener.onDrawLineFinal(start_X, start_y, motionEvent);
                    }
                    break;
                }
            }
        }
        return isCanvas ;
    }

    private void drawLin(MotionEvent end_event) {
        end_X = end_event.getX();
        end_y = end_event.getY();
        this.invalidate();
    }


    public void clean() {
        start_X = 0;
        start_y = 0;
        end_X = 0;
        end_y = 0;
        this.invalidate();
    }

    public void setOnDrawListener(CanvalView.onDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    public void setCanvas(boolean canvas) {
        isCanvas = canvas;
    }


    public interface onDrawListener {
        void onDrawLineFinal(float start_x, float start_y, MotionEvent end_Event);

        /**
         * 移动划线
         * @param start_x
         * @param start_y
         * @param moven_Event
         */
        void onMoven(float start_x, float start_y, MotionEvent moven_Event);
    }
}
