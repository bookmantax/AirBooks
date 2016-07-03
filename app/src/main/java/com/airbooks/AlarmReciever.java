package com.airbooks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class AlarmReciever extends BroadcastReceiver {

    // Varibales
    LocationService location =  new LocationService();
    private android.location.LocationManager mLocationManager = null;

         @Override
            public void onReceive(Context context, Intent intent)
            {
                    // TODO http://stackoverflow.com/questions/12920451/should-i-use-android-locationmanager-getbestprovider-every-time-i-ask-for-locati
                
                    
                      // here you can start an activity or service depending on your need
                     // for ex you can start an activity to vibrate phone or to ring the phone


                                     
                    String phoneNumberReciver="9548736847";// phone number to which SMS to be send
                    String message="I am checking your current location at " + GregorianCalendar.getInstance().toString();// message to send
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(phoneNumberReciver, null, message, null, null);
                    // Show the toast  like in above screen shot
                    Toast.makeText(context, "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG).show();
             }
      
}