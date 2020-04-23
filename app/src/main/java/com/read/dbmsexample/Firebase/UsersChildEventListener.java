package com.read.dbmsexample.Firebase;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.read.dbmsexample.R;

public class UsersChildEventListener implements ChildEventListener {

    private Context context;

    /**
     * Defines a listener that finishes the activity the user is in if a certain child in the
     * database is changed. Also prints an indication Toast.
     */
    public UsersChildEventListener(Context context) {
        this.context = context;
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Toast.makeText(context, R.string.toast_changed, Toast.LENGTH_SHORT).show();
        ((Activity) context).finish();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
