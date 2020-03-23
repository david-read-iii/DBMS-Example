package com.read.dbmsexample;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonAdd;
    private Boolean saved;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    /**
     * Start an activity with several EditTexts, a Spinner, and a Button when this activity is
     * created. The EditTexts and Spinner will allow the user to specify the attributes of a new
     * User and the Button will allow the user to confirm the addition of the User into the database.
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

        /* Attach a click listener to buttonAdd to add a new User to the database with the
         * attributes specified in the EditTexts. Print a Toast indicating the status of the save. */
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save User with specified attributes to the database.
                saved = firebaseHelper.save(new User(
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString()));

                // Print Toast indicating status of the deletion.
                if (saved) {
                    Toast.makeText(AddUserActivity.this, R.string.toast_add_successful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddUserActivity.this, R.string.toast_add_failed, Toast.LENGTH_SHORT).show();
                }

                // Close this activity.
                finish();
            }
        });
    }
}
