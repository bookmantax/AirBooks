/**
 * AirBooks app
 * EditUserInfo class :
 * This class allow the user to complete and / or update the profile information
 * Created by Rodrigo Escobar in July 2016
 */
package com.airbooks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditUserInfo extends AppCompatActivity
        implements View.OnClickListener, GestureDetector.OnGestureListener{
    //GestureVariables
    private GestureDetectorCompat mDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    // Variables
    private static Button button_sbm;
    DatabaseHelper db = new DatabaseHelper(this);
    EditText edit_Name_EditText, edit_Address_EditText, edit_Employer_EditText, edit_Base_EditText,
             edit_Email_EditText, edit_Phone_EditText ;
    public static final String TAG = PerDiemSearch.class.getSimpleName();
    Cursor cursor;
    int pos;
    private final String USER_TABLE_NAME = "user_info_table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        mDetector = new GestureDetectorCompat(this, this);
        // On Click Listener for buttons interaction
        OnClickButtonListenerSave();
        OnClickButtonListenerCancel();

        //Initialize EditText Variables
        edit_Name_EditText = (EditText) findViewById(R.id.edit_Name_EditText);
        edit_Address_EditText = (EditText) findViewById(R.id.edit_Address_EditText);
        edit_Employer_EditText = (EditText) findViewById(R.id.edit_Employer_EditText);
        edit_Base_EditText = (EditText) findViewById(R.id.edit_Base_EditText);
        edit_Email_EditText = (EditText) findViewById(R.id.edit_Email_EditText);
        edit_Phone_EditText = (EditText) findViewById(R.id.edit_Phone_EditText);

        if(!db.isEmpty(USER_TABLE_NAME)) {
            cursor = db.getAllData();
            if(cursor != null) {
                cursor.moveToFirst();
                pos = cursor.getColumnIndex("NAME");
                if(pos != -1){
                    edit_Name_EditText.setText(cursor.getString(pos));
                }
                pos = cursor.getColumnIndex("ADDRESS");
                if(pos != -1){
                    edit_Address_EditText.setText(cursor.getString(pos));
                }
                pos = cursor.getColumnIndex("AIRLINE");
                if(pos != -1){
                    edit_Employer_EditText.setText(cursor.getString(pos));
                }
                pos = cursor.getColumnIndex("BASE");
                if(pos != -1){
                    edit_Base_EditText.setText(cursor.getString(pos));
                }
                pos = cursor.getColumnIndex("EMAIL");
                if(pos != -1){
                    edit_Email_EditText.setText(cursor.getString(pos));
                }
                pos = cursor.getColumnIndex("PHONE");
                if(pos != -1){
                    edit_Phone_EditText.setText(cursor.getString(pos));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    // Save a Book Listener
    public void OnClickButtonListenerSave() {
        button_sbm = (Button) findViewById(R.id.edit_save_btn);
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Call of the method Validate to check if EditText are empty
                        boolean fieldsOK = validate(new EditText[]{
                                edit_Name_EditText,
                                edit_Address_EditText,
                                edit_Employer_EditText,
                                edit_Base_EditText,
                                edit_Email_EditText,
                                edit_Phone_EditText});
                        if (fieldsOK == true) {

                            boolean isInserted = db.updateData(
                                    edit_Name_EditText.getText().toString(),
                                    edit_Address_EditText.getText().toString(),
                                    edit_Employer_EditText.getText().toString(),
                                    edit_Base_EditText.getText().toString(),
                                    edit_Email_EditText.getText().toString(),
                                    edit_Phone_EditText.getText().toString());

                            if (isInserted == true) {
                                Toast.makeText(EditUserInfo.this, "Data Inserted!", Toast.LENGTH_SHORT).show();
                                // Reset fields
                                edit_Name_EditText.setText("");
                                edit_Address_EditText.setText("");
                                edit_Employer_EditText.setText("");
                                edit_Base_EditText.setText("");
                                edit_Email_EditText.setText("");
                                edit_Phone_EditText.setText("");
                                // Return to Home Class
                                Intent intent = new Intent(EditUserInfo.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EditUserInfo.this, "Data not Inserted!", Toast.LENGTH_SHORT).show();
                            } // close if loop
                        } else {
                            Toast.makeText(EditUserInfo.this, "Please enter the missing information", Toast.LENGTH_LONG).show();
                        }
                    } // end onClick
                }
        );
    } // END of OnClickButtonListenerSave

    // For loop to validate EditText Fields are not empty
    private boolean validate(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    // Cancel a Book Listener
    public void OnClickButtonListenerCancel() {
        button_sbm = (Button) findViewById(R.id.edit_cancel_btn);
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Return to Home Class
                        Intent intent = new Intent(EditUserInfo.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
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
                Intent activity = new Intent(this, MyLocationManager.class);
                activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(activity);
                finish();
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                startActivity(new Intent(this, TaxDeductionsToDate.class));
            }
        } catch (Exception e){

        }
        return false;
    }
}


