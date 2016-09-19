package com.barclaycardus.myapplication1.domains;

import java.io.Serializable;

/**
 * Created by Ritesh on 9/13/2016.
 */
public class Address implements Serializable {
    private final String line1;
    private final String line2;
    private final String city;
    private final String state;
    private final String zip;

    public Address(String line1, String line2, String city, String state, String zip) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }
}

