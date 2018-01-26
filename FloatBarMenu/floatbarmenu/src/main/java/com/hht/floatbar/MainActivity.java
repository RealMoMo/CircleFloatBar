package com.hht.floatbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


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
}
