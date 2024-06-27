package com.example.healthtrack.view;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtrack.R;
import com.example.healthtrack.model.User;
import com.example.healthtrack.model.Workout;
import com.example.healthtrack.viewModel.UserViewModel;
import com.example.healthtrack.viewModel.WorkoutViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    // Instance Variables
    private UserViewModel userViewModel;
    private WorkoutViewModel workoutViewModel;
    private FirebaseAuth auth;

    // Constants
    private static final String USER1_EMAIL = "user1@gmail.com";
    private static final String USER1_PASSWORD = "user1password";
    private static final Integer USER1_HEIGHT = 180;
    private static final Integer USER1_WEIGHT = 70;
    private static final String USER1_GENDER = "Male";

    private static final String USER1_WORKOUT1_NAME = "workoutName1User1";
    private static final Integer USER1_WORKOUT1_CALORIES = 11;
    private static final Integer USER1_WORKOUT1_SETS = 110;
    private static final Integer USER1_WORKOUT1_REPS = 1100;
    private static final String USER1_WORKOUT1_NOTES = "workoutNotes1User1";
    private static final String USER1_WORKOUT2_NAME = "workoutName2User1";
    private static final Integer USER1_WORKOUT2_CALORIES = 12;
    private static final Integer USER1_WORKOUT2_SETS = 120;
    private static final Integer USER1_WORKOUT2_REPS = 1200;
    private static final String USER1_WORKOUT2_NOTES = "workoutNotes2User1";


    private static final String USER2_EMAIL = "user2@gmail.com";
    private static final String USER2_PASSWORD = "user2password";
    private static final Integer USER2_HEIGHT = 160;
    private static final Integer USER2_WEIGHT = 50;
    private static final String USER2_GENDER = "Female";
    private static final String USER2_WORKOUT1_NAME = "workoutName1User2";
    private static final Integer USER2_WORKOUT1_CALORIES = 21;
    private static final Integer USER2_WORKOUT1_SETS = 210;
    private static final Integer USER2_WORKOUT1_REPS = 2100;
    private static final String USER2_WORKOUT1_NOTES = "workoutNotes1User2";
    private static final String USER2_WORKOUT2_NAME = "workoutName2User2";
    private static final Integer USER2_WORKOUT2_CALORIES = 22;
    private static final Integer USER2_WORKOUT2_SETS = 220;
    private static final Integer USER2_WORKOUT2_REPS = 2200;
    private static final String USER2_WORKOUT2_NOTES = "workoutNotes2User2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setDataInitialized(false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        auth = FirebaseAuth.getInstance();

        if (!isDataInitialized()) {
            Log.d(TAG, "Data not initialized. Populating initial data.");
            populateInitialData();
        } else {
            Log.d(TAG, "Data already initialized. Proceeding to next screen.");
        }

        Button btnBrowse = findViewById(R.id.btnBrowse);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        // Intent should switch the activity on screen to HomeActivity when btnBrowse is clicked
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create an intent to start a second activity
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Intent should switch the activity on screen to LoginActivity when btnLogin is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create an intent to start a second activity
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Intent should switch the activity on screen to AccountCreationActivity when
        // btnRegister is clicked
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create an intent to start a second activity
                Intent intent = new Intent(WelcomeActivity.this, AccountCreationActivity.class);
                startActivity(intent);
            }
        });

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isDataInitialized() {
        return getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("data_initialized", false);
    }

    private void setDataInitialized(boolean initialized) {
        getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                .putBoolean("data_initialized", initialized).apply();
        Log.d(TAG, "Data initialization flag set to " + initialized);
    }

    private void populateInitialData() {
        // Create test user 1
        auth.createUserWithEmailAndPassword(USER1_EMAIL, USER1_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser1 = auth.getCurrentUser();
                        if (firebaseUser1 != null) {
                            String userId1 = firebaseUser1.getUid();
                            User user1 = new User(userId1, USER1_EMAIL);
                            Log.d(TAG, "User 1 created with UID: " + userId1);

                            userViewModel.addUser(user1);
                            userViewModel.updateUserInformation(userId1, USER1_HEIGHT, USER1_WEIGHT,
                                    USER1_GENDER);

                            Workout workout1User1 = new Workout(userId1, USER1_WORKOUT1_NAME,
                                    USER1_WORKOUT1_CALORIES, USER1_WORKOUT1_SETS,
                                    USER1_WORKOUT1_REPS, USER1_WORKOUT1_NOTES);
                            Workout workout2User1 = new Workout(userId1, USER1_WORKOUT2_NAME,
                                    USER1_WORKOUT2_CALORIES, USER1_WORKOUT2_SETS,
                                    USER1_WORKOUT2_REPS, USER1_WORKOUT2_NOTES);

                            workoutViewModel.addWorkout(user1, workout1User1);
                            workoutViewModel.addWorkout(user1, workout2User1);
                            Log.d(TAG, "Workouts for User 1 added.");
                        } else {
                            Log.e(TAG, "FirebaseUser1 is null.");
                        }
                    } else {
                        Log.e(TAG, "Error creating user 1: " + task.getException().getMessage());
                    }
                });

        // Create test user 2
        auth.createUserWithEmailAndPassword(USER2_EMAIL, USER2_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser2 = auth.getCurrentUser();
                        if (firebaseUser2 != null) {
                            String userId2 = firebaseUser2.getUid();
                            User user2 = new User(userId2, USER2_EMAIL);
                            Log.d(TAG, "User 2 created with UID: " + userId2);

                            userViewModel.addUser(user2);
                            userViewModel.updateUserInformation(userId2, USER2_HEIGHT, USER2_WEIGHT,
                                    USER2_GENDER);


                            Workout workout1User2 = new Workout(userId2, USER2_WORKOUT1_NAME,
                                    USER2_WORKOUT1_CALORIES, USER2_WORKOUT1_SETS,
                                    USER2_WORKOUT1_REPS, USER2_WORKOUT1_NOTES);
                            Workout workout2User2 = new Workout(userId2, USER2_WORKOUT2_NAME,
                                    USER2_WORKOUT2_CALORIES, USER2_WORKOUT2_SETS,
                                    USER2_WORKOUT2_REPS, USER2_WORKOUT2_NOTES);

                            workoutViewModel.addWorkout(user2, workout1User2);
                            workoutViewModel.addWorkout(user2, workout2User2);
                            Log.d(TAG, "Workouts for User 2 added.");
                        } else {
                            Log.e(TAG, "FirebaseUser2 is null.");
                        }
                    } else {
                        Log.e(TAG, "Error creating user 2: " + task.getException().getMessage());
                    }
                });

        setDataInitialized(true);
    }
}