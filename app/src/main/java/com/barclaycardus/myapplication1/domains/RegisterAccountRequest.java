package com.barclaycardus.myapplication1.domains;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ritesh on 9/18/2016.
 */
public class RegisterAccountRequest {
    private String mobileNumber;
    private List<Account> accounts = new ArrayList<>();
    private List<Address> addressList = new ArrayList<>();

    public RegisterAccountRequest(String mobileNumber, List<Account> accounts, List<Address> addresses) {

        this.mobileNumber = mobileNumber;
        this.accounts = accounts;
        addressList = addresses;
    }

    ;

    public RegisterAccountRequest(String mobileNumber, Account account, Address address) {
        this.mobileNumber = mobileNumber;
        accounts.add(account);
        addressList.add(address);
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public RegisterAccountRequest() {
    }

    public List<Address> getAddressList() {
        return addressList;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
