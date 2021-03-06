package com.barclaycardus.myapplication1.utilities;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

/**
 * Created by Ritesh on 9/19/2016.
 */
public class HttpUtils {


    public ResponseEntity<String> makeRequest(String url, Object payload) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Add the identity Accept-Encoding header
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> exchange;
        if (payload != null) {
            HttpEntity<?> requestEntity = new HttpEntity<>(payload, requestHeaders);
            Log.e("post request :",url + requestEntity.getBody().toString());
            exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } else {
            Log.e("get request :",url);
            HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);
            exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        }
        Log.e("response from server",exchange.toString());
        return exchange;
    }
}
