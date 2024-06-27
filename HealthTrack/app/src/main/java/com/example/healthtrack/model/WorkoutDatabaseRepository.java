package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;

public class WorkoutDatabaseRepository {
    private WorkoutDatabase workoutDatabase;
    private DatabaseReference db;

    public WorkoutDatabaseRepository() {
        workoutDatabase = WorkoutDatabase.getInstance();
        db = workoutDatabase.getDatabaseReference();
    }

    public void addWorkout(User user, Workout workout,
                           DatabaseReference.CompletionListener completionListener) {
        db.child(user.getUserId()).child(workout.getName()).setValue(workout, completionListener);
    }
}
