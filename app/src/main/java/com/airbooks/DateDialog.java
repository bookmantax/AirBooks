/**
 * AirBooks app
 * DateDialog class :
 * This class handles the date picker used in AddNewTrip.java
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    EditText txtDate;
    String date;

    public DateDialog(View view) {
        txtDate = (EditText) view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = ((month + 1) + "/" + day + "/" + year);
        txtDate.setText(date);
    }
}
