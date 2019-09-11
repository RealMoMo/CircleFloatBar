package com.hht.floatbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hht.floatbar.R;

import androidx.annotation.NonNull;


/**
 * @author Realmo
 * @version 1.0.0
 * @name CircleLayout
 * @email momo.weiye@gmail.com
 * @time 2017/12/19 14:50
 * @describe 圆形布局(阉割版:移除可滚动圆盘功能)
 */
public class CircleLayout extends ViewGroup {


    private int mRadius = 250;//子item的中心和整个layout中心的距离
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMaxHeight = Integer.MAX_VALUE;

    private int mCenterX;
    private int mCenterY;
    private double mChangeCorner = 0.0;

    private boolean isCanScroll = false;
    private boolean isDragging = false;


    private View mCenterView;


    public CircleLayout(Context context) {
        this(context, null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CircleLayout, defStyleAttr, defStyleAttr);
        if (attrs != null) {
            try {
                mRadius = (int) a.getDimension(R.styleable.CircleLayout_radium, 250);
                mChangeCorner = (double) a.getFloat(R.styleable.CircleLayout_changeCorner, 0);
            } finally {
                a.recycle();
            }

        }


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();//item的数量

        //可用宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int ps = getPaddingStart();
        int pe = getPaddingEnd();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        setMeasuredDimension(widthSize, heightSize);

        //子view最高多少，最宽多少
        int childMaxWidth = 0;
        int childMaxHeight = 0;
        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            child.measure(MeasureSpec.makeMeasureSpec(widthSize - ps - pe, MeasureSpec.UNSPECIFIED)
                    , MeasureSpec.makeMeasureSpec(heightSize - pt - pb, MeasureSpec.UNSPECIFIED));

            childMaxWidth = Math.max(childMaxWidth, child.getMeasuredWidth());
            childMaxHeight = Math.max(childMaxHeight, child.getMeasuredHeight());
        }

        int width = resolveAdjustedSize(mRadius * 2 + childMaxWidth + ps + pe, mMaxWidth, widthMeasureSpec);
        int height = resolveAdjustedSize(mRadius * 2 + childMaxHeight + pt + pb, mMaxHeight, heightMeasureSpec);

        int finalWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec));
        int finalHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
        setMeasuredDimension(finalWidthSpec, finalHeightSpec);

    }

    private int resolveAdjustedSize(int desiredSize, int maxSize,
                                    int measureSpec) {
        int result = desiredSize;
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                   than max size imposed on ourselves.
                */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = mCenterView == null ? getChildCount() : getChildCount() - 1;//item的数量
        mCenterX = (getMeasuredWidth() - getPaddingStart() - getPaddingEnd()) / 2;
        mCenterY = (getMeasuredHeight() - getPaddingBottom() - getPaddingTop()) / 2;

        View child;
        int childWidth;//item的宽
        int childHeight;//item的高
        double corner;//旋转角度

        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }
            corner = 360 / childCount * i;

            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();


            int cX = (int) (mCenterX - mRadius * Math.cos(Math.toRadians(corner + mChangeCorner)));
            int cY = (int) (mCenterY - mRadius * Math.sin(Math.toRadians(corner + mChangeCorner)));

            child.layout(cX - childWidth / 2, cY - childHeight / 2, cX + childWidth / 2, cY + childHeight / 2);

        }
        if (mCenterView != null) {
            mCenterView.layout(mCenterX - mCenterView.getMeasuredWidth() / 2, mCenterY - mCenterView.getMeasuredHeight() / 2
                    , mCenterX + mCenterView.getMeasuredWidth() / 2, mCenterY + mCenterView.getMeasuredHeight() / 2);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN: {

                return true;
            }
            case MotionEvent.ACTION_POINTER_UP: {

                return true;
            }
            default:
                return false;
        }


    }


    @SuppressWarnings("unused")
    public int getRadius() {
        return mRadius;
    }

    @SuppressWarnings("unused")
    public void setRadius(int mRadius) {
        this.mRadius = mRadius;
        requestLayout();
    }

    @SuppressWarnings("unused")
    public int getMaxWidth() {
        return mMaxWidth;
    }

    @SuppressWarnings("unused")
    public void setMaxWidth(int mMaxWidth) {
        this.mMaxWidth = mMaxWidth;
    }

    @SuppressWarnings("unused")
    public int getMaxHeight() {
        return mMaxHeight;
    }

    @SuppressWarnings("unused")
    public void setMaxHeight(int mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }


    @SuppressWarnings("unused")
    public boolean isCanScroll() {
        return isCanScroll;
    }

    /**
     * @param canScroll 设置是否可以旋转
     */
    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    @SuppressWarnings("unused")
    public boolean isDragging() {
        return isDragging;
    }

    public void setCenterView(@NonNull View view) {
        if (mCenterView == null) {
            mCenterView = view;
            addView(mCenterView);
        }
        requestLayout();
    }

    public void removeCenterView() {
        if (mCenterView != null) {
            removeView(mCenterView);
            mCenterView = null;
        }
    }

    /**
     * @return 获取中心的view，没有的话就返回null
     */
    public View getCenterView() {
        return mCenterView;
    }

    /**
     * @return 获取旋转角度
     */
    public double getChangeCorner() {
        return mChangeCorner;
    }

    /**
     * @param changeCorner 设置旋转角度
     */
    public void setChangeCorner(double changeCorner) {
        this.mChangeCorner = changeCorner;
        invalidate();
    }

    /**
     * @return 获取layout中心的x坐标
     */
    public int getCenterX() {
        return mCenterX;
    }

    /**
     * @return 获取layout中心的y坐标
     */
    public int getCenterY() {
        return mCenterY;
    }


}
