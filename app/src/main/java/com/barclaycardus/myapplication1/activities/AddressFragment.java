package com.barclaycardus.myapplication1.activities;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.domains.Account;
import com.barclaycardus.myapplication1.domains.Address;
import com.barclaycardus.myapplication1.domains.RegisterAccountRequest;
import com.barclaycardus.myapplication1.utilities.HttpUtils;

import java.util.Collections;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static java.util.Collections.singletonList;

public class AddressFragment extends Fragment {

    private EditText edLine1, edLine2, edCity, edState, edZip;
    private Button sbmtAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup inflatedView = (ViewGroup) inflater.inflate(
                R.layout.activity_address, container, false);

        edLine1 = (EditText) inflatedView.findViewById(R.id.edLine1);
        edLine2 = (EditText) inflatedView.findViewById(R.id.edLine2);
        edCity = (EditText) inflatedView.findViewById(R.id.edCity);
        edState = (EditText) inflatedView.findViewById(R.id.edState);
        edZip = (EditText) inflatedView.findViewById(R.id.edZip);
        sbmtAddress = (Button) inflatedView.findViewById(R.id.sbmtAddress);

        sbmtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.accounts.Account mobile = AccountManager.get(getContext()).getAccountsByType("Barclays")[0];
                new HttpRequestTask(new RegisterAccountRequest(mobile.name, null, singletonList(getAddress())), getActivity()).execute((Void) null);
            }
        });

        return inflatedView;
    }

    @NonNull
    private Address getAddress() {
        return new Address(edLine1.getText().toString(),
                edLine2.getText().toString(),
                edCity.getText().toString(),
                edState.getText().toString(),
                edZip.getText().toString()
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
