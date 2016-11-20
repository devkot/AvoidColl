package com.example.stelios.ergasia1;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mSpeed, mProximity, mLight;
    private float last_x = 0, last_y = 0, last_z = 0;
    TextView acceleration, distance, light;
    NotificationCompat.Builder notification;
    private static final int id = 1;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSpeed = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        acceleration = (TextView)findViewById(R.id.acceleration);
        distance = (TextView)findViewById(R.id.distance);
        light = (TextView)findViewById(R.id.light);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true); //deletes notification on main app screen
        if (!isTaskRoot()) { //prevent additional tasks from opening on intent click
            finish();
        }


    }
    public void Notitriggered(String text){                       //create notification
        notification.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        notification.setTicker("Alert");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Collision Danger");
        notification.setContentText(text);
        notification.setDefaults(Notification.DEFAULT_VIBRATE); //vibrate
        notification.setPriority(NotificationCompat.PRIORITY_MAX);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //notification click
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //build and send notification
        nm.notify(id, notification.build());
        Context context = getApplicationContext();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.play();
    }
    public void Notidestroy(){ //destroy notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();
        Context context = getApplicationContext();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                this.startActivity(intent);
                break;
            case R.id.delay_settings:
                Intent intent1 = new Intent(this, DelaySettings.class);
                this.startActivity(intent1);
                break;
            case R.id.exit:
                onStop();
                System.exit(0);
                break;
        }
        return false;
    }

    long lastUpdateA = System.currentTimeMillis(), lastUpdateP=System.currentTimeMillis(), lastUpdateL=System.currentTimeMillis();
    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTimeA = System.currentTimeMillis(),curTimeP= System.currentTimeMillis(), curTimeL=System.currentTimeMillis();
        if (event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){   //linear acceleration used to filter out gravity
            if ((curTimeA - lastUpdateA) > DelaySettings.delayA) {
                acceleration.setText("Linear Acceleration\n" + "X: " + event.values[0] + " m/s^2" + "\nY: " + event.values[1] + " m/s^2" + "\nZ: " + event.values[2] + " m/s^2");
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float speed = Math.abs(x + y + z - last_x - last_y - last_z);
                if (speed > Settings.acceleration_value) {
                    Notitriggered("Acceleration too high");
                } else Notidestroy();
                last_x = x;
                last_y = y;
                last_z = z;
                lastUpdateA = curTimeA;
            }
        }
        if(event.sensor.getType()== Sensor.TYPE_PROXIMITY) {
            distance.setText("Proximity:\n" + String.valueOf(event.values[0]) + " cm");
            if (event.values[0] == 0){
                Notitriggered("Object close");
            }
            else Notidestroy();
        }
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            light.setText("Lighting:\n" + event.values[0] + " lx");
                if (event.values[0] < Settings.light_value) {
                    Notitriggered("Lighting too low");
                }
                else Notidestroy();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
    //  mSensorManager.unregisterListener(this); //Uncomment if we want to release the sensors for less power consumption
    }
    @Override
    protected void onResume() {
        super.onResume();/*
        switch(DelaySettings.value){ //switch for acceleration delay
            case 0:
                mSensorManager.registerListener(this, mSpeed, SensorManager.SENSOR_DELAY_FASTEST);  // 0 delay
                break;
            case 1:
                mSensorManager.registerListener(this, mSpeed, SensorManager.SENSOR_DELAY_NORMAL); //5 readings per sec
                break;
            case 2:
                mSensorManager.registerListener(this, mSpeed, 100000000);  //once per second (in microseconds)
                break;
            case 3:
                mSensorManager.registerListener(this, mSpeed, 500000000);  //once per 5 seconds (in microseconds)
                break;
        }
        switch(DelaySettings.valuep){ //switch statement for proximity sensor delay
            case 0:
                mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_FASTEST); // 0 delay
                break;
            case 1:
                mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL); //5 readings per sec
                break;
            case 2:
                mSensorManager.registerListener(this, mProximity, 100000000);
                break;
            case 3:
                mSensorManager.registerListener(this, mProximity, 500000000);
                break;
        }
        switch(DelaySettings.valuel){ //switch statement for light sensor delay
            case 0:
                mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST); // 0 delay
                break;
            case 1:
                mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL); //5 readings per sec
                break;
            case 2:
                mSensorManager.registerListener(this, mLight, 100000000);
                break;
            case 3:
                mSensorManager.registerListener(this, mLight, 500000000);
                break;
        } */
        mSensorManager.registerListener(this, mSpeed, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }
    @Override
    protected void onStop () {
        super.onStop();
    }
}