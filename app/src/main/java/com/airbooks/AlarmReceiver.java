package com.airbooks;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by unlimited_power on 7/6/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    // Variables

    boolean isAtHomeArea = false;
    int  miles;
    Double currentLatitude, currentLongitude, homeLatitude, homeLongitude, perDiem, distance, homeArea = 75.0;
    String country, state, city;
    boolean tripRecorded = false;
    private LocationManager mLocationManager = null;

// TODO : check device wakeUp https://developer.android.com/reference/android/support/v4/content/WakefulBroadcastReceiver.html

    @Override
    public void onReceive(Context context, Intent intent) {

        GetLocation gL = new GetLocation(context);
        MainActivity mA = new MainActivity();

        String[] locationStringArray = gL.whereAmI(context);

        if (locationStringArray != null) {
            currentLatitude = Double.parseDouble(locationStringArray[0]);
            currentLongitude = Double.parseDouble(locationStringArray[1]);
            homeLatitude = Double.parseDouble(locationStringArray[2]);
            homeLongitude = Double.parseDouble(locationStringArray[3]);
            country = locationStringArray[4];
            state = locationStringArray[5];
            city = locationStringArray[6];
            perDiem = Double.parseDouble(locationStringArray[7]);


            // if array is not null
            distance = distance(homeLatitude, homeLongitude, currentLatitude, currentLongitude);

            if (distance > homeArea) {
                isAtHomeArea = false;
            } else {
                isAtHomeArea = true;
            }

            if (isAtHomeArea == false) {
                String location = country + " - "
//                                    + state + " - "
                                    + city;
                tripRecorded = gL.recordTrip(location, perDiem);

                if (tripRecorded == true) {
                    gL.stopUsingGPS();
                } else {
                    gL.whereAmI(context);
                    gL.recordTrip(location, perDiem);
                    mA.startAlarm();
                }
            } else {
                // Don't Record location
            }

            //    Set alarm when boot completed
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                mA.startAlarm();
            }

        } else {
            /**
             * To launch the application (open)
             * http://stackoverflow.com/questions/3343432/how-do-i-programmatically-launch-a-specific-application-in-android
             * REF:https://blog.alexwendland.com/2013/alarmmanager-broadcastreceivers-activities/
             * https://developer.android.com/reference/android/content/Intent.html
             */
            Intent i = context.getPackageManager().getLaunchIntentForPackage("com.airbooks");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        }
    }


    // Haversine Formula: http://www.movable-type.co.uk/scripts/latlong.html
    public double distance(double homeLat, double homeLong, double currentLat, double currentLong){

        int R = 6371; // km
        double dLat = toRadians(currentLat - homeLat);
        double dLon = toRadians(currentLong - homeLong);
        homeLat = toRadians(homeLat);
        currentLat = toRadians(currentLat);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(homeLat) * Math.cos(currentLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public double toRadians(Double deg) {
        return deg * (Math.PI/180);
    }



}
