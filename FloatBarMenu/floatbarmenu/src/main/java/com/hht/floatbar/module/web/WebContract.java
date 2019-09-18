package com.hht.floatbar.module.web;

import android.app.Activity;

import com.hht.floatbar.base.IBasePresenter;
import com.hht.floatbar.base.IBaseView;


public interface WebContract {

    interface IWebView extends IBaseView {
        Activity getWebViewContext();

        void setGankTitle(String title);

        void loadGankUrl(String url);

        void initWebView();
    }

    interface IWebPresenter extends IBasePresenter {
        String getGankUrl();
    }
}
