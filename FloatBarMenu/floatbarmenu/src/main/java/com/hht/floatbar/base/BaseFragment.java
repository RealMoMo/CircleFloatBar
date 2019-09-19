package com.hht.floatbar.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/19 17:24
 * @describe
 */
public abstract  class BaseFragment<T extends IBasePresenter,V extends IBaseView> extends Fragment {

    protected T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(),container,false);
        initView(view);
        presenter = createPresenter();
        presenter.subscribe();


        return view;
    }


    public abstract int layoutId();

    public abstract void initView(View view);

    public abstract T createPresenter();
}
