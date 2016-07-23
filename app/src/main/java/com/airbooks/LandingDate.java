/**
 * AirBooks app
 * LandingDate class :
 * This class handles the date picker used for Landing in AddNewTrip.java
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class LandingDate extends Activity {

    // Variables
    private static Button button_sbm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_date);
    }
}


