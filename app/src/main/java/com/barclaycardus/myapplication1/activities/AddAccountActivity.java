package com.barclaycardus.myapplication1.activities;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.domains.Account;
import com.barclaycardus.myapplication1.domains.RegisterAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Ritesh on 9/13/2016.
 */
public class AddAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }

    public void registerAccount(View view) {
//        String stringUrl = urlText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            new HttpRequestTask(new RegisterAccountRequest("123123123", Collections.singletonList(getAccount())),this).execute((Void) null);
        }
        return;
    }

    private Account getAccount() {
        EditText edAccount = (EditText) findViewById(R.id.edAccountNumber);
        EditText edName = (EditText) findViewById(R.id.edName);
        EditText edCvv = (EditText) findViewById(R.id.edCvv);
        EditText edExpiryDate = (EditText) findViewById(R.id.edExpiryDate);

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
            progressDialog.setMessage("Doing something, please wait.");
            progressDialog.show();
        }

        @Override
        protected Account doInBackground(Void... params) {
            try {
                final String url = "https://barclays-cloud-server-1.appspot.com/save";
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // Add the identity Accept-Encoding header
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<?> requestEntity = new HttpEntity<Object>(registerAccountRequest, requestHeaders);


                ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if(exchange.getStatusCode() == HttpStatus.OK){
                    // parse and return object
                }

                return registerAccountRequest.getAccounts().get(0);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Account greeting) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    }

}
