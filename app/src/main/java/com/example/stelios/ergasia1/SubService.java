package com.example.stelios.ergasia1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SubService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Subscriber.main("Acceleration/Proximity/Light","AndroidDevice");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
