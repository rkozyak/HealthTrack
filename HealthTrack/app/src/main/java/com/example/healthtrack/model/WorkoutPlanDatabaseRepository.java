package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;

public class WorkoutPlanDatabaseRepository {
    private WorkoutPlanDatabase workoutPlanDatabase;
    private DatabaseReference db;

    public WorkoutPlanDatabaseRepository() {
        workoutPlanDatabase = WorkoutPlanDatabase.getInstance();
        db = workoutPlanDatabase.getDatabaseReference();
    }

    public void addWorkout(String userId, WorkoutPlan workoutPlan,
                           DatabaseReference.CompletionListener completionListener) {
        DatabaseReference userRef = db.child(userId);
        String workoutId = userRef.push().getKey();

        if (workoutId != null) {
            userRef.child(workoutId).setValue(workoutPlan, completionListener);
        }
    }

    //Note: Gets USER SPECIFIC workouts
    public DatabaseReference getWorkoutsReference(String userId) {
        return db.child(userId);
    }
}
