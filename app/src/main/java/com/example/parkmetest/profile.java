package com.example.parkmetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class profile extends AppCompatActivity {


    TextView name,username;
    String UserPREFERENCES = "userdata";
    String NAME = "name";
    String EMAILID = "email";
    String PASSWORD = "password";
    String TOKEN = "token";
    Button showvehicle;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name  = findViewById(R.id.name);
        username = findViewById(R.id.username);
        sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        name.setText(sharedpreferences.getString(NAME,"Unknown"));
        username.setText(sharedpreferences.getString(EMAILID,"Unknown"));
        showvehicle = findViewById(R.id.showvehicles);
        showvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this,vehicles.class));
            }
        });


    }
}
