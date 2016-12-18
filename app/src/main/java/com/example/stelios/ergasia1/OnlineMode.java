package com.example.stelios.ergasia1;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.provider.Settings;


public class OnlineMode extends AppCompatActivity {
    Switch wifiswitch;
    Button gpsbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_mode);
        final Context context = getApplicationContext();
        final WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE);
     //   final LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        wifiswitch = (Switch)findViewById(R.id.switch1);
        wifiswitch.setChecked(false);

        gpsbutton=(Button)findViewById(R.id.button);

        if(wifi.isWifiEnabled()) wifiswitch.setChecked(true);
        else wifiswitch.setChecked(false);

        wifiswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifi.setWifiEnabled(true);
                    Toast.makeText(context, "Wi-Fi enabled", Toast.LENGTH_SHORT).show();
                } else {
                    wifi.setWifiEnabled(false);
                    Toast.makeText(context, "Wi-Fi disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gpsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                Toast.makeText(context, "GPS status changed", Toast.LENGTH_LONG).show();
            }
        });



    }
}
