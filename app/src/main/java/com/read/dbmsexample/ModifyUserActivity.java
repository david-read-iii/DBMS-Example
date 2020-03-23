package com.read.dbmsexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyUserActivity extends AppCompatActivity {

    private User selected;
    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonDelete, buttonSave;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private Boolean deleted, modified;

    /**
     * Start an activity with several EditTexts, a Spinner, and Buttons when this activity is
     * created. The User selected will represent the User that the user clicked on to get to this
     * activity. The EditTexts will allow the user to specify new attributes for User selected, the
     * Button buttonSave will allow the user to confirm the modification of User selected in the
     * database, and the Button buttonDelete will allow the user to delete User selected from the
     * database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        /* Receive the attributes of the clicked User and set them as attributes of a temporary
         * User selected. */
        Intent intent = getIntent();

        selected = new User(
                intent.getStringExtra("key"),
                intent.getStringExtra("firstName"),
                intent.getStringExtra("lastName"),
                intent.getStringExtra("username"),
                intent.getStringExtra("password"),
                intent.getStringExtra("role"));

        /* Start listening for the changing of User selected in the database. If User selected is
         * changed, close this activity. */
        firebaseHelper.startListeningForChangedUser(this, selected);

        // Bring XML elements to Java.
        editTextFirstName = findViewById(R.id.edittext_modify_first_name);
        editTextLastName = findViewById(R.id.edittext_modify_last_name);
        editTextUsername = findViewById(R.id.edittext_modify_username);
        editTextPassword = findViewById(R.id.edittext_modify_password);
        spinnerRole = findViewById(R.id.spinner_modify_role);
        buttonDelete = findViewById(R.id.button_delete);
        buttonSave = findViewById(R.id.button_save);

        // Attach ArrayAdapter to Spinner.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerAdapter);

        // Set the EditText values as the attributes of the User selected.
        editTextFirstName.setText(selected.getFirstName());
        editTextLastName.setText(selected.getLastName());
        editTextUsername.setText(selected.getUsername());
        editTextPassword.setText(selected.getPassword());
        spinnerRole.setSelection(spinnerAdapter.getPosition(selected.getRole()));

        /* Attach a click listener to buttonDelete to delete the User selected from the database.
         * Print a Toast indicating the status of the deletion. */
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach this database listener.
                firebaseHelper.stopListeningForChangedUser();

                // Delete User selected from the database.
                deleted = firebaseHelper.delete(selected);

                // Print Toast indicating status of the deletion.
                if (deleted) {
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_delete_successful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_delete_failed, Toast.LENGTH_SHORT).show();
                }

                // Close this activity.
                finish();
            }
        });

        /* Attach a click listener to buttonSave to modify the User selected in the database with
         * the attributes specified in the EditTexts. Print a Toast indicating the status of the
         * modification. */
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach this database listener.
                firebaseHelper.stopListeningForChangedUser();

                // Modify User selected in the database with the attributes specified in this new User.
                modified = firebaseHelper.modify(selected, new User(
                        selected.getKey(),
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString()));

                // Print Toast indicating status of the modification.
                if (modified) {
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_modify_successful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_modify_failed, Toast.LENGTH_SHORT).show();
                }

                // Close this activity.
                finish();
            }
        });
    }

    /**
     * Close this activity when this activity is not in the foreground.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Detach this database listener.
        firebaseHelper.stopListeningForChangedUser();

        // Close this activity.
        finish();
    }
}