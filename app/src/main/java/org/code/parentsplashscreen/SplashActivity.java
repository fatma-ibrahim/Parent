package org.code.parentsplashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.code.parentsplashscreen.OnBoardingActivity;
import org.codeforiraq.parentsplashscreen.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread= new Thread(){
            public  void  run(){

                try {
                    sleep(3000);
                    Intent intent= new Intent(getApplicationContext(), OnBoardingActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }
}