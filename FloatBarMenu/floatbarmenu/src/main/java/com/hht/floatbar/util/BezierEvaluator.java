package com.hht.floatbar.util;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * @author Realmo
 * @version 1.0.0
 * @name BezierEvaluator
 * @email momo.weiye@gmail.com
 * @time 2018/1/3 13:52
 * @describe  自定义贝塞尔曲线动画估值器
 */
public class BezierEvaluator implements TypeEvaluator<Point> {

    //控制点
    private Point controllPoint;

    private Point curPoint;

    public BezierEvaluator(){
        curPoint = new Point();
    }

    public BezierEvaluator(Point controllPoint){
        curPoint = new Point();
        this.controllPoint = controllPoint;
    }

    public void setControllPoint(Point controllPoint) {
        this.controllPoint = controllPoint;
    }

    public void setControllPoint(int x,int y){
        if(controllPoint == null){
            controllPoint = new Point();
        }
        controllPoint.set(x,y);
    }

    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
        int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
        curPoint.set(x, y);
        return curPoint;
    }
}
