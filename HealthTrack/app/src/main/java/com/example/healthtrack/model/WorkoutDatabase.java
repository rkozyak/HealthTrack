package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkoutDatabase {
    private static WorkoutDatabase instance;
    private DatabaseReference db;

    private WorkoutDatabase() {
        db = FirebaseDatabase.getInstance().getReference("workouts");
    }

    public static WorkoutDatabase getInstance() {
        if (instance == null) {
            synchronized (WorkoutDatabase.class) {
                if (instance == null) {
                    instance = new WorkoutDatabase();
                }
            }
        }

        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return db;
    }
}
