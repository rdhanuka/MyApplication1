package com.barclaycardus.myapplication1.activities;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.utilities.HttpUtils;
import com.barclaycardus.myapplication1.utilities.SMSService;
import com.barclaycardus.myapplication1.utilities.ServiceCallbacks;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via mobileNumber/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity implements ServiceCallbacks {

    // UI references.
    private AutoCompleteTextView mMobileView;
    private EditText mPasswordView;
    private boolean bound;


    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mMobileView = (AutoCompleteTextView) findViewById(R.id.mobileNumber);

        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        bound = false;
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("otp");
                Log.e("LocalBroadcastManager", "otp : " + message);
                responseReceived();
            }
        };

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid mobileNumber, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mMobileView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String mobile = mMobileView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid mobileNumber.
        if (TextUtils.isEmpty(mobile)) {
            mMobileView.setError(getString(R.string.error_field_required));
            focusView = mMobileView;
            cancel = true;
        } else if (!isEmailValid(mobile)) {
            mMobileView.setError(getString(R.string.error_invalid_number));
            focusView = mMobileView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
          /*  AsyncTask mAuthTask = new UserLoginTask(mobile, password, this, progressDialog);
            mAuthTask.execute((Void) null);*/
        }
    }

    private boolean isEmailValid(String number) {
        return !number.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password.length() == 4;
    }

    ProgressDialog progressDialog;

    @Override
    public void responseReceived() {
        Log.e("apple", "response recieved called");
        bound = true;
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!bound){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("creating account, please wait.");
        progressDialog.show();
        }
    }


    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("my-custom-event"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPause();
    }


    private class UserLoginTask extends AsyncTask {
        private static final String PARAM_USER_PASS = "password";
        private final String mobileNumber;
        private final String password;
        private final ProgressDialog progressDialog;
        private AccountManager mAccountManager;

        public UserLoginTask(String mobileNumber, String password, Context mContext, ProgressDialog dialog) {

            this.mobileNumber = mobileNumber;
            this.password = password;
            this.mAccountManager = AccountManager.get(mContext);
            this.progressDialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("creating account, please wait.");
            progressDialog.show();
        }

        @Override
        protected Intent doInBackground(Object[] params) {
            String createUrl = "https://barclays-cloud-server-1.appspot.com/login?mobileNumber=7042576168";
            new HttpUtils().makeRequest(createUrl);

            //TODO: wait for receiving OTP

            String loginUrl = "https://barclays-cloud-server-1.appspot.com/login?mobileNumber=7042576168&otp=1111";
            ResponseEntity<String> json = new HttpUtils().makeRequest(loginUrl);
            String authtoken = UUID.randomUUID().toString(); //TODO: SERVICE CALL TO API
            final Intent res = new Intent();
            res.putExtra(AccountManager.KEY_ACCOUNT_NAME, mobileNumber);
            res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, "Barclays");
            res.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
            res.putExtra(AccountManager.KEY_AUTH_TOKEN_LABEL, "Barclays Payment Token");
            res.putExtra(PARAM_USER_PASS, password);
            return res;
        }

        @Override
        protected void onPostExecute(Object intent) {
            finishLogin((Intent) intent);
        }

        private void finishLogin(Intent intent) {

            String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
            final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = intent.getStringExtra(AccountManager.KEY_AUTH_TOKEN_LABEL);

            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);

            setAccountAuthenticatorResult(intent.getExtras());  //TODO: check this
            setResult(RESULT_OK, intent);
            finish();

        }
    }
}

