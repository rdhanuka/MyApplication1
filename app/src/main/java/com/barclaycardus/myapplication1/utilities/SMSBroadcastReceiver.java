package com.barclaycardus.myapplication1.utilities;

/**
 * Created by Ritesh on 9/13/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.barclaycardus.myapplication1.activities.ConfirmPaymentActivity;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                String msg = "";
                for (int i = 0; i < pdus.length; i++) {
                    msg += SmsMessage.createFromPdu((byte[]) pdus[i]).getDisplayMessageBody();

                }
                if (messages.length > -1 && msg.contains("OTP")) {
                    Toast.makeText(context, "Message recieved: " + msg, Toast.LENGTH_LONG).show();
                    Intent popup = new Intent(context, ConfirmPaymentActivity.class);
                    popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    popup.putExtra("strMessage", msg);
                    context.startActivity(popup);
                }
            }
        }
    }
}