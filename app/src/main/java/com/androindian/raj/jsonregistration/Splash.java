package com.androindian.raj.jsonregistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                SharedPreferences preferences=
                        getSharedPreferences("Login",MODE_PRIVATE);
                String s1=preferences.getString("Mobile",null);

                /*if(s1.isEmpty()){*/
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                    finish();
                /*}else{
                    Intent i = new Intent(Splash.this, Welcome.class);
                    startActivity(i);
                    finish();
                }*/



        }
    }, SPLASH_TIME_OUT);
}
    }

