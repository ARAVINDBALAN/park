package com.example.parkmetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkmetest.LocationPlaces;
import com.example.parkmetest.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LocationPlaces> arrayList;
    private TextView  name, contactNum,latitude,longitude,spots,vacantSpots;
    public ListAdapter(Context context, ArrayList<LocationPlaces> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_view, parent, false);
        name = convertView.findViewById(R.id.name);
        //contactNum = convertView.findViewById(R.id.contactNum);
        name.setText(arrayList.get(position).name);
        latitude = convertView.findViewById(R.id.latitude);
        latitude.setText(" " + arrayList.get(position).getLatitude());
        longitude = convertView.findViewById(R.id.longitude);
        final View finalConvertView = convertView;
        longitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(finalConvertView.getContext(),"ssds",Toast.LENGTH_LONG).show();
            }
        });
        latitude.setText(" " + arrayList.get(position).getLongitude());
        return convertView;
    }
}