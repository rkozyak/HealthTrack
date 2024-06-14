package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthtrack.R;

public class HomeActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_actvity);

        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActvity.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActvity.this, WorkoutTracker.class);
                startActivity(intent);
            }
            });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActvity.this, WorkoutPlans.class);
                startActivity(intent);
            }
            });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent  = new Intent(HomeActvity.this, CommunityScreen.class);
                startActivity(intent);
            }
            });
    }
}