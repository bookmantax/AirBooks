package com.airbooks;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
    int  miles; // Miles ( it might need to be converted to meters)
    Double currentLatitude, currentLongitude, homeLatitude, homeLongitude, perDiem, distance, homeArea = 75.0;
    String country, state, city;
    private LocationManager mLocationManager = null;

// TODO : check device wakeUp https://developer.android.com/reference/android/support/v4/content/WakefulBroadcastReceiver.html

    @Override
    public void onReceive(Context context, Intent intent) {


        GetLocation gL = new GetLocation(context);
        String[] locationStringArray = gL.whereAmI(context);

        currentLatitude = Double.parseDouble(locationStringArray[0]);
        currentLongitude = Double.parseDouble(locationStringArray[1]);
        homeLatitude = Double.parseDouble(locationStringArray[2]);
        homeLongitude = Double.parseDouble(locationStringArray[3]);
        country = locationStringArray[4];
        state = locationStringArray[5];
        city = locationStringArray[6];
        perDiem = Double.parseDouble(locationStringArray[7]);

        distance = distance(homeLatitude, homeLongitude, currentLatitude, currentLongitude);

        if (distance > homeArea) {
            isAtHomeArea = false;
        } else {
            isAtHomeArea = true;
        }

        if (isAtHomeArea == false) {

           String location =  country + " - " + state + " - " + city;
            gL.recordTrip(location, perDiem);
//            Toast.makeText(context, distance + " from Home!", Toast.LENGTH_LONG).show();
        } else {
            // Don't Record location
//            Toast.makeText(context, distance + " You are at home!", Toast.LENGTH_LONG).show();
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
