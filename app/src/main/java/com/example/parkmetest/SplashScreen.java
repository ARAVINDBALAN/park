package com.example.parkmetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

//import com.example.parkmetest.ui.login.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String UserPREFERENCES = "userdata";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                if(sharedPreferences.getString("name","Unknown").equals("Unknown")) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                else{
                    startActivity(new Intent(SplashScreen.this,showAvailAlloc.class));
                }
                finish();
            }
        }, 3000);
    }
}
