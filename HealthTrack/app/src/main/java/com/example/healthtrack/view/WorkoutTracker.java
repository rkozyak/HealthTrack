package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtrack.R;
import com.example.healthtrack.model.WorkoutDatabaseRepository;
import com.example.healthtrack.viewModel.WorkoutViewModel;
import com.example.healthtrack.model.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class WorkoutTracker extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private WorkoutDatabaseRepository workoutDatabaseRepository;
    private WorkoutViewModel workoutViewModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_tracker);

        mAuth = FirebaseAuth.getInstance();
        workoutDatabaseRepository = new WorkoutDatabaseRepository();
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        Button backButton = findViewById(R.id.btn_tracker_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkoutTracker.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, CommunityScreen.class);
                startActivity(intent);
            }
        });

        Button addWorkoutButton = findViewById(R.id.addWorkout);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopupWindow();
            }
        });


    }

    private void showPopupWindow() {
        // Inflate the popup_layout.xml
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.workout_popout, null);

        // Create the PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // Lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(getWindow().findViewById(android.R.id.content), Gravity.CENTER, 0, 0);

        // Set up the submit button inside the popup
        Button submitButton = popupView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from EditTexts
                EditText editName = popupView.findViewById(R.id.editName);
                EditText editSets = popupView.findViewById(R.id.editSets);
                EditText editReps = popupView.findViewById(R.id.editSetReps);
                EditText editCals = popupView.findViewById(R.id.editCaloriesPerRep);
                EditText editNotes = popupView.findViewById(R.id.editNotes);

                String name = editName.getText().toString();
                String sets = editSets.getText().toString();
                String reps = editReps.getText().toString();
                String cals = editCals.getText().toString();
                String notes = editNotes.getText().toString();


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(sets) || TextUtils.isEmpty(reps)
                        || TextUtils.isEmpty(cals)) {
                    Toast.makeText(WorkoutTracker.this, "Please fill in all necessary fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int setsInt = Integer.parseInt(sets);
                int repsInt = Integer.parseInt(reps);
                int calsInt = Integer.parseInt(cals);

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String userId = currentUser.getUid();

                Workout workout = new Workout(userId, name, setsInt, repsInt, calsInt, notes);
                // Save data to Firebase
                workoutViewModel.addWorkout(userId, workout);

                // Dismiss the popup window
                popupWindow.dismiss();
            }
        });
    }



}