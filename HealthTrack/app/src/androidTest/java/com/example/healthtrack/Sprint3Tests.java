package com.example.healthtrack;

import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.model.WorkoutPlanDatabaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.util.Log;

public class Sprint3Tests {

    private WorkoutPlanDatabaseRepository repository;
    private final String userId = "testUser"; // Consistent user ID for tests

    @Before
    public void setUp() {
        repository = new WorkoutPlanDatabaseRepository();
        authenticateTestUser();

        // Clear the database before each test
        CountDownLatch latch = new CountDownLatch(1);
        repository.getWorkoutPlansReference(userId).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.e("DBError", "Failed to clear database: " + databaseError.getMessage());
            }
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private void authenticateTestUser() {
        // Implement authentication logic here
        // For example, using FirebaseAuth:
        FirebaseAuth.getInstance().signInWithEmailAndPassword("fakeUser@gmail.com", "password")
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        fail("Authentication failed: " + task.getException().getMessage());
                    }
                });
    }

    @After
    public void tearDown() {
        CountDownLatch latch = new CountDownLatch(1);
        repository.getWorkoutPlansReference(userId).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                System.out.println("Failed to clean up database: " + databaseError.getMessage());
            }
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Test for adding duplicate workouts
    @Test
    public void testAddDuplicateWorkoutPlan() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        String userId = "fakeUserUid"; // Ensure this is the actual user ID you're testing with

        // Authenticate the user
        FirebaseAuth.getInstance().signInWithEmailAndPassword("fakeUser@gmail.com", "password")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "Plan A", 300, 3, 10, 30, "Notes");

                        // First addition
                        repository.addWorkoutPlan(userId, workoutPlan, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.e("DBError", "Error on first add: " + databaseError.getMessage());
                                }
                                // Attempt to add the same workout plan
                                repository.addWorkoutPlan(userId, workoutPlan, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Log.e("DBError", "Error on second add: " + databaseError);
                                        assertNotNull(databaseError); // This should pass if error occurred
                                        latch.countDown();
                                    }
                                });
                            }
                        });
                    } else {
                        Log.e("AuthError", "Authentication failed: " + task.getException().getMessage());
                    }
                });

        latch.await(); // Wait for the operation to complete
    }





    // Test for empty fields in WorkoutPlan
    @Test
    public void testEmptyWorkoutPlanFields() {
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "", 200, 3, 10, 30, ""); // Empty name and notes

        CountDownLatch latch = new CountDownLatch(1);
        repository.addWorkoutPlan(userId, workoutPlan, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                assertNotNull("Should return error for invalid workout plan", databaseError);
            } else {
                fail("Expected error for invalid workout plan, but was added successfully.");
            }
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    // Test for empty workout plan name
    @Test
    public void testWorkoutPlanWithEmptyName() {
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "", 200, 3, 10, 30, "Notes"); // Empty name

        CountDownLatch latch = new CountDownLatch(1);
        repository.addWorkoutPlan(userId, workoutPlan, (databaseError, databaseReference) -> {
            assertNotNull("Should return error for empty workout plan name", databaseError);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    // Test for empty workout plan notes
    @Test
    public void testWorkoutPlanWithEmptyNotes() {
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "Plan B", 200, 3, 10, 30, ""); // Empty notes

        CountDownLatch latch = new CountDownLatch(1);
        repository.addWorkoutPlan(userId, workoutPlan, (databaseError, databaseReference) -> {
            assertNotNull("Should return error for empty workout plan notes", databaseError);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    // Test for zero calories
    @Test
    public void testWorkoutPlanWithZeroCalories() {
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "Plan C", 0, 3, 10, 30, "Notes"); // Zero calories

        CountDownLatch latch = new CountDownLatch(1);
        repository.addWorkoutPlan(userId, workoutPlan, (databaseError, databaseReference) -> {
            assertNotNull("Should return error for zero calories", databaseError);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    // Test for negative calories
    @Test
    public void testWorkoutPlanWithNegativeCalories() {
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "Plan E", -100, 3, 10, 30, "Notes");

        CountDownLatch latch = new CountDownLatch(1);
        repository.addWorkoutPlan(userId, workoutPlan, (databaseError, databaseReference) -> {
            assertNotNull("Should return error for negative calories", databaseError);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    // Test for negative sets
    @Test
    public void testWorkoutPlanWithNegativeSets() {
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "Plan F", 200, -3, 10, 30, "Notes");

        CountDownLatch latch = new CountDownLatch(1);
        repository.addWorkoutPlan(userId, workoutPlan, (databaseError, databaseReference) -> {
            assertNotNull("Should return error for negative sets", databaseError);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    // Test for invalid reps
    @Test
    public void testWorkoutPlanWithInvalidReps() throws InterruptedException {
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, "Plan G", 200, 3, -10, 30, "Notes");

        CountDownLatch latch = new CountDownLatch(1);
        repository.addWorkoutPlan(userId, workoutPlan, (databaseError, databaseReference) -> {
            assertNotNull("Should return error for invalid reps", databaseError);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
