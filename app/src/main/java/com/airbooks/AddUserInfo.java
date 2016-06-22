/*
 * First Class Tax app
 * Main activity class, used as the main menu of the application.
 */
package com.airbooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddUserInfo extends AppCompatActivity implements View.OnClickListener{


    // Variables
    private static Button button_sbm;
    DatabaseHelper db = new DatabaseHelper(this);
    EditText add_Name_EditText, add_Address_EditText, add_Employer_EditText, add_Base_EditText,
            add_Email_EditText, add_Phone_EditText;
    public static final String TAG = PerDiemSearch.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_user_info);

            // On Click Listener for buttons interaction
            OnClickButtonListenerSave();

            //Initialize EditText Variables
//            add_Name_EditText = (EditText) findViewById(R.id.add_Name_EditText);
//            add_Address_EditText = (EditText) findViewById(R.id.add_Address_EditText);
//            add_Employer_EditText = (EditText) findViewById(R.id.add_Employer_EditText);
            add_Base_EditText = (EditText) findViewById(R.id.add_Base_EditText);
//            add_Email_EditText = (EditText) findViewById(R.id.add_Email_EditText);
//            add_Phone_EditText = (EditText) findViewById(R.id.add_Phone_EditText);

        }


    @Override
    public void onClick(View v) {
    }

    // Save a Book Listener
    public void OnClickButtonListenerSave() {
        button_sbm = (Button) findViewById(R.id.add_save_btn);
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        // Call of the method Validate to check if EditText are empty
                        boolean fieldsOK = validate(new EditText[]{
//                                add_Name_EditText,
//                                add_Address_EditText,
//                                add_Employer_EditText,
                                add_Base_EditText,
//                                add_Email_EditText,
//                                add_Phone_EditText
                        });
                        if (fieldsOK == true) {

                            String name, address, employer, email, phone ;
                            boolean isInserted = db.insertData(
//                                    add_Name_EditText.getText().toString(),
                                    name = "Name",
//                                    add_Address_EditText.getText().toString(),
                                    address = "Address",
//                                    add_Employer_EditText.getText().toString(),
                                    employer = "Employer",
                                    add_Base_EditText.getText().toString(),
//                                    add_Email_EditText.getText().toString(),
                                    email = "E-Mail",
//                                    add_Phone_EditText.getText().toString()
                                    phone = "Phone"
                            );

                            if (isInserted == true) {
                                Toast.makeText(AddUserInfo.this, "Data Inserted!", Toast.LENGTH_SHORT).show();
                                // Reset fields
//                                add_Name_EditText.setText("");
//                                add_Address_EditText.setText("");
//                                add_Employer_EditText.setText("");
                                add_Base_EditText.setText("");
//                                add_Email_EditText.setText("");
//                                add_Phone_EditText.setText("");
                                // Return to Home Class
                                Intent intent = new Intent(AddUserInfo.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AddUserInfo.this, "Data not Inserted!", Toast.LENGTH_SHORT).show();
                            } // close if loop
                        } else {
                            Toast.makeText(AddUserInfo.this, "Please enter the missing information", Toast.LENGTH_LONG).show();
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

/*
    // Options menu to shout about info
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ABOUT, Menu.NONE, "About")
                .setIcon(R.drawable.add)
                .setAlphabeticShortcut('a');
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ABOUT:
                add();
                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    private void add() {
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
*/
}


