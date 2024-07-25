package com.example.healthtrack.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtrack.R;
import com.example.healthtrack.model.CommunityChallenge;
import com.example.healthtrack.model.Observer;
import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.viewModel.CommunityViewModel;
import com.example.healthtrack.viewModel.WorkoutPlanViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommunityScreen extends AppCompatActivity implements Observer {
    private Dialog dialog;
    private Dialog dialog2;
    private Button btnDialogAdd;
    private Button btnDialogWorkout;
    private FirebaseAuth mAuth;
    private CommunityViewModel communityViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community_screen);

        communityViewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(CommunityScreen.this);
        dialog.setContentView(R.layout.add_challenge_popout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        if (AddWorkoutCommunity.returnList != null && !AddWorkoutCommunity.returnList.isEmpty()) {
            Toast.makeText(CommunityScreen.this, "Workout Plans added to new challenge", Toast.LENGTH_SHORT).show();
        }

        Button backButton = findViewById(R.id.btn_community_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityScreen.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CommunityScreen.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CommunityScreen.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CommunityScreen.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CommunityScreen.this, CommunityScreen.class);
                startActivity(intent);
            }
        });

        // Add Challenge Button
        Button addChallengeButton = findViewById(R.id.addChallengeButton);
        addChallengeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
                TextView workoutPlanDisplay = dialog.findViewById(R.id.workoutPlanDisplay);
                String workoutPlanString = "Workout Plans: ";
                if (AddWorkoutCommunity.returnList != null && !AddWorkoutCommunity.returnList.isEmpty()) {
                    for (WorkoutPlan plan : AddWorkoutCommunity.returnList) {
                        workoutPlanString = String.join(", ", workoutPlanString, plan.getName());
                    }
                }
                workoutPlanDisplay.setText(workoutPlanString);
            }
        });

        btnDialogAdd = dialog.findViewById(R.id.publishChallenge);
        btnDialogWorkout = dialog.findViewById(R.id.addWorkoutPlan);
        btnDialogWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityScreen.this, AddWorkoutCommunity.class);
                startActivity(intent);
            }
        });
        btnDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText challengeNameInput = dialog.findViewById(R.id.editChallengeName);
                EditText descriptionInput = dialog.findViewById(R.id.description);
                EditText yearInput = dialog.findViewById(R.id.year);
                EditText monthInput = dialog.findViewById(R.id.month);
                EditText dayInput = dialog.findViewById(R.id.day);

                String challengeName = challengeNameInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String year = yearInput.getText().toString();
                String month = monthInput.getText().toString();
                String day = dayInput.getText().toString();

                if (TextUtils.isEmpty(challengeName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(year)
                        || TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
                    Toast.makeText(CommunityScreen.this,
                            "Please fill in all necessary fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int yearInt;
                int monthInt;
                int dayInt;
                try {
                    yearInt = Integer.parseInt(year);
                    monthInt = Integer.parseInt(month);
                    dayInt = Integer.parseInt(day);
                } catch (NumberFormatException e) {
                    Toast.makeText(CommunityScreen.this,
                            "Please ensure that no non-number characters are in date fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (AddWorkoutCommunity.returnList == null || AddWorkoutCommunity.returnList.isEmpty()) {
                    Toast.makeText(CommunityScreen.this, "Please add at least one workout plan", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String userId = currentUser.getUid();

                CommunityChallenge challenge = new CommunityChallenge(userId, challengeName,
                        new ArrayList<WorkoutPlan>(AddWorkoutCommunity.returnList), dayInt, monthInt, yearInt);

                communityViewModel.addChallenge(userId, challenge);

                AddWorkoutCommunity.returnList.clear();

                challengeNameInput.setText("");
                descriptionInput.setText("");
                yearInput.setText("");
                monthInput.setText("");
                dayInput.setText("");

                dialog.dismiss();
            }
        });
    }


    @Override
    public void update(String message) {
        // Update UI components with the new status
        System.out.println("CommunityScreen updated with message: " + message);
    }
}