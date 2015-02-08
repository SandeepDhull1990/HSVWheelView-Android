package com.example.sandeepdhull.hsvcolorwheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sandeepdhull on 08/02/15.
 */
public class ColorPickerThumbView extends View implements Drawable.Callback {

    private int mColor;
    GradientDrawable mDrawable;
    GradientDrawable mShadowDrawable;
    DrawFilter filter;

    public ColorPickerThumbView(Context context) {
        super(context);
        initialize();
    }

    public ColorPickerThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ColorPickerThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mColor = Color.WHITE;
        mDrawable = new GradientDrawable();
        mDrawable.setColor(Color.CYAN);
        mDrawable.setShape(GradientDrawable.OVAL);
        mDrawable.setStroke(3, Color.LTGRAY);

        mDrawable.setCallback(this);


        mShadowDrawable = new GradientDrawable();
        mShadowDrawable.setColor(Color.BLACK);
        mShadowDrawable.setShape(GradientDrawable.OVAL);
        mShadowDrawable.setAlpha(100);

        filter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = 0;

        if (width < height) {
            size = width;
        } else {
            size = height;
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(filter);

        mShadowDrawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        mShadowDrawable.draw(canvas);
        mDrawable.setBounds(0,0,canvas.getWidth() - 3,canvas.getHeight() - 3);
        mDrawable.draw(canvas);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        mDrawable.setColor(mColor);
        mDrawable.invalidateSelf();
    }
}
