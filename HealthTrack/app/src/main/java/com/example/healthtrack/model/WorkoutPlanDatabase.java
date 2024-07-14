package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkoutPlanDatabase {
    private static WorkoutPlanDatabase instance;
    private DatabaseReference db;

    private WorkoutPlanDatabase() {
        db = FirebaseDatabase.getInstance().getReference("workout plans");
    }

    public static WorkoutPlanDatabase getInstance() {
        if (instance == null) {
            synchronized (WorkoutPlanDatabase.class) {
                if (instance == null) {
                    instance = new WorkoutPlanDatabase();
                }
            }
        }

        return instance;
    }

    public DatabaseReference getDatabaseReference() {
        return db;
    }
}
