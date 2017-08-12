package com.example.sakkar.canvaspainttest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class MyLayout extends LinearLayout {
    int count;

    public MyLayout(Context context) throws Throwable {
        super(context);
        init(null);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs) throws Throwable {
        super(context, attrs);
        init(attrs);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) throws Throwable {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) throws Throwable {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attr) throws Throwable {
        setOrientation(VERTICAL);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(200,200);

        final MyView myView=new MyView(getContext());
        myView.setLayoutParams(lp);
        myView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                myView.startCustomAnimation();
                postInvalidate();
            }
        });
        addView(myView);

        if(attr!=null){
            TypedArray ta=getResources().obtainAttributes(attr,R.styleable.MyLayout);

            count=ta.getInt(R.styleable.MyLayout_count,0);

            ta.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setCount(int count) {

        this.count=count;

        postInvalidate();
    }

}
