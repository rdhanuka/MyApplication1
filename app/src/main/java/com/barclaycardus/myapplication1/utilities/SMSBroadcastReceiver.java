package com.barclaycardus.myapplication1.utilities;

/**
 * Created by Ritesh on 9/13/2016.
 */

import com.barclaycardus.myapplication1.activities.ConfirmPaymentActivity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

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



                  // if the SMS is not from our gateway, ignore the message
                    if (!senderNumber.equals("+13107766392")) {
                        return;
                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(msg);

                    Log.e(TAG, "OTP received: " + verificationCode);


                    if (msg.contains("login")) {
                        Intent intent1 = new Intent("my-custom-event");
                        intent1.putExtra("otp", verificationCode);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                    } else if (msg.contains("login")) {
                        Intent popup = new Intent(context, ConfirmPaymentActivity.class);
                        popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        popup.putExtra("otp", verificationCode);
                        context.startActivity(popup);
                    }


                }
            }
        }
    }


    private String getVerificationCode(String msg) {
        String[] split = msg.split(" ");
        return split[split.length - 1];
    }
}