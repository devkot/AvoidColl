package com.example.stelios.ergasia1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.UUID;

//google settings api documentation
public class OnMode extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ConnectivityReceiver.ConnectivityReceiverListener, SensorEventListener {
    private GoogleApiClient googleApiClient; //google api for gps
    private SensorManager oSensorManager; //set sensor manager
    private Sensor oSpeed, oProximity, oLight; //declare sensors
    private Switch myswitch;
    String DeviceID=UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_mode);
        Context context = getApplicationContext();
        myswitch=(Switch)findViewById(R.id.switch2);
        myswitch.setChecked(false);

        oSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        oSpeed = oSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        oProximity = oSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        oLight = oSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);





        LocationManager LM = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);//gps manager
        WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE);//wifi manager
        if(!wifi.isWifiEnabled()){//is it isn't enabled, turn it on and show toast
            wifi.setWifiEnabled(true);
            Toast.makeText(context, "Wi-Fi enabled", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "Wi-Fi is already enabled", Toast.LENGTH_SHORT).show();//else inform the user

        if(LM.isProviderEnabled(LocationManager.GPS_PROVIDER)){//inform the user that gps was turned on
            Toast.makeText(context, "GPS is already enabled", Toast.LENGTH_SHORT).show();
        }


//following piece of code is taken from google settings api
        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();//create request
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//use gps,wifi (and network but it doesn't have permissions)
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true); //it was boolean show in google doc

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        OnMode.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });


        }

        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    oSensorManager.registerListener(OnMode.this, oSpeed, SensorManager.SENSOR_DELAY_NORMAL);
                    oSensorManager.registerListener(OnMode.this, oLight, SensorManager.SENSOR_DELAY_NORMAL);
                    oSensorManager.registerListener(OnMode.this, oProximity, SensorManager.SENSOR_DELAY_NORMAL);
                }else{
                    oSensorManager.unregisterListener(OnMode.this);
                }

            }
        });


    }//oncreate end

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Init.getInstance().setConnectivityListener(this);
       // oSensorManager.registerListener(this, oSpeed, SensorManager.SENSOR_DELAY_NORMAL);
       // oSensorManager.registerListener(this, oLight, SensorManager.SENSOR_DELAY_NORMAL);
     //   oSensorManager.registerListener(this, oProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //create menu
        inflater.inflate(R.menu.online_menu, menu); //get resource
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){ //switch classes
            case R.id.mqtt_settings://go to mqtt settings
             //   Intent intent = new Intent(this, Settings.class);//allagh auta
             //   this.startActivity(intent);
                break;
            case R.id.offline: //button to go offline
                WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(false);//broadcaster will terminate activity
                break;
            case R.id.exit2:
                finish();
                System.exit(0); //exit app without dialog
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() { //exit app through back button
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit") //build dialog
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //exit on click
                        WifiManager wifi =(WifiManager)getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(false);//broadcaster will terminate activity
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) { //on connection change we terminate the activity
        if(isConnected==false){
            Toast.makeText(getApplicationContext(), "No internet connectivity, Online Mode terminating", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                Publisher.main(String.valueOf(event.values[0]),"Accelerometer",DeviceID);
                Publisher.main(String.valueOf(event.values[1]),"Accelerometer",DeviceID);
                Publisher.main(String.valueOf(event.values[2]),"Accelerometer",DeviceID);
            }
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                Publisher.main(String.valueOf(event.values[0]),"Proximity",DeviceID);
            }
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                Publisher.main(String.valueOf(event.values[0]),"Light",DeviceID);
            }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
