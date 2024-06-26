package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDatabase {
    private static UserDatabase instance;
    private DatabaseReference db;

    private UserDatabase() {
        db = FirebaseDatabase.getInstance().getReference("users");
    }

    public static UserDatabase getInstance() {
        if (instance == null) {
            synchronized (WorkoutDatabase.class) {
                if (instance == null) {
                    instance = new UserDatabase();
                }
            }
        }

        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return db;
    }
}
