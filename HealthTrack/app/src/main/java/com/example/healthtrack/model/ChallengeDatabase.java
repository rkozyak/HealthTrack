package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChallengeDatabase {
    private static ChallengeDatabase instance;
    private DatabaseReference db;

    private ChallengeDatabase() {
        db = FirebaseDatabase.getInstance().getReference("challenges");
    }

    public static ChallengeDatabase getInstance() {
        if (instance == null) {
            synchronized (ChallengeDatabase.class) {
                if (instance == null) {
                    instance = new ChallengeDatabase();
                }
            }
        }

        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return db;
    }
}
