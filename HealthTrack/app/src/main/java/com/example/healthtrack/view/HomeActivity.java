package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtrack.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    // Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_activity);

        // Firebase stuff
        mAuth = FirebaseAuth.getInstance();


        // Buttons
        Button loginButton = findViewById(R.id.loginButton);
        Button accountInfoButton = findViewById(R.id.accountInfoButton);
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        Button openTrackerButton = findViewById(R.id.trackerButton);
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        Button openCommunityButton = findViewById(R.id.communityButton);

        // Check if user is logged in
        if (mAuth.getCurrentUser() == null) {
            // user is not logged in
            loginButton.setVisibility(View.VISIBLE);
            accountInfoButton.setVisibility(View.GONE);
        } else {
            // user is logged in
            loginButton.setVisibility(View.GONE);
            accountInfoButton.setVisibility(View.VISIBLE);
        }

        // Login Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Account Info Button
        accountInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActivity.this, AccountInfo.class);
                startActivity(intent);
            }
        });

        // Calorie Button
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActivity.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActivity.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActivity.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActivity.this, CommunityScreen.class);
                startActivity(intent);
            }
        });
    }
}