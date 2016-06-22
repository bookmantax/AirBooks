/*
 * First Class Tax app
 * Main activity class, used as the main menu of the application.
 */
package com.airbooks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class TaxDeductionsToDate extends AppCompatActivity implements View.OnClickListener{

    TextView deductionsToDate;
    private DatabaseHelper db = new DatabaseHelper(this);
    double totalDeductions = 0.00;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_deductions_to_date);

        deductionsToDate = (TextView) findViewById(R.id.deductions_To_Date_TextView);

        if(!db.isTripsEmpty()) {
            totalDeductions = db.allPerDiem();
        }

        deductionsToDate.setText("$ " + String.valueOf(totalDeductions));

    }


    @Override
    public void onClick(View v) {

    }
}


