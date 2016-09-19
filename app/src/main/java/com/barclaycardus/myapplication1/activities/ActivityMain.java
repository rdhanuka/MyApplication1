package com.barclaycardus.myapplication1.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.barclaycardus.myapplication1.R;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("Barclays");

      if(accounts.length == 0){
          Intent  intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        super.onStart();

    }

    public void launch(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.launchRegisterAccount:
                intent = new Intent(this, AddAccountActivity.class);
                break;
            case R.id.launchAddAddress:
                intent = new Intent(this, AddressActivity.class);
                break;
        }
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            //add the function to perform here
            return(true);
        case R.id.reset:
            //add the function to perform here
            return(true);
        case R.id.about:
            //add the function to perform here
            return(true);
        case R.id.exit:
            //add the function to perform here
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }
}
