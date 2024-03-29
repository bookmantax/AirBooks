/**
 * AirBooks app
 * GetLocation class :
 * This class obtain the location by best provider, search perDiem values from database based in country, state and city.
 * Invoked from AlarmReceiver.java by the whereAmI() method.
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GetLocation extends Service implements android.location.LocationListener {

    private LocationManager mLocationManager;
    DatabaseHelper db;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0;
    Date sDate = Calendar.getInstance().getTime();
    Double meals = 0.0, finalMeals = 0.0, currentLatitude, currentLongitude, homeLatitude, homeLongitude, perDiem;
    String country, state, city, base;
    Context context;
    Location mLastLocation;

    public  GetLocation(Context context) {
        String provider = getProviderName();
        this.context = context;
        db = new DatabaseHelper(this.context);
        mLastLocation = new Location(provider);
        GetLocation();
    }

    public int GetLocation() {
        String provider = getProviderName();
        db = new DatabaseHelper(this.context);
        mLastLocation = new Location(provider);
        db = new DatabaseHelper(this.context);
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation.set(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }


        @Override
        public void onLocationChanged(Location location) {
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public void onCreate() {
        initializeLocationManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in our app.
     * */
    public void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(GetLocation.this);
        }
    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
                break;
            }
        }
        return bestLocation;
    }

    public String[] whereAmI(Context context) {

        String provider = null;
        if (PassValues.isLocationAvailable) {
            String currentLocation = null;
            if (this.context == null)
                this.context = context;

            String bestProvider = getProviderName();
            if (bestProvider != null) {
                try {
                    mLocationManager.requestLocationUpdates(bestProvider, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            this);

                    Location l = getLastKnownLocation();

                    currentLatitude = l.getLatitude();
                    currentLongitude = l.getLongitude();
                    country = country(currentLatitude, currentLongitude);
                    state = state(currentLatitude, currentLongitude);

                    base = getBase();

                    int length = base.length();
                    if (length == 3) {
                        homeLatitude = getBaseLatitudeIATA(base);
                        homeLongitude = getBaseLongitudeIATA(base);
                    } else {
                        homeLatitude = getBaseLatitudeICAO(base);
                        homeLongitude = getBaseLongitudeICAO(base);
                    }
                    if (setPerDiem() != 0.0) {
                        perDiem = setPerDiem();
                    } else {
                        String nearestCity = nearestAirport(country);
                        city = nearestCity;
                        perDiem = db.getLodgingByCity(nearestCity);

                    }
                    if (l != null) {

                        String[] locationStringArray = new String[8];
                        locationStringArray[0] = String.valueOf(currentLatitude);
                        locationStringArray[1] = String.valueOf(currentLongitude);
                        locationStringArray[2] = String.valueOf(homeLatitude);
                        locationStringArray[3] = String.valueOf(homeLongitude);
                        locationStringArray[4] = country;
                        locationStringArray[5] = state;
                        locationStringArray[6] = city;
                        locationStringArray[7] = String.valueOf(setPerDiem());

                        return locationStringArray;

                    }
                } catch (java.lang.SecurityException ex) {
                } catch (IllegalArgumentException ex) {

                }
            } else {
                Toast.makeText(this.context, "Please turn on your GPS", Toast.LENGTH_SHORT).show();
            }
            GetLocation();
            whereAmI(context);
        }
        return null;

    }

    /**
     * Get provider name.
     *
     * @return Name of best suiting provider.
     */
    public String getProviderName() {


        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
        criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
        criteria.setAltitudeRequired(false); // Choose if you use altitude.
        criteria.setBearingRequired(false); // Choose if you use bearing.
        criteria.setCostAllowed(false); // Choose if this provider can waste money :-)

        try {
            // Provide your criteria and flag enabledOnly that tells
            // MyLocationManager only to return active providers.
            String bestProviderName;
            if (this.context != null) {
                if (mLocationManager == null)
                    mLocationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
                return mLocationManager.getBestProvider(criteria, true);
            }
        } catch (NullPointerException exp) {
            exp.printStackTrace();
        }
        return null;
    }


    /*
 * Get location details
 */
    public String country(Double lat, Double lon) {
        Geocoder geoCoder = new Geocoder(this.context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String country = addresses.get(0).getCountryName();
        return country;
    }

    public String state(Double lat, Double lon) {
        Geocoder geoCoder = new Geocoder(this.context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String state = addresses.get(0).getAdminArea();
        return state;
    }

    public String city(Double lat, Double lon) {

        Geocoder geoCoder = new Geocoder(this.context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city = addresses.get(0).getLocality();
        return city;
    }

    /**
     * Set Meals Per Diem Value Based in Location
     *
     * @return
     */
    public Double setPerDiem() {

        if (meals == 0.0) {
            Double cityMeals = getCityMeals();
            finalMeals = cityMeals;
        } else if (finalMeals == 0.0) {
            Double stateMeals = getStateMeals();
            finalMeals = stateMeals;
        } else if (finalMeals == 0.0) {
            Double countryMeals = getCountryMeals();
            finalMeals = countryMeals;
        } else if (finalMeals == 0.0) {
            finalMeals = 65.0;
        }
        return finalMeals;
    }

    public Double getCountryMeals() {
        meals = db.getMealsByCountry(country);
        if (meals != 0.0) {
            return meals;
        }
        return 0.0;
    }

    public Double getStateMeals() {
        meals = db.getMealsBySate(state);
        if (meals != 0.0) {
            return meals;
        }
        return 0.0;
    }

    public Double getCityMeals() {
        meals = db.getMealsByCity(city);
        if (meals != 0.0) {
            return meals;
        }
        return 0.0;
    }

    public String getBase() {
        base = db.getHomeLocation();
        if (base != null) {
            return base;
        }
        return null;
    }

    public Double getBaseLatitudeIATA(String homeAirport) {
        homeLatitude = db.getHomeLatitude_IATA(homeAirport);
        if (homeLatitude != null) {
            return homeLatitude;
        }
        return null;
    }

    public Double getBaseLongitudeIATA(String homeAirport) {
        homeLongitude = db.getHomeLongitude_IATA(homeAirport);
        if (homeLongitude != null) {
            return homeLongitude;
        }
        return null;
    }

    public Double getBaseLatitudeICAO(String homeAirport) {
        homeLatitude = db.getHomeLatitude_ICAO(homeAirport);
        if (homeLatitude != null) {
            return homeLatitude;
        }
        return null;
    }

    public Double getBaseLongitudeICAO(String homeAirport) {
        homeLongitude = db.getHomeLongitude_ICAO(homeAirport);
        if (homeLongitude != null) {
            return homeLongitude;
        }
        return null;
    }

    public boolean recordTrip(String location, Double perDiem) {
        boolean isInserted = db.insertTrip(location, sDate.toString(), "", String.valueOf(perDiem));
        if (isInserted != false) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the nearest city based in the closest airport by coordinates
     * @param country
     * @return nearestCity (String)
     */
    public String nearestAirport(String country) {
        String nearestCity = null;
        AlarmReceiver aR = new AlarmReceiver();
        Double distance = 0.0;
        int lines;

        Cursor cursor = db.getNearAirports(country);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Toast.makeText(this.context, "Cursor is null", Toast.LENGTH_SHORT).show();
        }

        lines = cursor.getCount();
//        Toast.makeText(this.context, "Lines = " + lines, Toast.LENGTH_SHORT).show();

        for (int x = 0; lines >= x; x++) {

            Double latitude = cursor.getDouble(cursor.getColumnIndex("AIRPORT_LATITUDE"));
            Double longitude = cursor.getDouble(cursor.getColumnIndex("AIRPORT_LONGITUDE"));
            Double d = aR.distance(latitude, longitude, currentLatitude, currentLongitude);

            if (distance == 0.0) {
                distance = d;
                cursor.moveToNext();
                x++;
            } else if (d > distance) {
                cursor.moveToNext();
                x++;
            } else if (distance == 0.0 || d < distance) {
                distance = d;
                nearestCity = cursor.getString(cursor.getColumnIndex("AIRPORT_CITY"));
                cursor.moveToNext();
                x++;
            } else {
            }
        }
        return nearestCity;
    }
}
