package com.example.stelios.ergasia1;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class LocListener extends AppCompatActivity implements android.location.LocationListener{
    static double lat;
    static double lon;
    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();

        lat = location.getLatitude();
        lon = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
