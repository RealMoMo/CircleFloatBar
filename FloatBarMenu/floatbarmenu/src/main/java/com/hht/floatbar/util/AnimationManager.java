package com.hht.floatbar.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author Realmo
 * @version 1.0.0
 * @name AnimationManager
 * @email momo.weiye@gmail.com
 * @time 2017/12/20 9:27
 * @describe 管理FloatingBar动画
 */
//TODO 管理FloatingBar动画
public class AnimationManager {

//    private static AnimationManager mInstance = new AnimationManager();
//
//
//    //==========================Animator===================================
//    //触摸菜单Item action_down的缩小动画
//    public Animator touchDownItemScaleAnimation;
//    //触摸菜单Item action_up&action_cancel的放大动画
//    private Animator touchUpItemScaleAnimation;
//    //
//    private ValueAnimator noTouchAlphaAnimation;
//
//
//    //==============================================================
//
//
//
//    //==========================Animator duration time====================================
//
//    //展开菜单动画的时间
//    private final long DURATION_EXPAND_ANIMATION = 240;
//    //收缩菜单动画的时间
//    private final long DURATION_SHRINK_ANIMATION = 240;
//    //触摸菜单item缩放动画的时间
//    private final long DURATION_TOUCH_ITEM_SCALE_ANIMATION = 200;
//    //无触摸时透明度动画的时间
//    private final long DURATION_NO_TOUCH_ALPHA_ANIMATION = 160;
//
//    //触摸item缩放min值
//    private final float TOUCH_ITEM_SCALE_MIN_VALUE = 0.8f;
//    //触摸item缩放max值
//    private final float TOUCH_ITEM_SCALE_MAX_VALUE = 1f;
//
//
//    //==============================================================
//
//
//
//
//
//
//    private AnimationManager(){
//        initNoTouchAlphaAnimation();
//        initTouchDownItemScaleAnimation();
//        initTouchUpItemScaleAnimation();
//    }
//
//    public static AnimationManager getInstance(){
//        return mInstance;
//    }
//
//
//
//    private void initNoTouchAlphaAnimation(){
//
//        PropertyValuesHolder valueHolder = PropertyValuesHolder.ofFloat("alpha", 1f, 0.5f);
//        noTouchAlphaAnimation = ObjectAnimator.ofPropertyValuesHolder(valueHolder);
//        noTouchAlphaAnimation.setDuration(DURATION_NO_TOUCH_ALPHA_ANIMATION);
//        noTouchAlphaAnimation.setInterpolator(new LinearInterpolator());
//
//    }
//
//    private Animator getNoTouchAlphaAnimation(View view, float startValue, float endValue){
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"alpha",startValue,endValue);
//        objectAnimator.setDuration(DURATION_NO_TOUCH_ALPHA_ANIMATION);
//        objectAnimator.setInterpolator(new LinearInterpolator());
//
//        return  objectAnimator;
//    }
//
//
//
//
//
//    private void initTouchDownItemScaleAnimation(){
//        PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat("scaleX", TOUCH_ITEM_SCALE_MAX_VALUE, TOUCH_ITEM_SCALE_MIN_VALUE);
//        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat(
//                "scaleY", TOUCH_ITEM_SCALE_MAX_VALUE, TOUCH_ITEM_SCALE_MIN_VALUE);
//        touchDownItemScaleAnimation = ObjectAnimator.ofPropertyValuesHolder(valueHolder_1, valuesHolder_2);
//        touchDownItemScaleAnimation.setDuration(DURATION_TOUCH_ITEM_SCALE_ANIMATION);
//        touchDownItemScaleAnimation.setInterpolator(new LinearInterpolator());
//
//    }
//
//
//    private void initTouchUpItemScaleAnimation(){
//        PropertyValuesHolder valueHolder_3 = PropertyValuesHolder.ofFloat(
//                "scaleX", TOUCH_ITEM_SCALE_MIN_VALUE, TOUCH_ITEM_SCALE_MAX_VALUE);
//        PropertyValuesHolder valuesHolder_4 = PropertyValuesHolder.ofFloat(
//                "scaleY", TOUCH_ITEM_SCALE_MIN_VALUE, TOUCH_ITEM_SCALE_MAX_VALUE);
//        touchUpItemScaleAnimation = ObjectAnimator.ofPropertyValuesHolder(valueHolder_3,
//                valuesHolder_4);
//        touchUpItemScaleAnimation.setDuration(DURATION_TOUCH_ITEM_SCALE_ANIMATION);
//        touchUpItemScaleAnimation.setInterpolator(new LinearInterpolator());
//
//    }
//
//
//    /**
//     *
//     * @param view
//     * @param startValue
//     * @param endValue
//     * @return
//     */
//    public Animator updateNoTouchAlphaAnimation(View view ,float startValue,float endValue){
//        if(noTouchAlphaAnimation == null){
//            initNoTouchAlphaAnimation();
//        }
//
//        noTouchAlphaAnimation.setTarget(view);
//        noTouchAlphaAnimation.setFloatValues(startValue,endValue);
//        return noTouchAlphaAnimation;
//    }
//
//
//
//    public Animator updateTouchDownItemScaleAnimation(View view){
//        if(touchDownItemScaleAnimation == null){
//            initTouchDownItemScaleAnimation();
//        }
//        touchDownItemScaleAnimation.setTarget(view);
//        return touchDownItemScaleAnimation;
//    }
//
//
//    public Animator updateTouchUpItemScaleAnimation(View view){
//        if(touchUpItemScaleAnimation == null){
//            initTouchUpItemScaleAnimation();
//        }
//        touchUpItemScaleAnimation.setTarget(view);
//        LogUtil.d("updateTouchUpItemScaleAnimation");
//        return touchUpItemScaleAnimation;
//    }




}
