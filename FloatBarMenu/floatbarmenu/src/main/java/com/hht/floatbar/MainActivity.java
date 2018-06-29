package com.hht.floatbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.hht.floatbar.module.web.WebViewActivity;
import com.hht.floatbar.service.FloatbarService;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent service = new Intent(this, FloatbarService.class);
        service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(service);



    }



    public void jumpToGithub(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title","我的GitHub,欢迎Star");
        intent.putExtra("url","https://github.com/RealMoMo");
        this.startActivity(intent);

    }

    public void executePathAnimation(View view) {
        Intent intent = new Intent("com.realmo.two.finger.touch");
        sendBroadcast(intent);
    }
}
