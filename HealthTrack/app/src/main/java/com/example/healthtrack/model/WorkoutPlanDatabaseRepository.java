package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;

public class WorkoutPlanDatabaseRepository {
    private WorkoutPlanDatabase workoutPlanDatabase;
    private DatabaseReference db;

    public WorkoutPlanDatabaseRepository() {
        workoutPlanDatabase = WorkoutPlanDatabase.getInstance();
        db = workoutPlanDatabase.getDatabaseReference();
    }

    public void addWorkoutPlan(String userId, WorkoutPlan workoutPlan,
                           DatabaseReference.CompletionListener completionListener) {
        DatabaseReference userRef = db.child(userId);
        String workoutPlanId = userRef.push().getKey();

        if (workoutPlanId != null) {
            userRef.child(workoutPlanId).setValue(workoutPlan, completionListener);
        }
    }

    //Note: Gets USER SPECIFIC workouts
    public DatabaseReference getWorkoutPlansReference(String userId) {
        return db.child(userId);
    }
}
