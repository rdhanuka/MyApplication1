package com.barclaycardus.myapplication1.activities;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.domains.AsyncTaskResult;
import com.barclaycardus.myapplication1.utilities.HttpUtils;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ConfirmPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmpayment);
        setTheme(R.style.Theme_AppCompat_Translucent);

    }

    /**
     * Called when the user clicks the Submit
     */
    public void submitPayment(View v) {
        final String mobile = AccountManager.get(this).getAccountsByType("Barclays")[0].name;
        Intent intent = getIntent();

        String otp = intent.getStringExtra("otp");
        String url = String.format("https://barclays-cloud-server-1.appspot.com/paymentOtp?mobileNumber=%s&otp=%s", mobile, otp);
        new UserLoginTask(new Runnable() {
            @Override
            public void run() {
            }
        }).execute(url);
        intent.removeExtra("otp");
    }

    /**
     * Called when the user clicks the Cancel
     */
    public void cancelPayment(View view) {
        finish();
    }

    private class UserLoginTask extends AsyncTask {
        private Runnable consumer;

        public UserLoginTask(Runnable consumer) {
            this.consumer = consumer;
        }


        @Override
        protected Intent doInBackground(Object[] params) {
            try {
                new HttpUtils().makeRequest(params[0].toString(), "some payload");

        } catch (Exception e) {
            Log.e("AddPaymentFragment", e.getMessage(), e);
        }
            return null;
        }

        @Override
        protected void onPostExecute(Object intent) {
            finish();
            consumer.run();
        }

    }
}
