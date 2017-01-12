package com.example.stelios.ergasia1;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;

/**
 * Created by stelios on 3/1/2017.
 */

public class LightService extends Service implements SensorEventListener{
    static float valuel;

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // grab the values and timestamp -- off the main thread
        long timestamp = event.timestamp;
        valuel = event.values[0];//save values
        new LightSensorEventLoggerTask().execute(event);//pass to async task to publish
        // stop the service
        stopSelf();
    }

    private class LightSensorEventLoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            //SensorEvent event = events[0];
            // log the value
            Publisher.main(String.valueOf((valuel))+"/"
                    +String.valueOf(OnMode.latitude)+"/"+String.valueOf(OnMode.longitude),"Light",MainActivity.DeviceID);
            return null;
        }
    }

}
