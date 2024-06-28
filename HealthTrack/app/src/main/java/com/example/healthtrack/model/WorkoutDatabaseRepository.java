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
        DatabaseReference userRef = db.child(user.getUserId());
        String workoutId = userRef.push().getKey();

        if (workoutId != null) {
            userRef.child(workoutId).setValue(workout, completionListener);
        }
    }
}
