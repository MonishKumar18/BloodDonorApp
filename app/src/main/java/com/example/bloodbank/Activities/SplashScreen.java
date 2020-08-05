package com.example.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.bloodbank.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences prefs=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        final String city =prefs.getString("login", "");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!(city.equals(""))){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashScreen.this, LoginAcivity.class));
                    finish();
                }
            }
        }, 1500);
    }
}
