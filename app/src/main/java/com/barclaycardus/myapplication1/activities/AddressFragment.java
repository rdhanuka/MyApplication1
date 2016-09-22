package com.barclaycardus.myapplication1.activities;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.domains.Address;
import com.barclaycardus.myapplication1.domains.AsyncTaskResult;
import com.barclaycardus.myapplication1.domains.RegisterAccountRequest;
import com.barclaycardus.myapplication1.utilities.HttpUtils;

import java.util.Collections;

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
import android.widget.Toast;

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
                new HttpRequestTask(new RegisterAccountRequest(getRegistrationRequest().getMobileNumber(), getRegistrationRequest().getAccounts(), Collections.singletonList(getAddress())), getActivity()).execute((Void) null);
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

    private void performFinishAction(RegisterAccountRequest request) {

        getRegistrationRequest().setAddressList(request.getAddressList());
    }

    private RegisterAccountRequest getRegistrationRequest() {
        return ((ActivityMain) getActivity()).getRegisterationRequest();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, AsyncTaskResult> {

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
        protected AsyncTaskResult doInBackground(Void... params) {
            try {
                final String url = "https://barclays-cloud-server-1.appspot.com/save";

                new HttpUtils().makeRequest(url, registerAccountRequest);

                return new AsyncTaskResult(registerAccountRequest);
            } catch (Exception e) {
                Log.e("AddAddressFragment", e.getMessage(), e);
                return new AsyncTaskResult(e);
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult account) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (account.getError() != null) {
                Toast.makeText(getActivity(), "something went wrong.", Toast.LENGTH_SHORT).show();
                return;
            }
            performFinishAction(registerAccountRequest);
        }
    }

}
