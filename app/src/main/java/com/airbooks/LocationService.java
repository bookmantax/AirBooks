package com.airbooks;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TODO Line 84
 */
public class LocationService extends Service{
    private static final String TAG = "FIRSTCLASSTAXGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0;
    DatabaseHelper db = new DatabaseHelper(this);
    String country, state, city;
    Double latitude;
    Double longitude;
    int perDiem;
    int finalPerDiem = 0;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
/**
 * TODO: https://developer.android.com/reference/android/location/LocationManager.html#getBestProvider%28android.location.Criteria,%20boolean%29
 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**
         * Returns the name of the provider that best meets the given criteria.
         * Only providers that are permitted to be accessed by the calling activity will be returned. If several providers meet the criteria, the one with the best accuracy is returned.
         * If no provider meets the criteria, the criteria are loosened in the following sequence:
         * power requirement
         * accuracy
         * bearing
         * speed
         * altitude
         */
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Sets the criteria for a fine and low power provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // Gets the best matched provider, and only if it's on
        String bestProvider = mLocationManager.getBestProvider(criteria, true);


        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        try {
            Log.i(TAG, "I'm getting your location");
            mLocationManager.requestLocationUpdates(bestProvider, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
            Location l = mLocationManager.getLastKnownLocation(bestProvider);
            latitude = l.getLatitude();
            longitude = l.getLongitude();
            country = country(latitude, longitude);
            state = state(latitude, longitude);
            city = city(latitude, longitude);
            setPerDiem();
            if (l != null) {
                Log.i(TAG, "" + l.getLatitude() + " - " + l.getLongitude());
                Date sDate = Calendar.getInstance().getTime();
                boolean isInserted = db.insertTrip(
                        (country + " - " + state + " - " + city),
                        sDate.toString(), sDate.toString(),
                        String.valueOf(finalPerDiem));
            }
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        return START_STICKY;
    }



    /**
     * Set Per Diem Value Based in Location
     * @return
     */
    public int setPerDiem() {
        if (perDiem == 0) {
            int cityPerDiem = getPerDiemCity();
            finalPerDiem = cityPerDiem;
        } else if (finalPerDiem == 0) {
            int statePerDiem = getPerDiemState();
            finalPerDiem = statePerDiem;
        } else if (finalPerDiem == 0){
            int countryPerDiem = getPerDiemCountry();
            finalPerDiem = countryPerDiem;
        }
        return finalPerDiem;
    }


        public int getPerDiemCountry() {
        perDiem = db.getPerDiemByCountry(country);
        if (perDiem != 0) {
            return perDiem;
        }
        return 0;
    }

    public int getPerDiemState() {
        perDiem = db.getPerDiemBySate(state);
        if (perDiem != 0) {
            return perDiem;
        }
        return 0;
    }
    public int getPerDiemCity() {
        perDiem = db.getPerDiemByCity(city);
        if (perDiem != 0) {
            return perDiem;
        }
        return 0;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /*
     * Get location details
     */
    public String country(Double lat, Double lon) {
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        country = addresses.get(0).getCountryName();
        return country;
    }

    public String state(Double lat, Double lon) {
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        state = addresses.get(0).getAdminArea();
        return state;
    }

    public String city(Double lat, Double lon) {

        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        city = addresses.get(0).getLocality();
            return city;
    }
 }