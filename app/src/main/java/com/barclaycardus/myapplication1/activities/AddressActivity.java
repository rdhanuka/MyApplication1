package com.barclaycardus.myapplication1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.barclaycardus.myapplication1.R;
import com.barclaycardus.myapplication1.domains.Address;

public class AddressActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.ADRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address);
        setTitle("Address");
    }

    /**
     * Called when the user clicks the Submit
     */
    public void submitAddress(View view) {
        // Do something in response to button

        Intent intent = new Intent(this, RegistrationActivity.class);

        intent.putExtra(EXTRA_MESSAGE, getAddress());
        startActivity(intent);
    }

    @NonNull
    private Address getAddress() {
        EditText edLine1 = (EditText) findViewById(R.id.edLine1);
        EditText edLine2 = (EditText) findViewById(R.id.edLine2);
        EditText edCity = (EditText) findViewById(R.id.edCity);
        EditText edState = (EditText) findViewById(R.id.edState);
        EditText edZip = (EditText) findViewById(R.id.edZip);

        return new Address(edLine1.getText().toString(),
                edLine2.getText().toString(),
                edCity.getText().toString(),
                edState.getText().toString(),
                edZip.getText().toString()
                );
    }

}
