/**
 * AirBooks app
 * ViewTripHistory class :
 * Used to display all the trips stored into the database and the information for each trip
 * It also contains a floating action button to call the AddNewTrip to allow the user to enter a
 * trip manually.
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewTripHistory extends AppCompatActivity implements View.OnClickListener{

    // Variables
    private ListView list;
    private ArrayList<TripItem> trips, tripsReversed;
    private DatabaseHelper db = new DatabaseHelper(this);
    boolean moreTrips = true;
    // Options Menu Variables
    private static final int EDIT_PROFILE = Menu.FIRST + 1;
    private static final int ABOUT = Menu.FIRST + 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip_history);

        // Floating Action Button Click Listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(ViewTripHistory.this, AddNewTrip.class);
                startActivity(intent);
            }
        });

        list = (ListView)findViewById(R.id.tripListView);
        trips = new ArrayList<>();
        tripsReversed = new ArrayList<>();
        Cursor cursor = db.getAllTrips();
        if(!db.isTripsEmpty()) {
            cursor.moveToFirst();
            trips.add(new TripItem(
                    cursor.getString(cursor.getColumnIndex("LOCATION")),
                    cursor.getString(cursor.getColumnIndex("DATE_IN")) +
                            "    -    " + cursor.getString(cursor.getColumnIndex("DATE_OUT")),
                    "$" + cursor.getString(cursor.getColumnIndex("PER_DIEM"))));
            moreTrips = cursor.moveToNext();
            while (moreTrips) {
                trips.add(new TripItem(
                        cursor.getString(cursor.getColumnIndex("LOCATION")),
                        cursor.getString(cursor.getColumnIndex("DATE_IN")) +
                                " - " + cursor.getString(cursor.getColumnIndex("DATE_OUT")),
                        "$" + cursor.getString(cursor.getColumnIndex("PER_DIEM"))));
                moreTrips = cursor.moveToNext();
            }
        }

        for (int i = trips.size() - 1; i >= 0; i--) {
            tripsReversed.add(trips.get(i));
        }

        TripAdapter adapter = new TripAdapter(ViewTripHistory.this,
                R.layout.trip_item, tripsReversed);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

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


