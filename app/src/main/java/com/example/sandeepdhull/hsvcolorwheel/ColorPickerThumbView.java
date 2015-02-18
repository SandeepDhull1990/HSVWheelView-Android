package com.example.sandeepdhull.hsvcolorwheel;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
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
        mDrawable.setColor(Color.WHITE);
        mDrawable.setShape(GradientDrawable.OVAL);
        mDrawable.setStroke(2, Color.LTGRAY);
        mDrawable.setCallback(this);


        mShadowDrawable = new GradientDrawable();
        mShadowDrawable.setColor(Color.BLACK);
        mShadowDrawable.setShape(GradientDrawable.OVAL);
        mShadowDrawable.setAlpha(50);

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
        mDrawable.setBounds(2,2,canvas.getWidth() - 2,canvas.getHeight() - 2);
        mDrawable.draw(canvas);
    }

    public int getColor() {
        return mColor;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setColor(int mColor) {
        this.mColor = mColor;
        mDrawable.setColor(mColor);
        mDrawable.setColorFilter(mColor, PorterDuff.Mode.OVERLAY);
        mDrawable.invalidateSelf();
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        invalidate();
    }
}
