package com.example.stelios.ergasia1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationListener;


//google settings api documentation
public class OnMode extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private GoogleApiClient googleApiClient; //google api for gps
    private Switch myswitch;
    static double latitude;
    static Location mLastLocation;
    static double longitude;
    static LatLng latLng;
    private static Handler hm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_mode);
        Context context = getApplicationContext();
        myswitch = (Switch) findViewById(R.id.switch2);
        myswitch.setChecked(false);

        hm = new Handler() {//create handler
            @Override
            public void handleMessage(Message m) {
                Toast.makeText(OnMode.this,"Collision Danger",Toast.LENGTH_LONG).show();
            }
        };

        final LocationManager LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);//gps manager

        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);//wifi manager
        if (!wifi.isWifiEnabled()) {//is it isn't enabled, turn it on and show toast
            wifi.setWifiEnabled(true);
            Toast.makeText(context, "Wi-Fi enabled", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, "Wi-Fi is already enabled", Toast.LENGTH_SHORT).show();//else inform the user

        if (LM.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//inform the user that gps was turned on
            Toast.makeText(context, "GPS is already enabled", Toast.LENGTH_SHORT).show();
        }


//following piece of code is taken from google settings api
        if (googleApiClient == null) {
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
                if (isChecked) {
                    Intent intent3 = new Intent(getApplicationContext(), SubService.class);
                    startService(intent3);
                    Intent intent1 = new Intent(getApplicationContext(), ProximityService.class);
                    startService(intent1);
                    Intent intent2 = new Intent(getApplicationContext(), LightService.class);
                    startService(intent2);
                    Intent intent = new Intent(getApplicationContext(), AccelerationService.class);
                    startService(intent);

                } else {


                }

            }
        });


    }//oncreate end

    public static Handler returnHandler(){ //return handler
        return hm;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Init.getInstance().setConnectivityListener(this);//listener for broadcast receiver
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();//disc client
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //create menu
        inflater.inflate(R.menu.online_menu, menu); //get resource
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { //switch classes
            case R.id.mqtt_settings://go to mqtt settings
                Intent intent = new Intent(this, MQTTSettings.class);
                this.startActivity(intent);
                break;
            case R.id.offline: //button to go offline
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
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
                        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(false);//broadcaster will terminate activity
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            latitude=(mLastLocation.getLatitude());
            longitude=(mLastLocation.getLongitude());
            latLng = new LatLng(latitude, longitude);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) { //on connection change we terminate the activity
        if(!isConnected){
            Toast.makeText(getApplicationContext(), "No internet connectivity, Online Mode terminating", Toast.LENGTH_SHORT).show();
            finish();
        }
    }




}
