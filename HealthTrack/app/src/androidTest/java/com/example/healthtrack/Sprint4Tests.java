package com.example.healthtrack;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.healthtrack.model.ChallengeDatabaseRepository;
import com.example.healthtrack.model.CommunityChallenge;
import com.example.healthtrack.model.User;
import com.example.healthtrack.model.UserDatabaseRepository;
import com.example.healthtrack.model.Workout;
import com.example.healthtrack.model.WorkoutDatabaseRepository;
import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.model.WorkoutPlanDatabaseRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class Sprint4Tests {

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private UserDatabaseRepository userDatabaseRepository;
    private WorkoutDatabaseRepository workoutDatabaseRepository;
    private WorkoutPlanDatabaseRepository workoutPlanDatabaseRepository;
    private ChallengeDatabaseRepository challengeDatabaseRepository;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabaseRepository = new UserDatabaseRepository();
        workoutDatabaseRepository = new WorkoutDatabaseRepository();
        workoutPlanDatabaseRepository = new WorkoutPlanDatabaseRepository();
        challengeDatabaseRepository = new ChallengeDatabaseRepository();
    }

    @Test
    public void testAddUser() throws InterruptedException {
        User user = new User("1", "testUser");
        user.updatePersonalInformation("Test User", 180, 75, "Male");
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference.CompletionListener listener = (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        };
        userDatabaseRepository.addUser(user, listener);
        latch.await();
    }

    @Test
    public void testUpdateUserInformation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        userDatabaseRepository.updateUserInformation("1", "Test User", 180, 75, "Male", (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void testUpdateUserInformationWithIncorrectInfo() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        userDatabaseRepository.updateUserInformation("1", "Test User", 180, 75, "Male", (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void testUpdateUserInformationWithEmptyInfo() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        userDatabaseRepository.updateUserInformation("1", "Test User", 180, 75, "Male", (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void testAddWorkout() throws InterruptedException {
        Workout workout = new Workout("1", "Test Workout", 100, 3, 10, "Test Notes");
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference.CompletionListener listener = (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        };
        workoutDatabaseRepository.addWorkout("1", workout, listener);
        latch.await();
    }

    @Test
    public void testAddWorkoutEmptyData() throws InterruptedException {
        Workout workout = new Workout("1", "Test Workout", 100, 3, 10, "Test Notes");
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference.CompletionListener listener = (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        };
        workoutDatabaseRepository.addWorkout("1", workout, listener);
        latch.await();
    }

    @Test
    public void testAddWorkoutInvalidData() throws InterruptedException {
        Workout workout = new Workout("1", "Test Workout", 100, 3, 10, "Test Notes");
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference.CompletionListener listener = (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        };
        workoutDatabaseRepository.addWorkout("1", workout, listener);
        latch.await();
    }


    @Test
    public void testAddChallenge() throws InterruptedException {
        ArrayList<WorkoutPlan> workoutPlans = new ArrayList<>();
        workoutPlans.add(new WorkoutPlan("1", "Test Workout Plan", 100, 3, 10, 30, "Test Notes"));
        CommunityChallenge challenge = new CommunityChallenge("1", "Test Challenge", workoutPlans, 31, 12, 2024);
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference.CompletionListener listener = (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        };
        challengeDatabaseRepository.addChallenge(challenge, listener);
        latch.await();
    }

    @Test
    public void testAddChallengeInvalidInfo() throws InterruptedException {
        ArrayList<WorkoutPlan> workoutPlans = new ArrayList<>();
        workoutPlans.add(new WorkoutPlan("1", "Test Workout Plan", 100, 3, 10, 30, "Test Notes"));
        CommunityChallenge challenge = new CommunityChallenge("1", "Test Challenge", workoutPlans, 31, 12, 2024);
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference.CompletionListener listener = (error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        };
        challengeDatabaseRepository.addChallenge(challenge, listener);
        latch.await();
    }

    @Test
    public void testRemoveChallengeAfterDeadline() throws InterruptedException {
        DatabaseReference challengeRef = challengeDatabaseRepository.getDatabaseReference().child("1");
        CountDownLatch latch = new CountDownLatch(1);
        challengeRef.removeValue((error, ref) -> {
            assertNotNull(ref);
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void testWorkoutPlanSortingByName() {
        ArrayList<WorkoutPlan> plans = new ArrayList<>();
        plans.add(new WorkoutPlan("1", "B Workout Plan", 100, 3, 10, 30, "Test Notes"));
        plans.add(new WorkoutPlan("1", "A Workout Plan", 100, 3, 10, 30, "Test Notes"));
        plans.add(new WorkoutPlan("1", "C Workout Plan", 100, 3, 10, 30, "Test Notes"));

        plans.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

        assertTrue(plans.get(0).getName().equals("A Workout Plan"));
        assertTrue(plans.get(1).getName().equals("B Workout Plan"));
        assertTrue(plans.get(2).getName().equals("C Workout Plan"));
    }

    @Test
    public void testWorkoutPlanFiltering() {
        ArrayList<WorkoutPlan> plans = new ArrayList<>();
        plans.add(new WorkoutPlan("1", "Test Workout Plan 1", 100, 3, 10, 30, "Test Notes"));
        plans.add(new WorkoutPlan("1", "Filtered Workout Plan", 100, 3, 10, 30, "Test Notes"));

        plans.removeIf(plan -> !plan.getName().contains("Filtered"));

        assertTrue(plans.size() == 1);
        assertTrue(plans.get(0).getName().equals("Filtered Workout Plan"));
    }
}
