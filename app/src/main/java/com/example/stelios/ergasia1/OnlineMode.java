package com.example.stelios.ergasia1;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.provider.Settings;

public class OnlineMode extends AppCompatActivity {
    ToggleButton toggle;
 /*   LocationManager locationManager;
    WifiManager wifiManager;
    TextView textViewGPS, textViewWIFI;

    boolean GpsStatus;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_mode);

        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //start service?
                    //online
                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(i);

                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent1);

                } else {
                    // The toggle is disabled
                }
            }
        });
/*
    public void CheckGpsStatus(){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GpsStatus == true) {
            textViewGPS.setText("Location Services Is Enabled");
        } else {
            textViewGPS.setText("Location Services Is Disabled");
        }
    }
*/
    }
}
