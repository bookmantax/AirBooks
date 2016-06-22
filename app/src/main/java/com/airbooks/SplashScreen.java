package com.airbooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
//            myDB.fillAirportsTable(ctx, R.raw.airports);
            myDB.fillAirportsTable(ctx, R.raw.int_airports);
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
