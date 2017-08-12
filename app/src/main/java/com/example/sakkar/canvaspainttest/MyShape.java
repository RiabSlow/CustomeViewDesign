package com.example.sakkar.canvaspainttest;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
class MyShape {

    private Paint paint;
    private Path path,path2;

    MyShape() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3f);

        path = new Path();
        path2 = new Path();
    }

    void setOval(float x, float y, float z, Path.Direction dir){
        path.reset();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.addCircle(75,75,50, Path.Direction.CW);

        //RectF rectF=new RectF(40,40,110,110);
        //RectF rectF=new RectF(150,150,220,220);

        //path.arcTo(rectF,0,180);

        //path.addArc(rectF,0,180);
        //path.addCircle(60,65,5,dir);
        //path.addCircle(90,65,5,dir);
    }

    Path getPath(){
        return path;
    }

    Paint getPaint(){
        return paint;
    }

}
