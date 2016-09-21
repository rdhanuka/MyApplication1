package com.barclaycardus.myapplication1.activities;

import static android.accounts.AccountManager.*;
import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.domains.Address;
import com.barclaycardus.myapplication1.utilities.HttpUtils;

import org.springframework.http.ResponseEntity;

import java.util.UUID;
import java.util.function.Consumer;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.util.UUID.randomUUID;

public class RegistrationActivity extends AccountAuthenticatorActivity implements View.OnClickListener {

    private Button btnRequestSms, btnVerifyOtp;
    private EditText inputName, inputMobile, inputOtp;
    private ProgressBar progressBar;
    private LinearLayout layoutNumber, layoutOtp;
    private BroadcastReceiver broadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sms);
        setTitle("Login");


        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputName = (EditText) findViewById(R.id.inputName);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btnRequestSms = (Button) findViewById(R.id.btn_request_sms);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layoutNumber = (LinearLayout) findViewById(R.id.layout_sms);
        layoutOtp = (LinearLayout) findViewById(R.id.layout_otp);

        // view click listeners
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);

        // hiding the edit mobile number
        layoutOtp.setVisibility(View.GONE);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("otp");
                Log.i("OTP Received", "otp : " + message);
                inputOtp.setText(message);
                }};
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_sms:
                validateForm();
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;
        }
    }

    private void verifyOtp() {
        final String mobile = inputMobile.getText().toString().trim();
        final String otp = inputOtp.getText().toString().trim();
        String url = String.format("https://barclays-cloud-server-1.appspot.com/login?mobileNumber=%s&otp=%s", mobile, otp);
        new UserLoginTask(new Runnable() {
            @Override
            public void run() {
                createAccount(mobile,otp);
            }

        }).execute(url);
    }

    private void createAccount(String mobile, String otp) {
        final Intent res = new Intent();
        res.putExtra(KEY_ACCOUNT_NAME, mobile);
        res.putExtra(KEY_ACCOUNT_TYPE, "Barclays");
        res.putExtra(KEY_AUTH_TOKEN_LABEL, "Barclays Payment Token");
        res.putExtra(KEY_AUTHTOKEN,  randomUUID().toString());

        AccountManager mAccountManager = get(this);
        final Account account = new Account(mobile, res.getStringExtra(KEY_ACCOUNT_TYPE));

        mAccountManager.addAccountExplicitly(account, otp, res.getExtras());
        mAccountManager.setAuthToken(account, res.getStringExtra(KEY_AUTH_TOKEN_LABEL), res.getStringExtra(KEY_AUTHTOKEN));

        setAccountAuthenticatorResult(res.getExtras());
        setResult(RESULT_OK, res);
        finish();
    }
    /**
     * Validating user details form
     */
    private void validateForm() {
        String name = inputName.getText().toString().trim();
        String mobile = inputMobile.getText().toString().trim();

        // validating empty name and email
        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your details", Toast.LENGTH_SHORT).show();
            return;
        }

        // validating mobile number
        // it should be of 10 digits length
        if (isValidPhoneNumber(mobile)) {

            // requesting for sms
             requestForSMS(name, mobile);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestForSMS(String name, String mobile) {
        String url = String.format("https://barclays-cloud-server-1.appspot.com/login?mobileNumber=%s",mobile);
        new UserLoginTask(new Runnable() {
            @Override
            public void run() {
                layoutNumber.setVisibility(View.GONE);
                layoutOtp.setVisibility(View.VISIBLE);
            }
        }).execute(url);
    }

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("my-custom-event"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private class UserLoginTask extends AsyncTask {
        private Runnable consumer;

        public UserLoginTask(Runnable consumer) {
            this.consumer = consumer;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Intent doInBackground(Object[] params) {
            new HttpUtils().makeRequest(params[0].toString(),"some payload");
            return null;
        }

        @Override
        protected void onPostExecute(Object intent) {
            progressBar.setVisibility(View.GONE);
            consumer.run();
        }

    }

}
