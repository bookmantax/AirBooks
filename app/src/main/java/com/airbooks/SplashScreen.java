/**
 * AirBooks app
 * SplashScreen class :
 * Used for branding.
 * It also check if the UserInfo Table from the database is empty or not to decide if
 * displays the AddUser or the MainActivity.
 * It also verify the Airports and perDiem tables are empty or not, if they are empty will invoke the corresponding
 * methods from the DatabaseHelper class to fill them with the information stored in the
 * Comma Separated Values (CSV) files located inside the raw folder.
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashScreen extends Activity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        String userTable = "user_info_table";
        String airportsTable = "airports_table";
        String perDiemTable = "per_diem_table";

        myDB = new DatabaseHelper(this);
        boolean checkUser = myDB.isEmpty(userTable);

        if (checkUser == true) {
            Thread myThread = new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        Intent startMainActivity = new Intent(getApplicationContext(), AddUserInfo.class);
                        startActivity(startMainActivity);
                        finish();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            myThread.start();
        } else {
            Thread myThread = new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        Intent startMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(startMainActivity);
                        finish();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            myThread.start();
        }

        boolean checkAirports = myDB.isEmpty(airportsTable);
        boolean checkPerDiem = myDB.isEmpty(perDiemTable);


        Context ctx = getApplicationContext();

        if (checkPerDiem == true){
            myDB.fillPerDiemTable(ctx, R.raw.per_diem);
        } else {
            // ignore
        }

        if (checkAirports == true){
            myDB.fillAirportsTable(ctx, R.raw.airports);
//            myDB.fillAirportsTable(ctx, R.raw.int_airports);
        } else {
            // ignore
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
