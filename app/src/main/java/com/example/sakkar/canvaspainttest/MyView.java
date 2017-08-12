package com.example.sakkar.canvaspainttest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class MyView extends View {

    float radius = 1;
    float height, width;
    Paint paint, paint2, paint3;
    boolean draw;
    Path path;

    public MyView(Context context) {
        super(context);
        initMyView();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyView();
    }

    public void initMyView() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint3 = new Paint();
        paint3.setColor(Color.RED);
        paint3.setFlags(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        paint2 = new Paint();
        paint2.setColor(Color.GREEN);
        paint2.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(3);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = getHeight();
                width = getWidth();
                radius = Math.min(height, width);
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(width / 2, height / 2, radius, paint2);
        canvas.drawCircle(width / 2, height / 2, Math.min(width / 2, height / 2), paint);
        if (draw)
            canvas.drawPath(path, paint3);
        super.onDraw(canvas);
    }

    void setColor(int color) {
        paint.setColor(color);
        postInvalidate();
    }


    public void startCustomAnimation() {
        setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        new Thread(new Runnable() {
            @Override
            public void run() {
                radius = Math.min(width / 2, height / 2);
                while (radius < Math.min(300 / 2, 300 / 2)) {
                    radius += 5;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
                bringCircle();
            }
        }).start();


        postInvalidate();
    }

    private void bringCircle() {
        draw = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                float y = 0f, x = (float) Math.abs(Math.sqrt(radius * radius - width / 2 * width / 2) + width / 2);
                postInvalidate();
                path.addCircle(x, y, 10, Path.Direction.CW);
                for (int i = 0; i < 36; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    path.reset();
                    x = (float) (width / 2 + (x - width / 2)*Math.cos(Math.PI/18) - (y-width / 2)*Math.sin(Math.PI/18));
                    y = (float) (width / 2 + (x - width / 2) *Math.sin(Math.PI/18) + (y-width / 2) *Math.cos(Math.PI/18));
                    path.addCircle(x, y, 10, Path.Direction.CW);
                    postInvalidate();
                }
            }
        }).start();
    }
}
