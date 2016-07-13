/*
 * First Class Tax app
 * Main activity class, used as the main menu of the application.
 */
package com.airbooks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TaxDeductionsToDate extends AppCompatActivity implements View.OnClickListener{

    // Variables
    TextView deductionsToDate;
    private DatabaseHelper db = new DatabaseHelper(this);
    double totalDeductions = 0.00;
    Cursor cursor;
    // Options Menu Variables
    private static final int EDIT_PROFILE = Menu.FIRST + 1;
    private static final int ABOUT = Menu.FIRST + 2;

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Options menu to shout about info
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, EDIT_PROFILE, Menu.NONE, "Edit Profile").setAlphabeticShortcut('e');
        menu.add(Menu.NONE, ABOUT, Menu.NONE, "About").setAlphabeticShortcut('?');
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT_PROFILE:
                Edit_Profile();
                return (true);
            case ABOUT:
                about();
                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    private void Edit_Profile() {
        Intent intent = new Intent("com.airbooks.EditUserInfo");
        startActivity(intent);
    }
    private void about() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View addView = inflater.inflate(R.layout.about, null);
        new AlertDialog.Builder(this)
                .setTitle(R.string.about)
                .setView(addView)
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // ignore, just dismiss
                            }
                        })
                .show();
    }
}


