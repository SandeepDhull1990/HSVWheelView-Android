package com.example.sandeepdhull.hsvcolorwheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sandeepdhull on 08/02/15.
 */
public class ColorPickerView extends ViewGroup{

    private HSLWheelView mWheeView;
    private ColorPickerThumbView mThumbView;

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

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        Step 1 :
//        Iterate over all children measuring their size

        int childCount = getChildCount();

        for(int i = 0 ; i < childCount ; i ++) {

            View v = getChildAt(i);

            measureChild(v,widthMeasureSpec,heightMeasureSpec);

        }

        setMeasuredDimension(mWheeView.getMeasuredWidth(), mWheeView.getMeasuredHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0 ; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.layout(20,20,r - l - 40 ,b - t - 40);
        }
    }
}
