package com.read.dbmsexample.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.dbmsexample.Adapters.UsersBaseAdapter;
import com.read.dbmsexample.Firebase.UsersValueEventListener;
import com.read.dbmsexample.Models.User;
import com.read.dbmsexample.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<User> users;
    private UsersBaseAdapter baseAdapter;
    private DatabaseReference databaseReference;
    private UsersValueEventListener valueEventListener;

    /**
     * When this activity is created, inflate a layout with a ListView and initialize objects that
     * work with the ListView.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar text as name of this activity.
        getSupportActionBar().setTitle(R.string.activity_main);

        // Bring XML ListView to Java.
        listView = findViewById(R.id.list_view);

        // Initialize ArrayList and UsersBaseAdapter.
        users = new ArrayList<>();
        baseAdapter = new UsersBaseAdapter(this, users);

        // Initialize DatabaseReference and UsersValueEventListener.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        valueEventListener = new UsersValueEventListener(users, baseAdapter);

        // Set adapter of ListView.
        listView.setAdapter(baseAdapter);
    }

    /**
     * When this activity is in the foreground, start syncing the ListView with the User objects in
     * the database.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Add ValueEventListener to part of database that contains User objects.
        databaseReference.child("Users").addValueEventListener(valueEventListener);
    }

    /**
     * When this activity leaves the foreground, stop syncing the ListView.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Remove ValueEventListener from database.
        databaseReference.child("Users").removeEventListener(valueEventListener);
    }

    /**
     * Specify what actions to place in the action bar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Define what to do in response to action clicks in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Open AddUserActivity when the Add action is clicked.
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
