package com.example.stelios.ergasia1;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;

public class ProximityService extends Service implements SensorEventListener{
    static float valuep;
    private AsyncTask ProxTask;
    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
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
        valuep = event.values[0];//save values
        ProxTask = new ProximitySensorEventLoggerTask().execute(event);//pass to async task to publish
        // stop the service
        stopSelf();
    }

    private class ProximitySensorEventLoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            Publisher.main(String.valueOf((valuep))+"/"
                    +String.valueOf(OnMode.latitude)+"/"+String.valueOf(OnMode.longitude),"Proximity",MainActivity.DeviceID);
            return null;
        }
    }

    public boolean stopService(Intent name) {
        ProxTask.cancel(true);
        return super.stopService(name);
    }

}
