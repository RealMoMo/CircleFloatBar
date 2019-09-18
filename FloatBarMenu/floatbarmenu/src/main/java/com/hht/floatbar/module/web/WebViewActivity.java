package com.hht.floatbar.module.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hht.floatbar.R;
import com.hht.floatbar.base.BaseActivity;

import androidx.appcompat.widget.Toolbar;


public class WebViewActivity extends BaseActivity implements WebContract.IWebView {



    TextView mWebTitle;

    Toolbar mWebToolbar;

    ProgressBar mWebProgressBar;

    WebView mWebView;


    @Override
    public int layoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView() {
        mWebToolbar = findViewById(R.id.web_toolbar);
        mWebTitle = findViewById(R.id.web_title);
        mWebProgressBar = findViewById(R.id.web_progressBar);
        mWebView = findViewById(R.id.web_view);
        
        mWebToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public WebContract.IWebPresenter createPresenter() {
        return new WebPresenter(this);
    }


    @Override
    public Activity getWebViewContext() {
        return this;
    }

    @Override
    public void setGankTitle(String title) {
        mWebTitle.setText(title);
    }

    @Override
    public void loadGankUrl(String url) {
        mWebView.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);

        mWebView.setWebChromeClient(new MyWebChrome());
        mWebView.setWebViewClient(new MyWebClient());
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private class MyWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mWebProgressBar.setVisibility(View.VISIBLE);
            mWebProgressBar.setProgress(newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            mWebProgressBar.setVisibility(View.GONE);
        }
    }
}
