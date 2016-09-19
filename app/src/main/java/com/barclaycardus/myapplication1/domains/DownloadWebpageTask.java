package com.barclaycardus.myapplication1.domains;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Ritesh on 9/18/2016.
 */
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    public DownloadWebpageTask(Object value) {
        this.value = value;
    }

    Object value;
    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            if(urls.length <3){
                downloadUrl(urls[0],urls[1],null);
            }
            return downloadUrl(urls[0],urls[1], urls[1]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        System.out.printf("asd"+result);
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl, String requestMethod, Object value) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);

            //Create connection
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");


            if ("POST".equals(requestMethod)) {
                // Send post request
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(value.toString());
                wr.flush();
                wr.close();
            }else {
            // Starts the query
            conn.connect();
            }




            int response = conn.getResponseCode();

            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream is) throws IOException, UnsupportedEncodingException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    }
}


