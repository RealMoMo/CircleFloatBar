package com.hht.floatbar;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.hht.floatbar.module.web.WebViewActivity;
import com.hht.floatbar.service.FloatbarService;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void starFloatBar(View view) {
        if(checkFloatingWindowPermission()){
            Intent service = new Intent(this, FloatbarService.class);
            service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(service);
        }
    }


    private boolean checkFloatingWindowPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                return true;
            } else {
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Toast.makeText(MainActivity.this,R.string.permission_floating_window,Toast.LENGTH_SHORT).show();
                startActivity(intent);
                return false;
            }

        }else {
            return true;
        }
    }


}
