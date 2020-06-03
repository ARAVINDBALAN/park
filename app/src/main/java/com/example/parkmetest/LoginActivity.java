package com.example.parkmetest;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parkmetest.MainActivity;
import com.example.parkmetest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    Button mEmailSignInButton;
    EditText username;
    EditText passwordEditText;
    String URL = "happy";

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String UserPREFERENCES = "userdata";
    String NAME = "name";
    String EMAILID = "email";
    String PASSWORD = "password";
    String TOKEN = "token";
    String usernameget;
    String passwordget;

    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        mEmailSignInButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        sharedpreferences = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String url = URL + "username=" + username.getText() + "&password=" + passwordEditText.getText();
                String url = "http://vast-ridge-62077.herokuapp.com/api/driver/auth/login/";
//                url = url.replace("@", "%40");
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                usernameget = username.getText().toString();
                passwordget = passwordEditText.getText().toString();
                if (usernameget.isEmpty() || passwordget.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "The fields are empty", Toast.LENGTH_LONG).show();
                }
                else{
                    if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        // notify user you are online
                        new LoginAsyncTask().execute(url, usernameget, passwordget);
                    } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                        Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }


    class LoginAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS).build();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", strings[1])
                    .add("password", strings[2])
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(formBody)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Log.d("request :", "timed out");
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEmailSignInButton.setEnabled(false);
            mEmailSignInButton.setText("PLEASE WAIT");
        }

        @Override
        protected void onPostExecute(String s) {
            mEmailSignInButton.setEnabled(true);
            mEmailSignInButton.setText("LOGIN");
            super.onPostExecute(s);
            if (s == null) {
                Toast.makeText(getApplicationContext(), "Request timed out", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject profileResp = new JSONObject(s);
                    if(profileResp.toString().contains("Unable to login with given Credentials")){
                        Toast.makeText(getApplicationContext(),";jfjgdjgf",Toast.LENGTH_LONG).show();
                    }
                    else {

                        editor.putString(NAME,profileResp.getString("username"));
                        editor.putString(TOKEN,profileResp.getString("token"));
                        editor.putString(EMAILID,username.getText().toString());
                        editor.putString(PASSWORD,passwordEditText.getText().toString());
                        editor.apply();

                        finish();
                        startActivity(new Intent(LoginActivity.this,profile.class));

                        //Toast.makeText(getApplicationContext(), profileResp.toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}