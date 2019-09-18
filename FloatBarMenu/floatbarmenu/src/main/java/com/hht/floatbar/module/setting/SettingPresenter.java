package com.hht.floatbar.module.setting;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/11 11:29
 * @describe
 */
public class SettingPresenter implements SettingContract.ISettingPresenter {


    private SettingContract.ISettingView view;

    public SettingPresenter(SettingContract.ISettingView settingView) {
        view = settingView;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
