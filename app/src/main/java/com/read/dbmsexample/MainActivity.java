package com.read.dbmsexample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    /**
     * Setup an activity with a ListView and a custom action bar when this activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bring XML elements to Java.
        toolbar = findViewById(R.id.toolbar);
        listView = findViewById(R.id.list_view);

        // Use custom action bar and set name of activity.
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_main);

        // Define and attach ListView Adapter.
        firebaseHelper.attachListViewAdapter(this, listView);
    }

    /**
     * Start updating the ListView with Users from the database as the database changes when this
     * activity is in the foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();

        firebaseHelper.startUpdatingListView(this);
    }

    /**
     * Stop updating the ListView with Users from the database as the database changes when this
     * activity is not in the foreground.
     */
    @Override
    protected void onPause() {
        super.onPause();

        firebaseHelper.stopUpdatingListView();
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
