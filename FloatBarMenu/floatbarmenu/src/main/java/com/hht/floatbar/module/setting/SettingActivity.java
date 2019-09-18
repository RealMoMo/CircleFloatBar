package com.hht.floatbar.module.setting;

import android.widget.TextView;

import com.hht.floatbar.R;
import com.hht.floatbar.base.BaseActivity;
import com.hht.floatbar.base.IBasePresenter;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/11 11:12
 * @describe
 */
public class SettingActivity extends BaseActivity implements SettingContract.ISettingView {


    private TextView mFilterTv;



    @Override
    public int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {

    }

    @Override
    public IBasePresenter createPresenter() {
        return new SettingPresenter(this);
    }
}
