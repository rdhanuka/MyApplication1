package com.barclaycardus.myapplication1.utilities;

/**
 * Created by Ritesh on 9/13/2016.
 */

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    private static BlockingQueue queue = new ArrayBlockingQueue(5);

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];

                String msg = "";
                String senderNumber = "";
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    senderNumber = messages[i].getDisplayOriginatingAddress();
                    msg += messages[i].getDisplayMessageBody();
                }

                if (messages.length > -1 && msg.contains("OTP")) {
                    Toast.makeText(context, "Message recieved: " + msg, Toast.LENGTH_LONG).show();
                    /*Intent popup = new Intent(context, ConfirmPaymentActivity.class);
                    popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    popup.putExtra("strMessage", msg);
                    context.startActivity(popup);*/


                  /*  // if the SMS is not from our gateway, ignore the message
                    if (!senderNumber.toLowerCase().contains("")) {
                        return;
                    }*/

                    // verification code from sms
                    String verificationCode = getVerificationCode(msg);

                    Log.e(TAG, "OTP received: " + verificationCode);


                    Intent intent1 = new Intent("my-custom-event");
                    intent1.putExtra("otp", verificationCode);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

                   Intent hhtpIntent = new Intent(context, SMSService.class);

                }
            }
        }
    }




    private String getVerificationCode(String msg) {
        return "1111";
    }
}