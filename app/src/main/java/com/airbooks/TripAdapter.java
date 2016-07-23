/**
 * AirBooks app
 * TripAdapter class :
 * Adapter for displaying all the database stored trips information to be displayed via inflater
 * at ViewTripHistory.java
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TripAdapter extends ArrayAdapter<TripItem> {
    final Context context;
    final int layoutResourceId;
    ArrayList<TripItem> trips = null;
    TripItem trip;

    public TripAdapter(Context context, int layoutResourceId, ArrayList<TripItem> trips){
        super(context, layoutResourceId, trips);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.trips = trips;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View row = inflater.inflate(layoutResourceId, parent, false);

        //View row = convertView;
        final PersonHolder holder = new PersonHolder();

        holder.txtLocation = (TextView) row.findViewById(R.id.locationText);
        holder.txtDates = (TextView) row.findViewById(R.id.datesText);
        holder.txtPerDiem = (TextView) row.findViewById(R.id.perDiemText);

        row.setTag(holder);

        trip = trips.get(position);
        holder.txtLocation.setText(trip.location);
        holder.txtDates.setText(trip.dates);
        holder.txtPerDiem.setText(trip.perDiem);
        holder.position = position;

        return row;
    }

    static class PersonHolder {
        TextView txtLocation;
        TextView txtDates;
        TextView txtPerDiem;
        int position;
    }
}
