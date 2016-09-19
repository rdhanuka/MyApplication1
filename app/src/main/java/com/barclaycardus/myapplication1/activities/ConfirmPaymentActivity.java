package com.barclaycardus.myapplication1.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.barclaycardus.myapplication1.R;

public class ConfirmPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmpayment);
    }

    /**
     * Called when the user clicks the Submit
     */
    public void submitPayment(View view) {
        // Do something in response to button

        Toast.makeText(this,"sending api call to confirm payment", Toast.LENGTH_LONG).show();
    }

    /**
     * Called when the user clicks the Cancel
     */
    public void cancelPayment(View view) {
        // Do something in response to button

        Toast.makeText(this,"sending api call to cancel payment", Toast.LENGTH_LONG).show();
    }
}
