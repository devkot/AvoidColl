package com.example.stelios.ergasia1;

import android.app.Application;

/**
 * Created by Stelios on 20/12/2016.
 */

public class Init extends Application {
    private static Init mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Init getInstance() {
        return mInstance;
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
