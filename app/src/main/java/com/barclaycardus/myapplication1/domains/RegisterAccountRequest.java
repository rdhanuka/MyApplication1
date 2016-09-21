package com.barclaycardus.myapplication1.domains;

import java.util.List;

/**
 * Created by Ritesh on 9/18/2016.
 */
public class RegisterAccountRequest {
    private String mobileNumber;
    private List<Account> accounts;
    private List<Address> addresses;

    public RegisterAccountRequest() {
    }



    public RegisterAccountRequest(String mobileNumber, List<Account> accounts,List<Address> addresses) {
        this.mobileNumber = mobileNumber;
        this.accounts = accounts;
        this.addresses = addresses;

    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public List<Account> getAccounts() {
        return accounts;
    }


}
