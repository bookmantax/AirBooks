package com.airbooks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.LocationManager;
import android.widget.Toast;
import com.airbooks.LocationService;

/**
 * Created by unlimited_power on 7/6/16.
 */
public class AlarmReceiver extends BroadcastReceiver  {

    // Variables
    boolean isAtHomeArea = false;
    private LocationManager mLocationManager = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (isAtHomeArea == false) {

            LocationService ls = new LocationService();
            String location = ls.whereAmI();

            // For our recurring task, we'll just display a message
            Toast.makeText(context, location, Toast.LENGTH_LONG).show();

        } else {
            // Don't Record location
        }
    }
}