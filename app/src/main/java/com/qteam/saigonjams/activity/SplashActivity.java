package com.qteam.saigonjams.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qteam.saigonjams.R;

public class SplashActivity extends AppCompatActivity {

    public static final int TIME_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, TIME_DELAY);
    }
}
