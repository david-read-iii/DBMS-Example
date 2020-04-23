package com.read.dbmsexample.Firebase;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.read.dbmsexample.Models.User;

public class UsersFirebaseHelper {

    /**
     * Saves a new User to the database.
     *
     * @param user The User to be saved. Must have a null key.
     * @return The status of the save:
     *              0 indicates successful save
     *              1 indicates a failed save due to database error
     *              2 indicates a failed save due to an attribute with invalid text
     */
    public static int save(User user) {
        int saved;

        // Verify that the User has valid attributes defined.
        if (user == null) {
            // TODO: Define some restrictions on what attributes can be entered for the object.
            saved = 2;
        }
        // Attempt to save User object to the database. Watch for a DatabaseException.
        else {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Users").push().setValue(user);
                saved = 0;

            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = 1;
            }
        }
        return saved;
    }

    /**
     * Deletes a User from the database.
     *
     * @param user The User to be deleted. Must have a key defined.
     * @return The status of the deletion:
     *              0 indicates successful deletion
     *              1 indicates a failed deletion due to database error
     */
    public static int delete(User user) {
        int deleted;

        // Attempt to delete the User object from the database. Watch for a DatabaseException.
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Users").child(user.getKey()).removeValue();
            deleted = 0;
        } catch (DatabaseException e) {
            e.printStackTrace();
            deleted = 1;
        }

        return deleted;
    }

    /**
     * Deletes an existing User from the database and replaces it with a new User.
     *
     * @param oldUser The User to be deleted. Must have a key defined.
     * @param newUser The User to replace the deleted. Must have a null key.
     * @return The status of the modification:
     *              0 indicates successful modification
     *              1 indicates a failed modification due to database error in the deletion step
     *              2 indicates a failed modification due to an attribute with invalid text
     *              3 indicates a failed modification due to database error in the save step
     */
    public static int modify(User oldUser, User newUser) {
        int modified;

        // Save newUser object to the database.
        modified = UsersFirebaseHelper.save(newUser);

        // If save is successful, delete oldUser from the database.
        if (modified == 0) {
            modified = UsersFirebaseHelper.delete(oldUser);
        } else {
            modified = 3;
        }

        return modified;
    }
}