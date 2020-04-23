package com.read.dbmsexample.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.read.dbmsexample.Adapters.UsersBaseAdapter;
import com.read.dbmsexample.Models.User;

import java.util.ArrayList;

public class UsersValueEventListener implements ValueEventListener {

    private ArrayList<User> users;
    private UsersBaseAdapter baseAdapter;

    /**
     * Defines a ValueEventListener that syncs a given ArrayList with User objects in the database.
     * When the ArrayList is synced, a given BadeAdapter is notified so that it can update its
     * ListView.
     * @param users
     * @param baseAdapter
     */
    public UsersValueEventListener(ArrayList<User> users, UsersBaseAdapter baseAdapter) {
        this.users = users;
        this.baseAdapter = baseAdapter;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Clear the ArrayList.
        users.clear();

        // Retrieve new User objects from the database and store them in the ArrayList.
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            User user = new User(
                    ds.getKey(),
                    ds.child("firstName").getValue(String.class),
                    ds.child("lastName").getValue(String.class),
                    ds.child("username").getValue(String.class),
                    ds.child("password").getValue(String.class),
                    ds.child("role").getValue(String.class)
            );
            users.add(user);
        }

        // Notify the BaseAdapter to update its ListView.
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
}
