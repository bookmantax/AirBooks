/**
 * AirBooks app
 * DepartingDate class :
 * This class handles the date picker used for Departing in AddNewTrip.java
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class DepartingDate extends AppCompatActivity implements View.OnClickListener{


    // Variables
    private static Button button_sbm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departing_date);

    }

    @Override
    public void onClick(View v) {

    }
}


