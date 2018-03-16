package com.hht.floatbar.module.web;

import android.app.Activity;

import com.hht.floatbar.base.BasePresenter;
import com.hht.floatbar.base.BaseView;


public interface WebContract {

    interface IWebView extends BaseView {
        Activity getWebViewContext();

        void setGankTitle(String title);

        void loadGankUrl(String url);

        void initWebView();
    }

    interface IWebPresenter extends BasePresenter {
        String getGankUrl();
    }
}
