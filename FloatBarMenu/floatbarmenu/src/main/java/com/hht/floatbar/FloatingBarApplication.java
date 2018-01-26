package com.hht.floatbar;

import android.app.Application;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatingBarApplication
 * @email momo.weiye@gmail.com
 * @time 2017/12/19 14:23
 * @describe
 */
public class FloatingBarApplication  extends Application{

    private static FloatingBarApplication mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static FloatingBarApplication getInstance(){
        return mApplication;
    }


}
