package com.example.sandeepdhull.hsvcolorwheel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by sandeepdhull on 04/02/15.
 */
public class HSLWheelView extends View {

    private Bitmap mColorWheelBitmap;
    private int width;
    private int height;
    private Paint mPaint;
    private Handler mHandler;
    private boolean mIsTaskFinished;

    public Point mCenter;
    public int mRadius;


    public HSLWheelView(Context context) {
        super(context);
        initialize();
    }

    public HSLWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public HSLWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        mHandler = new Handler();
        width = height = -1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        mIsTaskFinished = true;
//        Create bitmap of this size
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                createRenderScriptBitmap();
//            }
//        }).start();
        createRenderScriptBitmap();
    }

    private void createRenderScriptBitmap() {

        RenderScript rsCtx;
        ScriptC_file scriptC_file;

        rsCtx = RenderScript.create(getContext());
        scriptC_file = new ScriptC_file(rsCtx);

        scriptC_file.set_radius(width / 2);
        scriptC_file.set_centerX(width / 2);
        scriptC_file.set_centerY(height / 2);

        mCenter = new Point(width / 2,height/2);
        mRadius = width/2;

        Bitmap outBitmap;
        Allocation outAlloc;

        //  Create an output bitmap for our script and an Allocation to send
        //  it to the RenderScript
        outBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        outAlloc = Allocation.createFromBitmap(rsCtx, outBitmap);

        scriptC_file.forEach_root(outAlloc);
        outAlloc.copyTo(outBitmap);

        mIsTaskFinished = true;
        mColorWheelBitmap = outBitmap;
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                invalidate();
//            }
//        });
        rsCtx.destroy();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        No use of modes
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

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
        super.onDraw(canvas);
        if (mIsTaskFinished) {
            canvas.drawBitmap(mColorWheelBitmap, 0, 0, mPaint);
        }
    }

    private void createBitmap() {

        int[] image = new int[width * height];

        Point centerPoint = new Point(width / 2, height / 2);
        int radius = width / 2;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point point = new Point(x, y);
                int saturation = isPointInsideCircle(point, centerPoint, radius);

                if (saturation == -1) {
                    image[(width * y) + x] = Color.HSVToColor(0, new float[]{0.0f, 0.0f, 1.0f});
                    ;
                    continue;
                }

                float noramlizedSaturation = (float) saturation / radius;

                int hue = angleFromCenter(point, centerPoint);

                int pixelColor = Color.HSVToColor(new float[]{hue, noramlizedSaturation, 1.0f});

                image[(width * y) + x] = pixelColor;
            }
        }

        mColorWheelBitmap = Bitmap.createBitmap(image, width, height, Bitmap.Config.ARGB_8888);

        mIsTaskFinished = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }


    public int angleFromCenter(Point point, Point center) {
        int dx = point.x - center.x;
        int dy = point.y - center.y;

        double angleInRadians = Math.atan2(dy, dx);
        int angleInDegrees = (int) (angleInRadians * (180.0f / Math.PI));

        if (angleInDegrees < 0) {
            angleInDegrees = 360 + angleInDegrees;
        }
        return angleInDegrees;
    }

    public int isPointInsideCircle(Point point, Point center, int radius) {
        int dx = point.x - center.x;
        int dy = point.y - center.y;

        if (dx < 0) dx *= -1;
        if (dy < 0) dy *= -1;

        double distanceSquared = Math.pow(dx, 2) + Math.pow(dy, 2);
        double radiusSquared = Math.pow(radius, 2);

        if (distanceSquared <= radiusSquared) {
            int distance = (int) Math.sqrt(distanceSquared);
            if (distance < 0) distance *= -1;
            return distance;
        }
        return -1;
    }

    public Point getPoint(int color) {
        float hsv[] = new float[3];

        Color.colorToHSV(color, hsv);

        int hue = (int)hsv[0];
        int saturation = (int)hsv[1];
        int size = 0;
        if(width == -1) {
            size = getMeasuredWidth();
        } else {
            size = width;
        }

        int x = (int) ((size/2) + saturation * Math.cos(hue));
        int y = (int) ((size/2) + saturation * Math.sin(hue));

        Point point = new Point(x,y);
        return point;
    }


}
