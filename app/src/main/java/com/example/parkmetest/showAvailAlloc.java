package com.example.parkmetest;

//
//This page here is to show all the nearest available
//parking spots



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class showAvailAlloc extends AppCompatActivity  {

    LocationManager locationManager;
    String latitude, longitude;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_avail_alloc);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        SharedPreferences sharedPreferences = getSharedPreferences("userdata",Context.MODE_PRIVATE);
        String url = "http://vast-ridge-62077.herokuapp.com/api/driver/location/nearest";
        new ShowAvailTask().execute(url,sharedPreferences.getString("token",null),sharedPreferences.getString("latitide",null),sharedPreferences.getString("longitude",null));
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    String UserPREFERENCES = "userdata";
                                    SharedPreferences sharedPreferences = getSharedPreferences(UserPREFERENCES,Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("latitude",String.valueOf(location.getLatitude()));
                                    editor.putString("longitude",String.valueOf(location.getLongitude()));
                                    editor.apply();

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
           // Toast.makeText(getApplicationContext(),mLastLocation.getLatitude()+" ",Toast.LENGTH_LONG).show();
            String UserPREFERENCES = "userdata";
            SharedPreferences sharedPreferences = getSharedPreferences(UserPREFERENCES,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude",String.valueOf(mLastLocation.getLatitude()));
            editor.putString("longitude",String.valueOf(mLastLocation.getLongitude()));
            editor.apply();

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
//        if (checkPermissions()) {
//            getLastLocation();
//        }

    }
    class ShowAvailTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS).build();
            RequestBody formBody = new FormBody.Builder()
                    .add("latitude", "2.4")
                    .add("longitude", "2.5")
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .header("Authorization", strings[1])
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

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            if (s == null) {
                Toast.makeText(getApplicationContext(), "Request timed out", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONArray profileResp = new JSONArray(s);
                    if(profileResp.toString().contains("Unable to login with given Credentials")){
                        Toast.makeText(getApplicationContext(),";jfjgdjgf",Toast.LENGTH_LONG).show();
                    }
                    else {


                        Toast.makeText(getApplicationContext(), profileResp.toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
