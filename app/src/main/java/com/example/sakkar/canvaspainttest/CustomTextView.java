package com.example.sakkar.canvaspainttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CustomTextView extends View implements View.OnTouchListener {
    private Paint textPaint;
    private Paint borderPaint;
    private Paint shadePaint;

    private String text = "text";

    private Rect bounds;
    private RectF circularBorder;

    private int strokeWidth = 2,position;

    private int clickedTextColor, textColor, notClickedTextColor;
    private int shaderColor, shaderNotClickedColor, shaderClickedColor;
    private int borderColor, borderClickedColor, borderNotClickedColor;


    private int heightPadding, widthPadding;
    private boolean pressed;
    private int mHeight, mWidth;
    private float mTextSize;
    private SelectStateListener listener;

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    void init(@Nullable AttributeSet attrs) {

        mTextSize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22, getResources().getDisplayMetrics());

        borderPaint = getPaint(true,Paint.Style.STROKE);
        borderPaint.setStrokeWidth(strokeWidth);
        shadePaint = getPaint(true,Paint.Style.FILL);
        textPaint = getPaint(true,Paint.Style.FILL);
        textPaint.setTextSize(mTextSize);

        heightPadding = getValuePx(8);
        widthPadding = getValuePx(16);

        bounds = new Rect();

        notClickedTextColor = Color.BLACK;
        clickedTextColor = Color.WHITE;

        shaderClickedColor = Color.BLACK;
        shaderNotClickedColor = Color.TRANSPARENT;

        borderClickedColor = Color.BLACK;
        borderNotClickedColor = Color.BLACK;

        setContentSize();
        setContentColors();

        setOnTouchListener(this);

    }

    private Paint getPaint(boolean isAntiAlias, Paint.Style fill) {
        Paint p=new Paint();
        p.setAntiAlias(isAntiAlias);
        p.setStyle(fill);
        return p;
    }

    private void setContentColors() {
        if (pressed) {
            textColor = clickedTextColor;
            shaderColor = shaderClickedColor;
            borderColor = borderClickedColor;
        } else {
            textColor = notClickedTextColor;
            shaderColor = shaderNotClickedColor;
            borderColor = borderNotClickedColor;
        }

        borderPaint.setColor(borderColor);
        shadePaint.setColor(shaderColor);
        textPaint.setColor(textColor);
    }

    private void setContentSize() {
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        circularBorder = new RectF(2, 2, bounds.width() + 2 * widthPadding,
                textPaint.descent()-textPaint.ascent() + 2 * heightPadding);
        mHeight = (int) circularBorder.height() + 6;
        mWidth = (int) circularBorder.width() + 6;
    }

    int getValuePx(int x) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(circularBorder, (widthPadding>heightPadding?widthPadding:heightPadding),
                (widthPadding>heightPadding?widthPadding:heightPadding), shadePaint);
        canvas.drawRoundRect(circularBorder, (widthPadding>heightPadding?widthPadding:heightPadding),
                (widthPadding>heightPadding?widthPadding:heightPadding), borderPaint);
        canvas.drawText(text, mWidth/2-bounds.width()/2,mHeight/2+(textPaint.descent()-textPaint.ascent())*.3f, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    public void setText(String text) {
        this.text = text;
        setContentSize();
        postInvalidate();
    }

    public int getmHeight() {
        return mHeight;
    }

    public int getmWidth() {
        return mWidth;
    }

    @Override
    public boolean isPressed() {
        return pressed;
    }

    @Override
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
        setContentColors();
        postInvalidate();
    }

    void addListener(SelectStateListener listener) {
        this.listener = listener;
    }

    void setClickedTextColor(int clickedTextColor) {
        this.clickedTextColor = clickedTextColor;
        setContentColors();
    }

    void setNotClickedTextColor(int notClickedTextColor) {
        this.notClickedTextColor = notClickedTextColor;
        setContentColors();
    }

    void setShaderNotClickedColor(int shaderNotClickedColor) {
        this.shaderNotClickedColor = shaderNotClickedColor;
        setContentColors();
    }

    void setShaderClickedColor(int shaderClickedColor) {
        this.shaderClickedColor = shaderClickedColor;
        setContentColors();
    }

    void setBorderClickedColor(int borderClickedColor) {
        this.borderClickedColor = borderClickedColor;
        setContentColors();
    }

    void setBorderNotClickedColor(int borderNotClickedColor) {
        this.borderNotClickedColor = borderNotClickedColor;
        setContentColors();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public void setShaderColor(int shaderColor) {
        this.shaderColor = shaderColor;
        postInvalidate();;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        postInvalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        borderPaint.setStrokeWidth(strokeWidth);
    }

    public void setTextSize(float textSize) {
        if(textSize!=0) {
            textPaint.setTextSize(textSize);
            setContentSize();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressed = !pressed;
                setContentColors();
                if (listener != null)
                    listener.onSelect(CustomTextView.this);
                break;
        }
        postInvalidate();
        return true;

    }


    interface SelectStateListener{
        void onSelect(CustomTextView textView);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public SelectStateListener getListener() {
        return listener;
    }

    public void setHeightPadding(int heightPadding) {
        this.heightPadding = heightPadding;
        setContentSize();
    }

    public void setWidthPadding(int widthPadding) {
        this.widthPadding = widthPadding;
        setContentSize();
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        textPaint.setTextSize(mTextSize);
        setContentSize();
    }
}
