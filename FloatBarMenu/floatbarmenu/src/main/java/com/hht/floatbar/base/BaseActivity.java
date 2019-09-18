package com.hht.floatbar.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/18 16:25
 * @describe
 */
public abstract class BaseActivity<T extends IBasePresenter,V extends IBaseView> extends AppCompatActivity  {


    protected T presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initView();
        presenter = createPresenter();
        presenter.subscribe();
    }

    public abstract int layoutId();

    public abstract void initView();

    public abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.unSubscribe();
    }
}
