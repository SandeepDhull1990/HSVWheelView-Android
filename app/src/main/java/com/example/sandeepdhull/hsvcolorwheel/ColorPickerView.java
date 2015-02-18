package com.example.sandeepdhull.hsvcolorwheel;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;

/**
 * Created by sandeepdhull on 08/02/15.
 */
public class ColorPickerView extends ViewGroup{

    private HSLWheelView mWheeView;
    private ColorPickerThumbView mThumbView;
    private int mColor;


    private Rect mWheelViewRect;
    private Rect mThumbViewRect;

    private Point mThumbLocation;

    private boolean mShouldThumbMove;

    public ColorPickerView(Context context) {
        super(context);
        initialize();
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mWheeView = new HSLWheelView(getContext());

        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        mWheeView.setLayoutParams(params);

        addView(mWheeView);

        mThumbView = new ColorPickerThumbView(getContext());

        ViewGroup.LayoutParams params2 = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.colorpicker_thumb_radius),getResources().getDimensionPixelSize(R.dimen.colorpicker_thumb_radius));

        mThumbView.setLayoutParams(params2);

        addView(mThumbView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = getChildCount();

        for(int i = 0 ; i < childCount ; i ++) {

            View v = getChildAt(i);

            measureChild(v,widthMeasureSpec,heightMeasureSpec);

        }
        setMeasuredDimension(mWheeView.getMeasuredWidth(), mWheeView.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

            double scaledWidth = mThumbView.getMeasuredWidth() * 1.5;
            double scaledHeight = mThumbView.getMeasuredHeight() * 1.5;

            mWheelViewRect = new Rect((int)(scaledWidth/2),
                    (int)(scaledHeight/2),
                    mWheeView.getMeasuredWidth() - (int)(scaledWidth/2),
                    mWheeView.getMeasuredHeight() - (int)(scaledHeight/2));
            mWheeView.layout(mWheelViewRect.left,mWheelViewRect.top,mWheelViewRect.right,mWheelViewRect.bottom);

            mThumbViewRect = new Rect((r - l)/2 - mThumbView.getMeasuredWidth()/2 ,
                    (b - t)/2 - mThumbView.getMeasuredHeight()/2,
                    (r - l)/2 + mThumbView.getMeasuredWidth()/2,
                    (b - t)/2 + mThumbView.getMeasuredHeight()/2);

            mThumbView.layout( mThumbViewRect.left, mThumbViewRect.top, mThumbViewRect.right,mThumbViewRect.bottom);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(Color color) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Point touchPoint = new Point((int)event.getX() - mWheelViewRect.left,(int)event.getY() - mWheelViewRect.top);

        int dx = (int) ((mThumbView.getX() + mThumbView.getWidth()/2 ) - event.getX());
        int dy = (int) ((mThumbView.getY() + mThumbView.getHeight()/2)- event.getY());


        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                int saturation = mWheeView.isPointInsideCircle(touchPoint,mWheeView.mCenter, mWheeView.mRadius);
                int angle = 0;
                if(saturation == -1) {
                    return false;
                } else {
                    angle = mWheeView.angleFromCenter(touchPoint,mWheeView.mCenter);
                    mShouldThumbMove = true;
                    mThumbView.animate().translationYBy(-dy).translationXBy(-dx).setDuration(100).setInterpolator(new LinearInterpolator()).scaleX(1.5f).scaleY(1.5f);
                    float noramlizedSaturation = (float) saturation / mWheeView.mRadius;
                    this.mColor = Color.HSVToColor(new float[]{(float)angle,noramlizedSaturation,1.0f});
                    mThumbView.setColor(mColor);
                    return true;
                }

            case MotionEvent.ACTION_MOVE:
                saturation = mWheeView.isPointInsideCircle(touchPoint,mWheeView.mCenter, mWheeView.mRadius);
                angle = 0;
                if(saturation == -1) {
//                get the touch location, if outside then get the angle and set the radius
                    saturation = mWheeView.mRadius;
                    Point point = new Point(getWidth()/2,getHeight()/2);
                    angle = mWheeView.angleFromCenter(touchPoint,mWheeView.mCenter);

                    int x = (int) (getWidth()/2 + mWheeView.mRadius * Math.cos(Math.toRadians(angle)));
                    int y = (int) (getHeight()/2 + mWheeView.mRadius * Math.sin(Math.toRadians(angle)));

//                    recalculate deviation
                    dx = (int) ((mThumbView.getX() + mThumbView.getWidth()/2 ) - x);
                    dy = (int) ((mThumbView.getY() + mThumbView.getHeight()/2)- y);

                    mThumbView.animate().translationYBy(-dy).translationXBy(-dx).setDuration(0).setInterpolator(new LinearInterpolator());
                } else {
                    angle = mWheeView.angleFromCenter(touchPoint,mWheeView.mCenter);
                    mThumbView.animate().translationYBy(-dy).translationXBy(-dx).setDuration(0).setInterpolator(new LinearInterpolator());
                }
                float normalizedSaturation = (float) saturation / mWheeView.mRadius;
                this.mColor = Color.HSVToColor(new float[]{(float)angle,normalizedSaturation,1.0f});
                mThumbView.setColor(mColor);
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mThumbView.animate().setDuration(150).setInterpolator(new LinearInterpolator()).scaleX(1.0f).scaleY(1.0f);
                return false;
            default:
                return false;

        }




    }
}
