package com.barclaycardus.myapplication1.domains;

import java.util.List;

/**
 * Created by Ritesh on 9/18/2016.
 */
public class RegisterAccountRequest {
    private String mobileNumber;
    private List<Account> accounts;

    public RegisterAccountRequest() {
    }

    public RegisterAccountRequest(String mobileNumber, List<Account> accounts) {
        this.mobileNumber = mobileNumber;
        this.accounts = accounts;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public List<Account> getAccounts() {
        return accounts;
    }


}
