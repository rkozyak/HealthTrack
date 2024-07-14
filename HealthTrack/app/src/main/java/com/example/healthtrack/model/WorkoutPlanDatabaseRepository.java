package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class WorkoutPlanDatabaseRepository {
    private WorkoutPlanDatabase workoutPlanDatabase;
    private DatabaseReference db;

    public WorkoutPlanDatabaseRepository() {
        workoutPlanDatabase = WorkoutPlanDatabase.getInstance();
        db = workoutPlanDatabase.getDatabaseReference();
    }

    public void addWorkoutPlan(String userId, WorkoutPlan workoutPlan,
                               DatabaseReference.CompletionListener completionListener) {
        // Check for null values
        if (userId == null || workoutPlan == null) {
            if (completionListener != null) {
                completionListener.onComplete(DatabaseError.fromCode(DatabaseError.UNKNOWN_ERROR), null);
            }
            return; // Exit the method early if any parameter is null
        }

        // Check for zero or negative calories
        if (workoutPlan.getCaloriesPerSet() == null || workoutPlan.getCaloriesPerSet() <= 0) {
            completionListener.onComplete(DatabaseError.fromCode(DatabaseError.UNKNOWN_ERROR), null);
            return;
        }

        // Check for empty notes
        if (workoutPlan.getNotes() == null || workoutPlan.getNotes().trim().isEmpty()) {
            if (completionListener != null) {
                completionListener.onComplete(DatabaseError.fromCode(DatabaseError.UNKNOWN_ERROR), null); // Generic error for empty notes
            }
            return;
        }

        DatabaseReference userRef = db.child(userId);

        // Check for duplicates
        userRef.orderByChild("name").equalTo(workoutPlan.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Duplicate found
                    if (completionListener != null) {
                        completionListener.onComplete(DatabaseError.fromCode(DatabaseError.EXPIRED_TOKEN), null); // Error for duplicate
                    }
                } else {
                    String workoutPlanId = userRef.push().getKey();
                    if (workoutPlanId != null) {
                        userRef.child(workoutPlanId).setValue(workoutPlan, completionListener);
                    } else {
                        if (completionListener != null) {
                            completionListener.onComplete(DatabaseError.fromCode(DatabaseError.UNKNOWN_ERROR), null);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (completionListener != null) {
                    completionListener.onComplete(databaseError, null);
                }
            }
        });
    }






    //Note: Gets USER SPECIFIC workouts
    public DatabaseReference getWorkoutPlansReference(String userId) {
        return db.child(userId);
    }
}
