/*
 * First Class Tax app
 * Main activity class, used as the main menu of the application.
 */
package com.airbooks;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewTripHistory extends AppCompatActivity implements View.OnClickListener{

    private ListView list;
    private ArrayList<TripItem> trips, tripsReversed;
    private DatabaseHelper db = new DatabaseHelper(this);
    boolean moreTrips = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip_history);

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
}


