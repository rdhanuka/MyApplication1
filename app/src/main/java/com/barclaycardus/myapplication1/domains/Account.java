package com.barclaycardus.myapplication1.domains;

/**
 * Created by Ritesh on 9/18/2016.
 */
public class Account {

    private final String accountNumber;
    private final String name;
    private final String expDate;
    private final String cvv;

    public Account(String accountNumber, String name, String expiryDate, String cvv) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.expDate = expiryDate;
        this.cvv = cvv;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getCvv() {
        return cvv;
    }
}
