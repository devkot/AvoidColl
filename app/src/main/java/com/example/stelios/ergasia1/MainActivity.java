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
    private SensorManager mSensorManager; //set sensor manager
    private Sensor mSpeed, mProximity, mLight; //declare sensors
    private float last_x = 0, last_y = 0, last_z = 0; //initialize accelerometer speed values
    TextView acceleration, distance, light; //declare textview
    NotificationCompat.Builder notification; //declare notification builder
    private static final int id = 1; //initialize notification id

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare sensors and text view
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSpeed = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        acceleration = (TextView)findViewById(R.id.acceleration);
        distance = (TextView)findViewById(R.id.distance);
        light = (TextView)findViewById(R.id.light);

        notification = new NotificationCompat.Builder(this); //create notification builder
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
        notification.setPriority(NotificationCompat.PRIORITY_MAX); //make it heads-up notification

        Intent intent = new Intent(this, MainActivity.class); //create intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //notification click
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //build notification
        nm.notify(id, notification.build()); //issue notification
        Context context = getApplicationContext();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.play(); //build notification sound and play
    }
    public void Notidestroy(){ //destroy notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll(); //cancels all notifications
        Context context = getApplicationContext();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        r.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //create menu
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){ //switch classes
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class); //go to threshold settings
                this.startActivity(intent);
                break;
            case R.id.delay_settings:
                Intent intent1 = new Intent(this, DelaySettings.class); //go to delay
                this.startActivity(intent1);
                break;
            case R.id.exit:
                onStop();
                System.exit(0); //exit app without dialog
                break;
        }
        return false;
    }

    long lastUpdateA=System.currentTimeMillis(), lastUpdateP=System.currentTimeMillis(), lastUpdateL=System.currentTimeMillis();
    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        if (event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){   //linear acceleration used to filter out gravity

            if ((curTime - lastUpdateA) > DelaySettings.delayA) { //checks delay
                acceleration.setText("Linear Acceleration\n" + "X: " + event.values[0] + " m/s^2" + "\nY: " + event.values[1] + " m/s^2" + "\nZ: " + event.values[2] + " m/s^2");
                float x = event.values[0]; float y = event.values[1]; float z = event.values[2]; //accelerometer values
                float speed = Math.abs(x + y + z - last_x - last_y - last_z); //calculate speed (absolute values)

                if (speed > Settings.acceleration_value) { //check if speed > threshold
                    Notitriggered("Acceleration too high"); //danger
                } else Notidestroy();

                last_x = x; last_y = y; last_z = z; lastUpdateA = curTime; //save last readings
            }
        }
        if(event.sensor.getType()== Sensor.TYPE_PROXIMITY) { //proximity sensor
          //  distance.setText("Proximity:\n" + String.valueOf(event.values[0]) + " cm");
            if((curTime - lastUpdateP) > DelaySettings.delayP) { //checks delay
                distance.setText("Proximity:\n" + String.valueOf(event.values[0]) + " cm");
                if (event.values[0] == 0) { //check threshold
                    Notitriggered("Object close");
                } else Notidestroy();

                lastUpdateP= curTime;

            }
        }
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){ //light sensor

            if((curTime - lastUpdateL) > DelaySettings.delayL) { //check delay
                light.setText("Lighting:\n" + event.values[0] + " lx");
                if (event.values[0] < Settings.light_value) { //check threshold
                    Notitriggered("Lighting too low");
                } else Notidestroy();
                lastUpdateL=curTime;
            }
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
        super.onResume();
        mSensorManager.registerListener(this, mSpeed, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onBackPressed() { //exit app through back button
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit") //build dialog
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //exit on click
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