package com.admin.foodgenee.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.admin.foodgenee.R;
import com.admin.foodgenee.appintro.Welcome;
import com.admin.foodgenee.firebaseservices.MyFirebaseMessagingService;

import androidx.appcompat.app.AppCompatActivity;

public class Spalsh extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.
      /*  Intent j = new Intent(Spalsh.this, MyFirebaseMessagingService.class);
        startService(j);*/

        new Handler().postDelayed(() -> {
            Intent i=new Intent(Spalsh.this,
                    Welcome.class);
            //Intent is used to switch from one activity to another.

            startActivity(i);
            //invoke the SecondActivity.

            finish();
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT);
    }
    }
