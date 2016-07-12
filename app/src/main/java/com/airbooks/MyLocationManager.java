package com.airbooks;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MyLocationManager extends Activity {


    protected Context context;
    DatabaseHelper db = new DatabaseHelper(this);
    TextView txtLat, txtCountry, txtState, txtCity, txtPerDiem, txtMeals, txtLodging ;
    String lat, longitude, country, state, city;
    Double meals = 0.0, finalMeals = 0.0, lodging = 0.0, finalLodging = 0.0;
    int perDiem = 0, finalPerDiem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        txtLat = (TextView) findViewById(R.id.textview1);
        txtCountry = (TextView) findViewById(R.id.country_tv);
        txtState = (TextView) findViewById(R.id.state_tv);
        txtCity = (TextView) findViewById(R.id.city_tv);
        txtPerDiem = (TextView) findViewById(R.id.perDiem_tv);
        txtMeals = (TextView) findViewById(R.id.meals_tv);
        txtLodging = (TextView) findViewById(R.id.lodging_tv);


        GPSManager gps = new GPSManager(this);
        // check GPS active
        if (gps.canGetLocation()) {
            lat = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());

            country = country(gps.getLatitude(), gps.getLongitude());
            state = state(gps.getLatitude(), gps.getLongitude());
            city = city(gps.getLatitude(), gps.getLongitude());
            perDiem = setPerDiem();
            finalMeals = setMeals();
            finalLodging = setLodging();
            Passvalues.isLocationAvailable = false;
        }
        else{
            Passvalues.isLocationAvailable = false;
            gps.showSettingsAlert();
        }
        txtLat.setText(lat + longitude);
        txtCountry.setText(country);
        txtState.setText(state);
        txtCity.setText(city);
        txtPerDiem.setText("$ " + perDiem);
        txtMeals.setText(" US $ " + String.valueOf(finalMeals));
        txtLodging.setText(" US $ " + String.valueOf(finalLodging));
    }

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

    ///// Setters /////
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
    /**
     * Set Meals and Incidentals Per Diem Value Based in Location
     * @return
     */
    public Double setMeals() {
        if (meals == 0.0) {
            Double cityMeals = getMealsCity();
            finalMeals = cityMeals;
        } else if (finalMeals == 0.0) {
            Double stateMeals = getMealsState();
            finalMeals = stateMeals;
        } else if (finalMeals == 0.0){
            Double countryMeals = getMealsCountry();
            finalMeals = countryMeals;
        }
        return finalMeals;
    }
    /**
     * Set Lodging Per Diem Value Based in Location
     * @return
     */
    public Double setLodging() {
        if (lodging == 0.0) {
            Double cityLodging = getLodgingByCity();
            finalLodging = cityLodging;
        } else if (finalLodging == 0.0) {
            Double stateLodging = getLodgingState();
            finalLodging = stateLodging;
        } else if (finalLodging == 0.0){
            Double countryLodging = getLodgingCountry();
            finalLodging = countryLodging;
        }
        return finalLodging;
    }

    ///// Getters /////
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


    public Double getMealsCountry() {
        meals = db.getMealsByCountry(country);
        if (meals != 0.0) {
            return meals;
        }
        return 0.0;
    }

    public Double getMealsState() {
        meals = db.getMealsBySate(state);
        if (meals != 0.0) {
            return meals;
        }
        return 0.0;
    }
    public Double getMealsCity() {
        meals = db.getMealsByCity(city);
        if (meals != 0.0) {
            return meals;
        }
        return 0.0;
    }

    public Double getLodgingCountry() {
        lodging = db.getLodgingByCountry(country);
        if (lodging != 0.0) {
            return lodging;
        }
        return 0.0;
    }

    public Double getLodgingState() {
        lodging = db.getLodgingBySate(state);
        if (lodging != 0.0) {
            return lodging;
        }
        return 0.0;
    }
    public Double getLodgingByCity() {
        lodging = db.getLodgingByCity(city);
        if (lodging != 0.0) {
            return lodging;
        }
        return 0.0;
    }
}