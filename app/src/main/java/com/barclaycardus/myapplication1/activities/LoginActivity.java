package com.barclaycardus.myapplication1.activities;

import com.barclaycardus.myapplication1.R;

import java.util.UUID;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AccountManager accountManager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.mobileNumber);

        mPasswordView = (EditText) findViewById(R.id.password);

        accountManager = AccountManager.get(this);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
           AsyncTask mAuthTask = new UserLoginTask(email, password,accountManager);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private class UserLoginTask extends AsyncTask {
        private static final String PARAM_USER_PASS = "password";
        private final String email;
        private final String password;
        private AccountManager mAccountManager;

        public UserLoginTask(String email, String password, AccountManager accountManager) {

            this.email = email;
            this.password = password;
            this.mAccountManager = accountManager;
        }

        @Override
        protected Intent doInBackground(Object[] params) {
            String authtoken = UUID.randomUUID().toString(); //TODO: SERVICE CALL TO API
            final Intent res = new Intent();
            res.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
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

