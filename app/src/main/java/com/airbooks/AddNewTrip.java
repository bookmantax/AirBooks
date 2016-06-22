package com.airbooks;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class AddNewTrip extends AppCompatActivity {

    private static Button button_sbm;
    public static final String TAG = PerDiemSearch.class.getSimpleName();
    double latitude, longitude;
    LatLng latLng;
    EditText landingDate, departingDate;
    String dayIn = "", dayOut = "", cityName = "";
    DatabaseHelper db = new DatabaseHelper(this);
    PlaceAutocompleteFragment autocompleteFragment;
    int days;

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
                //get value in numDaysText, set trip info in Database(location, number of days)

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    // Save a Book Listener
    public void OnClickButtonListenerAdd() {
        button_sbm = (Button) findViewById(R.id.addTripButton);
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dayIn = landingDate.getText().toString();
                        dayOut = departingDate.getText().toString();

                        if(cityName != "" && dayIn != "" && dayOut != "") {
                            int perDiem = db.getPerDiemByCity(cityName);
                            days = daysBetween(dayIn, dayOut);
                            int totalPerDiem = perDiem * days;
                            boolean isInserted = db.insertTrip(
                                    cityName, dayIn, dayOut, String.valueOf(totalPerDiem)
                            );

                            if (isInserted) {
                                Toast.makeText(AddNewTrip.this, "Data Inserted!", Toast.LENGTH_SHORT).show();
                                autocompleteFragment.setText("");
                                landingDate.setText("");
                                departingDate.setText("");
                            }
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

}
