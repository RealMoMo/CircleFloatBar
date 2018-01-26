package com.hht.floatbar.service;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.hht.floatbar.R;
import com.hht.floatbar.util.BezierEvaluator;
import com.hht.floatbar.util.LogUtil;
import com.hht.floatbar.view.CircleLayout;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatbarService
 * @email momo.weiye@gmail.com
 * @time 2017/12/22 13:54
 * @describe 悬浮菜单Service：点击事件、触摸事件与动画效果。
 */
public class FloatbarService extends Service implements OnClickListener, OnTouchListener {


    private WindowManager wm;
    private WindowManager.LayoutParams params;

    //=========================隐藏菜单view=======================
    private View hideMenuView;
    // 隐藏时显示圆点
    private ImageView imHidingMenu;
    //===========================================================

    // 展开菜单view
    private View showMenuView;
    // ======================展开Menu上的组件===============
    private CircleLayout circleLayout;
    // 展开式显示圆点
    private ImageView imShowingMenu;
    private ImageView imHome;
    private ImageView imTask;
    private ImageView imWindows;
    private ImageView imReturn;
    private ImageView imWhiteBoard;
    private ImageView imAnnotation;

    //菜单选项的item集合
    private List<ImageView> views;
    //===============================================

    //是否移动flag
    private boolean isMoving = false;
    //是否展开的flag
    private boolean isShowing = false;


    // 点击坐标
    private float x, y;
    private float downX, downY;
    private float currentX, currentY;

    //Menu View的宽高一半(宽高一致的)-->为了双指点击移动到中心 & 隐藏展开到中心
    private int hideMenuHalfSize;
    private int showMenuHalfSize;

    //handler message what
    private static final int WHAT_APLHA_HALF = 0X100;
    private static final int WHAT_APLHA_ALL = 0X101;
    private static final int WHAT_APLHA_CANCEL = 0X102;


    //==========================About Animator ====================================

    //是否正在展开or隐藏Menu动画 flag
    private boolean animationRunning = false;
    //缩放动画
    private Animator scaleSmallAnim;
    private Animator scaleNormalAnim;
    //展开菜单动画的时间
    private final int DURATION_EXPAND_ANIMATION = 240;
    //收缩菜单动画的时间
    private final int DURATION_SHRINK_ANIMATION = 240;
    //位移动画的时间
    private final int DURATION_PATH_MOVE_ANIMATION = 500;
    //触摸菜单item缩放动画的时间
    private final int DURATION_TOUCH_ITEM_SCALE_ANIMATION = 200;
    //无触摸时透明度动画的时间
    private final int DURATION_NO_TOUCH_ALPHA_ANIMATION = 160;
    //触摸item缩放min值
    private final float TOUCH_ITEM_SCALE_MIN_VALUE = 0.8f;
    //触摸item缩放max值
    private final float TOUCH_ITEM_SCALE_MAX_VALUE = 1f;

    private final float APLHA_HALF_VALUE = 0.5f;
    private final float APLHA_ALL_VALUE = 0.3f;
    private final float APLHA_NO_VALUE = 1f;

    private final int ZERO = 0;
    private final int ONEHUNDRED_MILLSECOND = 100;
    private final int FIVETHOUSAND_MILLSECOND = 5000;
    private final int TENTHOUSAND_MILLSECOND = 10000;

    //==============================================================

    private int menuItemCount = 6;
    private final float angleStep = 360f / menuItemCount;


    //==============about Boardcast Receiver ==============================
    private FloatBarReceiver mFloatBarReceiver;


    //TODO 双指移动的广播（伪广播 自己测试用）
    private static final String ACTION_MOVE_POSITION = "com.hht.two.finger.move";

    //=================================================================


    private class FloatBarReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            LogUtil.d("FloatbarReceiver action:" + action);

            switch (action) {
                case ACTION_MOVE_POSITION: {
                    mHanlder.removeMessages(WHAT_APLHA_HALF);
                    mHanlder.removeMessages(WHAT_APLHA_ALL);
                    mHanlder.sendEmptyMessage(WHAT_APLHA_CANCEL);
                    Random random = new Random();
                    //TODO 获取屏幕宽度
                    int x = random.nextInt(1920) % 1920;
                    int y = random.nextInt(1080) % (1080);
                    if (isShowing) {
                        x -= showMenuHalfSize;
                        y -= showMenuHalfSize;
                    } else {
                        x -= hideMenuHalfSize;
                        y -= hideMenuHalfSize;
                    }
                    doPathAnimation(x, y);
                    mHanlder.sendEmptyMessageDelayed(WHAT_APLHA_HALF, FIVETHOUSAND_MILLSECOND);
                }
                break;
                default:
                    break;
            }
        }
    }


    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_APLHA_HALF: {
                    if (isShowing) {
                        getAlphaAnimation(showMenuView, APLHA_NO_VALUE, APLHA_HALF_VALUE).start();
                    } else {
                        getAlphaAnimation(hideMenuView, APLHA_NO_VALUE, APLHA_HALF_VALUE).start();
                    }
                    mHanlder.sendEmptyMessageDelayed(WHAT_APLHA_ALL, TENTHOUSAND_MILLSECOND);
                }
                break;
                case WHAT_APLHA_ALL: {
                    if (isShowing) {
                        getAlphaAnimation(showMenuView, APLHA_HALF_VALUE, APLHA_ALL_VALUE).start();
                    } else {
                        getAlphaAnimation(hideMenuView, APLHA_HALF_VALUE, APLHA_ALL_VALUE).start();
                    }
                }
                break;
                case WHAT_APLHA_CANCEL: {

                    if (isShowing) {
                        float alpha = showMenuView.getAlpha();
                        if (alpha < APLHA_NO_VALUE) {
                            getAlphaAnimation(showMenuView, alpha, APLHA_NO_VALUE).start();

                        }

                    } else {
                        float alpha = hideMenuView.getAlpha();
                        if (alpha < APLHA_NO_VALUE) {
                            getAlphaAnimation(hideMenuView, alpha, APLHA_NO_VALUE).start();

                        }

                    }
                }
                break;
            }
        }
    };


    @Override
    public void onCreate() {

        registerFloatBarReceiver();

        initView();
        initOnClickListener();
        initOnTouchEvent();

        createFloatBarView();


        mHanlder.sendEmptyMessageDelayed(WHAT_APLHA_HALF, FIVETHOUSAND_MILLSECOND);
    }

    private void registerFloatBarReceiver() {
        mFloatBarReceiver = new FloatBarReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_MOVE_POSITION);
        registerReceiver(mFloatBarReceiver, filter);
    }


    private void initView() {

        hideMenuView = LayoutInflater.from(this).inflate(R.layout.dismiss_menu, null);
        imHidingMenu = hideMenuView.findViewById(R.id.btn_menu);


        showMenuView = LayoutInflater.from(this).inflate(R.layout.show_menu, null);
        circleLayout = showMenuView.findViewById(R.id.circle_layout);

        imHome = showMenuView.findViewById(R.id.im_home);
        imTask = showMenuView.findViewById(R.id.im_task);
        imWindows = showMenuView.findViewById(R.id.im_windows);
        imReturn = showMenuView.findViewById(R.id.im_return);
        imWhiteBoard = showMenuView.findViewById(R.id.im_whiteboard);
        imAnnotation = showMenuView.findViewById(R.id.im_annotation);
        //设置CirCleLayout 中心View
        imShowingMenu = new ImageView(this);
        imShowingMenu.setId(R.id.circle_centerview);
        imShowingMenu.setImageResource(R.drawable.center_20);
        circleLayout.setCenterView(imShowingMenu);

        views = new ArrayList<>();
        views.add(imTask);
        views.add(imReturn);
        views.add(imWindows);
        views.add(imHome);
        views.add(imAnnotation);
        views.add(imWhiteBoard);

        hideMenuView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //init rect touch
                hideMenuHalfSize = hideMenuView.getHeight() / 2;

                hideMenuView.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this);
            }
        });

        showMenuView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                showMenuHalfSize = showMenuView.getHeight() / 2;

                showMenuView.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initOnClickListener() {

        imHidingMenu.setOnClickListener(this);
        imShowingMenu.setOnClickListener(this);

        imHome.setOnClickListener(this);
        imTask.setOnClickListener(this);
        imWindows.setOnClickListener(this);
        imReturn.setOnClickListener(this);
        imWhiteBoard.setOnClickListener(this);
        imAnnotation.setOnClickListener(this);
    }


    private void initOnTouchEvent() {
        imHidingMenu.setOnTouchListener(this);
        circleLayout.setOnTouchListener(this);


        ChildViewTouch childTouch = new ChildViewTouch();

        imShowingMenu.setOnTouchListener(childTouch);
        imHome.setOnTouchListener(childTouch);
        imTask.setOnTouchListener(childTouch);
        imWindows.setOnTouchListener(childTouch);
        imReturn.setOnTouchListener(childTouch);
        imWhiteBoard.setOnTouchListener(childTouch);
        imAnnotation.setOnTouchListener(childTouch);

    }


    private void createFloatBarView() {
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = (int) (getResources().getDisplayMetrics().widthPixels * 0.9f);
        params.y = getResources().getDisplayMetrics().heightPixels / 2;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.RGBA_8888;
        //for get showmenuview width & height
        wm.addView(showMenuView, params);
        wm.removeView(showMenuView);

        wm.addView(hideMenuView, params);


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View v) {

        if (isMoving) {
            return;
        }

        switch (v.getId()) {
            //隐藏菜单
            case R.id.circle_centerview: {

                //执行隐藏菜单动画
                if (!animationRunning) {
                    startHideMenuAnimation();
                }

            }
            break;
            //展开菜单
            case R.id.btn_menu: {


                // 显示展开菜单
                if (!animationRunning) {
                    isShowing = true;
                    wm.removeView(hideMenuView);
                    params.x = params.x - (showMenuHalfSize - hideMenuHalfSize);
                    params.y = params.y - (showMenuHalfSize - hideMenuHalfSize);

//                    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    wm.addView(showMenuView, params);
                    wm.updateViewLayout(showMenuView, params);

                    startShowMenuAnimation();
                }


            }
            break;
            case R.id.im_home: {


                sendKeyEvent(KeyEvent.KEYCODE_HOME);
            }
            break;
            case R.id.im_task: {

                showRecentlyApp();
            }
            break;
            case R.id.im_windows: {
                openSource();
            }
            break;
            case R.id.im_return: {

                sendKeyEvent(KeyEvent.KEYCODE_BACK);
            }
            break;
            case R.id.im_whiteboard: {

                openWhiteBoardMode();

            }
            break;
            case R.id.im_annotation: {

                openAnnotationMode();


            }
            break;
            default:
                break;
        }
    }


    /**
     * Touch监听事件
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        x = event.getRawX();
        y = event.getRawY();
        // 触摸事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHanlder.removeMessages(WHAT_APLHA_HALF);
                mHanlder.removeMessages(WHAT_APLHA_ALL);
                mHanlder.sendEmptyMessage(WHAT_APLHA_CANCEL);

                isMoving = false;
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    return false;
                }
                currentX = event.getX();
                currentY = event.getY();
                if (Math.abs(currentX - downX) > 5 || Math.abs(currentY - downX) > 5) {
                    isMoving = true;

                    params.x = (int) (x - downX);
                    params.y = (int) (y - downY);
                    if (isShowing) {
                        wm.updateViewLayout(showMenuView, params);
                    } else {
                        wm.updateViewLayout(hideMenuView, params);
                    }

                }
                break;
            case MotionEvent.ACTION_UP: {

                mHanlder.sendEmptyMessageDelayed(WHAT_APLHA_HALF, FIVETHOUSAND_MILLSECOND);


                if(isMoving){
                    return  true;
                }else{
                    return  false;
                }
            }
           // break;

//            case MotionEvent.ACTION_CANCEL: {
//
//            }
//            break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onDestroy() {

        if (isShowing) {
            wm.removeView(showMenuView);
        } else {
            wm.removeView(hideMenuView);
        }
        mHanlder.removeCallbacksAndMessages(null);
        mHanlder = null;
        unregisterReceiver(mFloatBarReceiver);

        super.onDestroy();

    }


    /**
     * @param keyCode 键值
     */
    private void sendKeyEvent(int keyCode) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("input keyevent " + keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示最近应用程序
     */
    private void showRecentlyApp() {

        Class<?> serviceManagerClass;
        try {
            serviceManagerClass = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClass.getMethod("getService",
                    String.class);
            IBinder retbinder = (IBinder) getService.invoke(
                    serviceManagerClass, "statusbar");
            Class<?> statusBarClass = Class.forName(retbinder
                    .getInterfaceDescriptor());
            Object statusBarObject = statusBarClass.getClasses()[0].getMethod(
                    "asInterface", IBinder.class).invoke(null,
                    new Object[]{retbinder});
            Method clearAll = statusBarClass.getMethod("toggleRecentApps");
            clearAll.setAccessible(true);
            clearAll.invoke(statusBarObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打开Source
     */
    private void openSource() {

        Toast.makeText(this,"Click Source",Toast.LENGTH_SHORT).show();
    }


    /**
     * 打开白板模式
     */
    private void openWhiteBoardMode() {
        Toast.makeText(this,"Click WhiteBoard",Toast.LENGTH_SHORT).show();
    }

    /**
     * 打开批注模式
     */
    private void openAnnotationMode() {
        Toast.makeText(this,"Click Annotation",Toast.LENGTH_SHORT).show();
    }


    private Animator getAlphaAnimation(View view, float startValue, float endValue) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", startValue, endValue);
        objectAnimator.setDuration(DURATION_NO_TOUCH_ALPHA_ANIMATION);
        objectAnimator.setInterpolator(new LinearInterpolator());


        return objectAnimator;
    }


    private void initTouchScaleAnimation(View view) {

        PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat("scaleX", TOUCH_ITEM_SCALE_MAX_VALUE, TOUCH_ITEM_SCALE_MIN_VALUE);
        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat(
                "scaleY", TOUCH_ITEM_SCALE_MAX_VALUE, TOUCH_ITEM_SCALE_MIN_VALUE);
        scaleSmallAnim = ObjectAnimator.ofPropertyValuesHolder(view, valueHolder_1, valuesHolder_2);
        scaleSmallAnim.setDuration(DURATION_TOUCH_ITEM_SCALE_ANIMATION);
        scaleSmallAnim.setInterpolator(new LinearInterpolator());

        PropertyValuesHolder valueHolder_3 = PropertyValuesHolder.ofFloat(
                "scaleX", TOUCH_ITEM_SCALE_MIN_VALUE, TOUCH_ITEM_SCALE_MAX_VALUE);
        PropertyValuesHolder valuesHolder_4 = PropertyValuesHolder.ofFloat(
                "scaleY", TOUCH_ITEM_SCALE_MIN_VALUE, TOUCH_ITEM_SCALE_MAX_VALUE);
        scaleNormalAnim = ObjectAnimator.ofPropertyValuesHolder(view, valueHolder_3,
                valuesHolder_4);
        scaleNormalAnim.setDuration(DURATION_TOUCH_ITEM_SCALE_ANIMATION);
        scaleNormalAnim.setInterpolator(new LinearInterpolator());


    }




    private void startHideMenuAnimation() {

        ValueAnimator hideMenu = ValueAnimator.ofFloat(0f, 1f);
        hideMenu.setInterpolator(new LinearInterpolator());
        hideMenu.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animationRunning = true;
                for (ImageView view : views) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                imShowingMenu.setScaleX(1f);
                imShowingMenu.setScaleY(1f);

                isShowing = false;
                wm.removeView(showMenuView);
                //+42
                params.x = params.x + showMenuHalfSize - hideMenuHalfSize;
                params.y = params.y + showMenuHalfSize - hideMenuHalfSize;
//                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                wm.addView(hideMenuView, params);
                wm.updateViewLayout(hideMenuView, params);

                animationRunning = false;


            }
        });
        hideMenu.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float fraction = valueAnimator.getAnimatedFraction();
                final float value = (float) valueAnimator.getAnimatedValue();

                for (int i = 0; i < menuItemCount; i++) {
                    float angle = angleStep * i - 90;
//                    float x = (float) Math.cos(Math.toRadians(angle + 120 * fraction)) * circleLayout.getRadius();
//                    float y = (float) Math.sin(Math.toRadians(angle + 120 * fraction)) * circleLayout.getRadius();

                    float x = (float) Math.cos(Math.toRadians(angle + 120 * fraction)) * (circleLayout.getRadius() - hideMenuHalfSize * fraction);
                    float y = (float) Math.sin(Math.toRadians(angle + 120 * fraction)) * (circleLayout.getRadius() - hideMenuHalfSize * fraction);

                    ImageView button = views.get(i);
                    button.setX(circleLayout.getCenterX() + x - imShowingMenu.getWidth());
                    button.setY(circleLayout.getCenterY() + y - imShowingMenu.getHeight());
                    button.setScaleX(1f - 0.7f * fraction);
                    button.setScaleY(1f - 0.7f * fraction);
                    //hide center img:30px  show center img:20px ->scale(1+0.5)
                    imShowingMenu.setScaleX(1f + 0.5f * fraction);
                    imShowingMenu.setScaleY(1f + 0.5f * fraction);
                }
            }
        });

        hideMenu.setDuration(DURATION_SHRINK_ANIMATION);
        hideMenu.start();
        ;
    }


    private void startShowMenuAnimation() {

        final ValueAnimator buttonsAppear = ValueAnimator.ofFloat(0f, 1f);
        buttonsAppear.setInterpolator(new LinearInterpolator());
        buttonsAppear.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                animationRunning = true;
                for (ImageView view : views) {
                    view.setScaleX(0.3f);
                    view.setScaleY(0.3f);
                    view.setVisibility(View.VISIBLE);
                }
                imShowingMenu.setScaleX(1.5f);
                imShowingMenu.setScaleY(1.5f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationRunning = false;

//                imShowingMenu.setScaleX(1f);
//                imShowingMenu.setScaleY(1f);


            }
        });
        buttonsAppear.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final float fraction = valueAnimator.getAnimatedFraction();
                final float value = (float) valueAnimator.getAnimatedValue();

                for (int i = 0; i < menuItemCount; i++) {
                    float angle = angleStep * (i + 2) - 90;
//                    float x = (float) Math.cos(Math.toRadians(angle - 120 * fraction)) * circleLayout.getRadius();
//                    float y = (float) Math.sin(Math.toRadians(angle - 120 * fraction)) * circleLayout.getRadius();

                    float x = (float) Math.cos(Math.toRadians(angle - 120 * fraction)) * (circleLayout.getRadius() - hideMenuHalfSize * (1 - fraction));
                    float y = (float) Math.sin(Math.toRadians(angle - 120 * fraction)) * (circleLayout.getRadius() - hideMenuHalfSize * (1 - fraction));


                    ImageView button = views.get(i);

                    button.setX(circleLayout.getCenterX() + x - imShowingMenu.getWidth());
                    button.setY(circleLayout.getCenterY() + y - imShowingMenu.getHeight());
                    button.setScaleX(0.3f + 0.7f * fraction);
                    button.setScaleY(0.3f + 0.7f * fraction);
                }
                //hide center img:30px  show center img:20px ->scale(1.5-0.5)
                imShowingMenu.setScaleX(1.5f - 0.5f * fraction);
                imShowingMenu.setScaleY(1.5f - 0.5f * fraction);
            }
        });

        buttonsAppear.setDuration(DURATION_EXPAND_ANIMATION);

        buttonsAppear.start();
        ;

    }


    //test path animation
    private BezierEvaluator evaluator;

    private void doPathAnimation(int x, int y) {

        if (evaluator == null) {
            Point controll = new Point((x + params.x) / 2, (y + params.y) / 2 - 100);
            evaluator = new BezierEvaluator(controll);
        } else {
            evaluator.setControllPoint((x + params.x) / 2, (y + params.y) / 2 - 100);
        }

        Point startPosition = new Point(params.x, params.y);
        Point endPosition = new Point(x, y);

        ValueAnimator anim = ValueAnimator.ofObject(evaluator, startPosition, endPosition);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {


            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                params.x = point.x;
                params.y = point.y;
                if (isShowing) {

                    wm.updateViewLayout(showMenuView, params);
                } else {
                    wm.updateViewLayout(hideMenuView, params);
                }


            }
        });
        anim.setDuration(DURATION_PATH_MOVE_ANIMATION);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();


    }



    class ChildViewTouch implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            x = event.getRawX();
            y = event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {

                    mHanlder.removeMessages(WHAT_APLHA_HALF);
                    mHanlder.removeMessages(WHAT_APLHA_ALL);
                    mHanlder.sendEmptyMessage(WHAT_APLHA_CANCEL);


                    initTouchScaleAnimation(v);
                    scaleNormalAnim.end();
                    scaleSmallAnim.start();

                    downX = event.getX();
                    downY = event.getY();

                    isMoving = false;
                }
                break;
                case MotionEvent.ACTION_MOVE: {

                    currentX = event.getX();
                    currentY = event.getY();

                    if (Math.abs(currentY - downY) > 3 || Math.abs(currentX - downX) > 3) {
                        isMoving = true;
                        params.x = (int) (x - downX - v.getLeft());
                        params.y = (int) (y - downY - v.getTop());

                        wm.updateViewLayout(showMenuView, params);
                    }
                }
                break;
                case MotionEvent.ACTION_UP: { //this no break->to do scale animation & update rect touch

                    mHanlder.sendEmptyMessageDelayed(WHAT_APLHA_HALF, FIVETHOUSAND_MILLSECOND);
                }
                case MotionEvent.ACTION_CANCEL: {

                    scaleSmallAnim.end();
                    scaleNormalAnim.start();



                    if(isMoving){
                        return  true;
                    }else{
                        return  false;
                    }

                }
               // break;
                default:
                    break;
            }
            return false;
        }

    }


}
