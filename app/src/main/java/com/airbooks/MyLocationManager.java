/**
 * AirBooks app
 * MyLocationManager class :
 * This class obtain the current location coordinates,
 * City, State, Country based in current location
 * Total per diem for current location.
 * NOTE: Some parts of the code are commented out, leaved for further development.
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationManager extends Activity
    implements GestureDetector.OnGestureListener{
    //GestureVariables
    private GestureDetectorCompat mDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    //Variables
    protected Context context;
    DatabaseHelper db = new DatabaseHelper(this);
    TextView txtLat, txtCountry, txtState, txtCity, txtPerDiem, txtMeals, txtLodging ; // TODO: Leaved for future usage
    String lat = "", longitude = "", country = "", state = "", city = "";
    Double meals = 0.0, finalMeals = 0.0, lodging = 0.0, finalLodging = 0.0;
    int perDiem = 0, finalPerDiem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mDetector = new GestureDetectorCompat(this, this);
        txtLat = (TextView) findViewById(R.id.textview1);
        txtCountry = (TextView) findViewById(R.id.country_tv);
        txtState = (TextView) findViewById(R.id.state_tv);
        txtCity = (TextView) findViewById(R.id.city_tv);
        txtPerDiem = (TextView) findViewById(R.id.perDiem_tv);
//        txtMeals = (TextView) findViewById(R.id.meals_tv); // Leaved for future usage
//        txtLodging = (TextView) findViewById(R.id.lodging_tv); // Leaved for future usage

        GPSManager gps = new GPSManager(this);
        // check GPS active
        if (gps.canGetLocation()) {
            lat = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());

            GetCountryStateCity(gps.getLatitude(), gps.getLongitude());
<<<<<<< HEAD
=======

//          perDiem = setPerDiem(); // Leaved for future usage
            perDiem = finalMeals.intValue();
>>>>>>> origin/master
            finalMeals = setMeals();
            finalLodging = setLodging();
//          perDiem = setPerDiem(); // Leaved for future usage
            perDiem = finalMeals.intValue();
            if (perDiem == 0) {
                perDiem = 65;
            }

            PassValues.isLocationAvailable = false;
        }
        else{
            PassValues.isLocationAvailable = false;
            gps.showSettingsAlert();
        }
        txtLat.setText(lat + longitude);
        txtCountry.setText(country);
        txtState.setText(state);
        txtCity.setText(city);
        txtPerDiem.setText("$ " + perDiem);
//        txtMeals.setText(" US $ " + String.valueOf(finalMeals)); // Leaved for future usage
//        txtLodging.setText(" US $ " + String.valueOf(finalLodging)); // Leaved for future usage
    }

    public void GetCountryStateCity(Double lat, Double lon){
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geoCoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
<<<<<<< HEAD
        }
        if(addresses.size() > 0) {
            country = addresses.get(0).getCountryName();
            state = addresses.get(0).getAdminArea();
            city = addresses.get(0).getLocality();
        }
=======
        }
        if(addresses.size() > 0) {
            country = addresses.get(0).getCountryName();
            state = addresses.get(0).getAdminArea();
            city = addresses.get(0).getLocality();
        }
>>>>>>> origin/master
    }

    ///// Setters /////
    /**
    * Set Per Diem Value Based in Location
    * Leaved for future usage
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

    /**
     * Swipe detection region
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        boolean handled = super.dispatchTouchEvent(ev);
        handled = mDetector.onTouchEvent(ev);
        return handled;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try{
            if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
                return false;
            }
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                startActivity(new Intent(this, ViewTripHistory.class));
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                startActivity(new Intent(this, EditUserInfo.class));
            }
        } catch (Exception e){

        }
        return false;
    }
}