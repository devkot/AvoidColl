package com.example.stelios.ergasia1;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;


public class AccelerationService extends Service implements SensorEventListener {
    static float valuex,valuey, valuez;
    private AsyncTask AccTask;
    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // grab the values off the main thread
        valuex = event.values[0]; valuey=event.values[1]; valuez=event.values[2];//save values

        AccTask = new SensorEventLoggerTask().execute(event);//pass to async task to publish
        // stop the service
        stopSelf();
    }

    private class SensorEventLoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            Publisher.main(String.valueOf((valuex))+"/"+String.valueOf((valuey))+"/"+ String.valueOf((valuez))+"/"
                    +String.valueOf(OnMode.latitude)+"/"+String.valueOf(OnMode.longitude),
                    "Acceleration",
                    MainActivity.DeviceID);
            return null;
        }


    }
    public boolean stopService(Intent name) {
        AccTask.cancel(true);//stop task
        return super.stopService(name);
    }

}
