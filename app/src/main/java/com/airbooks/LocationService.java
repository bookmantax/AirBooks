package com.airbooks;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
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
 * TODO: Create If for either one GPS or NET (GET_BEST_PROVIDER)
 * TODO: https://developer.android.com/reference/android/location/LocationManager.html#getBestProvider%28android.location.Criteria,%20boolean%29
 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        try {
            Log.i(TAG, "I'm getting your location");
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
            Location l = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latitude = l.getLatitude();
//Toast.makeText(LocationService.this, "NET = " + latitude, Toast.LENGTH_LONG).show();
            longitude = l.getLongitude();
//Toast.makeText(LocationService.this, "NET = " + longitude, Toast.LENGTH_LONG).show();
            country = country(latitude, longitude);
            state = state(latitude, longitude);
            city = city(latitude, longitude);
            setPerDiem();
            if(l != null) {
                Log.i(TAG, "" + l.getLatitude() + " - " + l.getLongitude());
                Date sDate = Calendar.getInstance().getTime();
                boolean isInserted = db.insertTrip(
                        ("NET " + country + " - " + state + " - " + city),
                        sDate.toString(), sDate.toString(),
                        String.valueOf(finalPerDiem));
            }
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            Log.i(TAG, "I'm getting your location");
//            GPSManager gps = new GPSManager(this);
//            gps.canGetLocation(); // Check GPS active
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
            Location l = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = l.getLatitude();
//Toast.makeText(LocationService.this, "GPS = " + latitude, Toast.LENGTH_LONG).show();
            longitude = l.getLongitude();
//Toast.makeText(LocationService.this, "GPS = " + longitude, Toast.LENGTH_LONG).show();
            country = country(latitude, longitude);
            state = state(latitude, longitude);
            city = city(latitude, longitude);
            perDiem = 0; // To get PerDiem value again.
            setPerDiem();
            if(l != null) {
                Log.i(TAG, "" + l.getLatitude() + " - " + l.getLongitude());
                Date sDate = Calendar.getInstance().getTime();
                boolean isInserted = db.insertTrip(
                        ("GPS " + country + " - " + state + " - " + city),
                        sDate.toString(), sDate.toString(),
                        String.valueOf(finalPerDiem));
            }
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
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
//Toast.makeText(LocationService.this, "City perDIem = " + cityPerDiem, Toast.LENGTH_LONG).show();
            finalPerDiem = cityPerDiem;
        } else if (finalPerDiem == 0) {
            int statePerDiem = getPerDiemState();
//Toast.makeText(LocationService.this, "State perDIem = " + statePerDiem, Toast.LENGTH_LONG).show();
            finalPerDiem = statePerDiem;
        } else if (finalPerDiem == 0){
            int countryPerDiem = getPerDiemCountry();
//Toast.makeText(LocationService.this, "Country perDIem = " + countryPerDiem, Toast.LENGTH_LONG).show();
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

    public Double setLatitude() {
        if (latitude == null) {
            Location gpsLoc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLoc != null) {
                latitude = gpsLoc.getLatitude();
                Toast.makeText(LocationService.this, "GPS = " + latitude, Toast.LENGTH_LONG).show();
            } else {
                Location netLoc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (netLoc != null) {
                    latitude = netLoc.getLatitude();
                    Toast.makeText(LocationService.this, "NET = " + latitude, Toast.LENGTH_LONG).show();
                }
//                Toast.makeText(LocationService.this, "setLatitude FAIL @ LocationService.java", Toast.LENGTH_LONG).show();
            }
        }
        return 0.0;
    }
    public Double setLongitude() {
        if (longitude == null) {
            Location gpsLoc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLoc != null) {
                longitude = gpsLoc.getLatitude();
                Toast.makeText(LocationService.this, "GPS = " + longitude, Toast.LENGTH_LONG).show();
            } else {
                Location netLoc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (netLoc != null) {
                   longitude = netLoc.getLatitude();
                    Toast.makeText(LocationService.this, "NET = " + longitude, Toast.LENGTH_LONG).show();
                }
//                Toast.makeText(LocationService.this, "setLongitude FAIL @ LocationService.java", Toast.LENGTH_LONG).show();
            }
        }
        return 0.0;
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
// Toast.makeText(LocationService.this, "You are @ " + country, Toast.LENGTH_LONG).show();
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
// Toast.makeText(LocationService.this, "You are @ " + state, Toast.LENGTH_LONG).show();
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
// Toast.makeText(LocationService.this, "You are @ " + city, Toast.LENGTH_LONG).show();
            return city;
    }
}