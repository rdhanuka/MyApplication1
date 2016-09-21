package com.barclaycardus.myapplication1.activities;

import static android.widget.Toast.LENGTH_SHORT;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.domains.Account;
import com.barclaycardus.myapplication1.domains.RegisterAccountRequest;
import com.barclaycardus.myapplication1.utilities.HttpUtils;

import java.util.Collections;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.util.Collections.singletonList;

/**
 * Created by Ritesh on 9/13/2016.
 */
public class AddAccountFragment extends Fragment {

    EditText edAccount, edName, edCvv, edExpiryDate;
    private Button btRegisterAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_account, container, false);

        edAccount = (EditText) rootView.findViewById(R.id.edAccountNumber);
        edName = (EditText) rootView.findViewById(R.id.edName);
        edCvv = (EditText) rootView.findViewById(R.id.edCvv);
        edExpiryDate = (EditText) rootView.findViewById(R.id.edExpiryDate);
        btRegisterAccount = (Button) rootView.findViewById(R.id.registerAccount);
        btRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.accounts.Account mobile = AccountManager.get(getContext()).getAccountsByType("Barclays")[0];
                new HttpRequestTask(new RegisterAccountRequest(mobile.name, singletonList(getAccount()),null), getActivity()).execute((Void) null);
            }
        });

        return rootView;
    }

    /* public void registerAccount(View view) {
//        String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            new HttpRequestTask(new RegisterAccountRequest("123123123", Collections.singletonList(getAccount())),this).execute((Void) null);
        }
        return;
    }*/

    private Account getAccount() {
        return new Account(edAccount.getText().toString(),
                edName.getText().toString(),
                edExpiryDate.getText().toString(),
                edCvv.getText().toString()
        );
    }


    private class HttpRequestTask extends AsyncTask<Void, Void, Account> {

        private final ProgressDialog progressDialog;
        RegisterAccountRequest registerAccountRequest;

        public HttpRequestTask(RegisterAccountRequest registerAccountRequest, Context mContext) {
            this.registerAccountRequest = registerAccountRequest;
            progressDialog = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("please wait.");
            progressDialog.show();
        }

        @Override
        protected Account doInBackground(Void... params) {
            try {
                final String url = "https://barclays-cloud-server-1.appspot.com/save";

                new HttpUtils().makeRequest(url, registerAccountRequest);

                return registerAccountRequest.getAccounts().get(0);
            } catch (Exception e) {
                Log.e("AddAccountFragment", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Account account) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    }

}
