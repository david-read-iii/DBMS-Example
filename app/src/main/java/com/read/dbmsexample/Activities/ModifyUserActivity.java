package com.read.dbmsexample.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.dbmsexample.Firebase.UsersChildEventListener;
import com.read.dbmsexample.Firebase.UsersFirebaseHelper;
import com.read.dbmsexample.R;
import com.read.dbmsexample.Models.User;

public class ModifyUserActivity extends AppCompatActivity {

    private User selected;
    private DatabaseReference databaseReference;
    private UsersChildEventListener childEventListener;
    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button buttonDelete, buttonSave;
    private int deleted, modified;

    /**
     * When this activity is created, inflate a layout with several EditTexts, a Spinner, and
     * Buttons. The User object selected will represent the User that the user clicked on to get to
     * this activity. The EditTexts will allow the user to specify new attributes for the selected
     * User object, the Button buttonSave will allow the user to confirm the modification of the
     * selected User object in the database, and the Button buttonDelete will allow the user to
     * delete the selected User object from the database. A UsersChildEventListener will be setup to
     * close this activity when the selected User object is changed by another user in the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        // Receive the attributes of the selected User object.
        Intent intent = getIntent();

        selected = new User(
                intent.getStringExtra("key"),
                intent.getStringExtra("firstName"),
                intent.getStringExtra("lastName"),
                intent.getStringExtra("username"),
                intent.getStringExtra("password"),
                intent.getStringExtra("role")
        );

        // Initialize DatabaseReference and UsersChildEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new UsersChildEventListener(this);

        // Attach UsersChildEventListener to the selected User object in the database.
        databaseReference.child("Users").child(selected.getKey()).addChildEventListener(childEventListener);

        // Bring XML elements to Java.
        editTextFirstName = findViewById(R.id.edittext_modify_first_name);
        editTextLastName = findViewById(R.id.edittext_modify_last_name);
        editTextUsername = findViewById(R.id.edittext_modify_username);
        editTextPassword = findViewById(R.id.edittext_modify_password);
        spinnerRole = findViewById(R.id.spinner_modify_role);
        buttonDelete = findViewById(R.id.button_delete);
        buttonSave = findViewById(R.id.button_save);

        // Define and attach ArrayAdapter to Spinner.
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(spinnerAdapter);

        // Set the EditText values as the attributes of the selected User object.
        editTextFirstName.setText(selected.getFirstName());
        editTextLastName.setText(selected.getLastName());
        editTextUsername.setText(selected.getUsername());
        editTextPassword.setText(selected.getPassword());
        spinnerRole.setSelection(spinnerAdapter.getPosition(selected.getRole()));

        // Attach a click listener to buttonDelete.
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach UsersChildEventListener.
                databaseReference.child("Users").child(selected.getKey()).removeEventListener(childEventListener);

                // Delete the selected User object from the database.
                deleted = UsersFirebaseHelper.delete(selected);

                // Print Toast indicating status of the deletion.
                if (deleted == 0) {
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_delete_successful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_delete_failed, Toast.LENGTH_SHORT).show();
                }

                // Close this activity.
                finish();
            }
        });

        // Attach a click listener to buttonSave.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Detach UsersChildEventListener.
                databaseReference.child("Users").child(selected.getKey()).removeEventListener(childEventListener);

                // Modify User selected in the database with the attributes specified in this new User.
                modified = UsersFirebaseHelper.modify(selected, new User(
                        editTextFirstName.getText().toString(),
                        editTextLastName.getText().toString(),
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString())
                );

                // Depending on the status of the modification, print a Toast and take an action.
                if (modified == 0) {
                    // Modification successful. Close this activity.
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_modify_successful, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (modified == 2) {
                    // Modification failed due to invalid attributes. Reattach UsersChildEventListener.
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_invalid, Toast.LENGTH_SHORT).show();
                    databaseReference.child("Users").child(selected.getKey()).addChildEventListener(childEventListener);
                } else {
                    // Modification failed due to database error. Close this activity.
                    Toast.makeText(ModifyUserActivity.this, R.string.toast_modify_failed, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * When this activity leaves the foreground, close this activity.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Detach UsersChildEventListener.
        databaseReference.child("Users").child(selected.getKey()).removeEventListener(childEventListener);

        // Close this activity.
        finish();
    }
}