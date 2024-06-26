package com.example.healthtrack.model;

public class WorkoutDatabase {
    private static WorkoutDatabase instance;

    private WorkoutDatabase() {

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
}
