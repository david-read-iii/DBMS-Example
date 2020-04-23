package com.read.dbmsexample.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.read.dbmsexample.Firebase.UsersFirebaseHelper;
import com.read.dbmsexample.R;
import com.read.dbmsexample.Models.User;

public class AddUserActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonAdd;
    private int saved;

    /**
     * When this activity is created, inflate a layout with several EditTexts, a Spinner, and a
     * Button. The EditTexts and Spinner will allow the user to specify the attributes of a new
     * User and the Button will allow the user to confirm the saving of the User into the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Bring XML elements to Java.
        editTextFirstName = findViewById(R.id.edittext_add_first_name);
        editTextLastName = findViewById(R.id.edittext_add_last_name);
        editTextUsername = findViewById(R.id.edittext_add_username);
        editTextPassword = findViewById(R.id.edittext_add_password);
        spinnerRole = findViewById(R.id.spinner_add_role);
        buttonAdd = findViewById(R.id.button_add);

        // Attach ArrayAdapter to Spinner.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerAdapter);

        // Attach a click listener to buttonAdd.
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save a User object with the specified attributes to the database.
                saved = UsersFirebaseHelper.save(new User(
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString())
                );

                // Depending on the status of the save, print a Toast and take an action.
                if (saved == 0) {
                    // Save successful. Close this activity.
                    Toast.makeText(AddUserActivity.this, R.string.toast_add_successful, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (saved == 1) {
                    // Save failed due to database error. Close this activity.
                    Toast.makeText(AddUserActivity.this, R.string.toast_add_failed, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Save failed due to invalid attributes.
                    Toast.makeText(AddUserActivity.this, R.string.toast_invalid, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
