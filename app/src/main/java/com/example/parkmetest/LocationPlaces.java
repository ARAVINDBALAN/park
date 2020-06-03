package com.example.parkmetest;

public class LocationPlaces {

    public String name;
    public String latitude;
    public  String longitude;
    public String spots;
    public String vacantSpots;


    public LocationPlaces(String name, String latitude, String longitude, String spots, String vacantSpots) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.spots = spots;
        this.vacantSpots = vacantSpots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpots() {
        return spots;
    }

    public void setSpots(String spots) {
        this.spots = spots;
    }

    public String getVacantSpots() {
        return vacantSpots;
    }

    public void setVacantSpots(String vacantSpots) {
        this.vacantSpots = vacantSpots;
    }



}
