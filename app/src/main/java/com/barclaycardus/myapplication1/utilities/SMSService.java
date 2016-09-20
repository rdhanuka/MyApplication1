package com.barclaycardus.myapplication1.utilities;


import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Ritesh
 */
public class SMSService extends IntentService {

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;
    public SMSService() {
        super("SMSService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("service called", intent.toString());
            if (serviceCallbacks != null) {
                serviceCallbacks.responseReceived();
            }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        serviceCallbacks = null;
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }


    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public SMSService getService() {
            // Return this instance of MyService so clients can call public methods
            return SMSService.this;
        }
    }
}
