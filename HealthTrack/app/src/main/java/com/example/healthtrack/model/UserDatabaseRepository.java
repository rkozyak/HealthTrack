package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;

public class UserDatabaseRepository {
    private UserDatabase userDatabase;
    private DatabaseReference db;

    public UserDatabaseRepository() {
        userDatabase = UserDatabase.getInstance();
        db = userDatabase.getDatabaseReference();
    }

    public void addUser(User user, DatabaseReference.CompletionListener completionListener) {
        db.child(user.getUserId()).setValue(user, completionListener);
    }

    public void updateUserInformation(String userId, String name,
                                      Integer height, Integer weight, String gender,
                                      DatabaseReference.CompletionListener completionListener) {
        db.child(userId).child("name").setValue(name, completionListener);
        db.child(userId).child("height").setValue(height, completionListener);
        db.child(userId).child("weight").setValue(weight, completionListener);
        db.child(userId).child("gender").setValue(gender, completionListener);
    }
}
