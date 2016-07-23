/**
 * AirBooks app
 * AddNewTrip class :
 * This class allow the user to save a trip manually in case the app didn't record it
 * due to several factors, like cell off, airplane mode, no gps, cell, wi-fi signal etc.
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddNewTrip extends AppCompatActivity {

    //Variables
    private static Button button_sbm;
    public static final String TAG = PerDiemSearch.class.getSimpleName();
    double latitude, longitude, totalPerDiem;
    LatLng latLng;
    EditText landingDate, departingDate;
    String dayIn = "", dayOut = "", cityName = "", countryName = "";
    DatabaseHelper db = new DatabaseHelper(this);
    PlaceAutocompleteFragment autocompleteFragment;
    int days;
    // Options Menu Variables
    private static final int EDIT_PROFILE = Menu.FIRST + 1;
    private static final int ABOUT = Menu.FIRST + 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);

        landingDate = (EditText) findViewById(R.id.dateLandedText);
        departingDate = (EditText) findViewById(R.id.dateDepartedText);


        landingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View v, boolean hasFocus) {
                 if (hasFocus) {
                     DateDialog dialog = new DateDialog(v);
                     FragmentTransaction ft = getFragmentManager().beginTransaction();
                     dialog.show(ft, "DatePicker");
                 }
             }
        });

        departingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if (hasFocus) {
                   DateDialog dialog = new DateDialog(v);
                   FragmentTransaction ft = getFragmentManager().beginTransaction();
                   dialog.show(ft, "DatePicker");
               }
           }
       });

        OnClickButtonListenerAdd();

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        /*
        * The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
        * set a filter returning only results with a precise address.
        */
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());//get place details here
                latLng = place.getLatLng();
                cityName = place.getName().toString();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    // Save Listener
    public void OnClickButtonListenerAdd() {
        button_sbm = (Button) findViewById(R.id.addTripButton);
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dayIn = landingDate.getText().toString();
                        dayOut = departingDate.getText().toString();

                        if(cityName != "" && dayIn != "" && dayOut != "") {

                            String location = countryName + " - " + cityName;
//                            int perDiem = db.getPerDiemByCity(cityName); // Leaved for future usage
                            double perDiem = db.getMealsByCity(cityName);
                            days = daysBetween(dayIn, dayOut);
                            if (days == 0){
                                totalPerDiem = perDiem;
                            } else {
                                totalPerDiem = perDiem * days;
                            }
                            boolean isInserted = db.insertTrip(
                                    location, dayIn, dayOut, String.valueOf(totalPerDiem)
                            );

                            if (isInserted) {
                                Toast.makeText(AddNewTrip.this, "Data Inserted!", Toast.LENGTH_SHORT).show();
                                autocompleteFragment.setText("");
                                landingDate.setText("");
                                departingDate.setText("");
                            }
                            Intent intent = new Intent(AddNewTrip.this, ViewTripHistory.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                }
        );
    }


    public static int daysBetween(String startDate, String endDate) {
        Calendar sDate = Calendar.getInstance();
        Calendar eDate = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try{
            sDate.setTime(format.parse(startDate));
            eDate.setTime(format.parse(endDate));
        } catch(ParseException e){
            e.printStackTrace();
        }

        int daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Options menu to shout about info
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, EDIT_PROFILE, Menu.NONE, "Edit Profile").setAlphabeticShortcut('e');
        menu.add(Menu.NONE, ABOUT, Menu.NONE, "About").setAlphabeticShortcut('?');
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT_PROFILE:
                Edit_Profile();
                return (true);
            case ABOUT:
                about();
                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    private void Edit_Profile() {
        Intent intent = new Intent("com.airbooks.EditUserInfo");
        startActivity(intent);
    }
    private void about() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View addView = inflater.inflate(R.layout.about, null);
        new AlertDialog.Builder(this)
                .setTitle(R.string.about)
                .setView(addView)
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // ignore, just dismiss
                            }
                        })
                .show();
    }
}
