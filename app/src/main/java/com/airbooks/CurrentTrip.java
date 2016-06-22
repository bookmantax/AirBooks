/*
 * First Class Tax app
 * Main activity class, used as the main menu of the application.
 */
package com.airbooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CurrentTrip extends AppCompatActivity implements View.OnClickListener{


    // Variables
    private static Button button_sbm;
    private static Button button_sbm2;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_current_trip);

            OnClickButtonListenerLocation();
            OnClickButtonListenerDepartingDate();

        }

    // Current Trip Listener
    public void OnClickButtonListenerLocation() {
        button_sbm = (Button) findViewById(R.id.current_Trip_Location_Btn);
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.airbooks.LocationManager");
                        startActivity(intent);
                    }
                }
        );
    }

    // Current Trip Listener
    public void OnClickButtonListenerDepartingDate() {
        button_sbm2 = (Button) findViewById(R.id.current_Trip_Departing_Btn);
        button_sbm2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.airbooks.AddNewTrip");
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {

    }
}


