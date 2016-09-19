package com.barclaycardus.myapplication1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Ritesh on 9/17/2016.
 */
public class UdinicAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        UdinicAuthenticator authenticator = new UdinicAuthenticator(this);
        return authenticator.getIBinder();
    }
}