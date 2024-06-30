package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtrack.R;
import com.example.healthtrack.model.User;
import com.example.healthtrack.model.UserDatabaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CalorieTracking extends AppCompatActivity {

    private TextView textViewCaloriesToBurn;
    private TextView textViewBurntCalories;
    private UserDatabaseRepository userDatabaseRepository;
    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calorie_tracking);

        // Linking text to variables
        textViewBurntCalories = findViewById(R.id.calories_burnt);
        textViewCaloriesToBurn = findViewById(R.id.calories_to_burn);


        Button backButton = findViewById(R.id.btn_calorie_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalorieTracking.this, HomeActivity.class);
                startActivity(intent);
            }
        });



        // Below are the button for the navigation bar
        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CalorieTracking.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CalorieTracking.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CalorieTracking.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(CalorieTracking.this, CommunityScreen.class);
                startActivity(intent);
            }
        });
    }

    private void loadUserInfo() {
        // get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if user is logged in
        if (currentUser == null) {
            // no user
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // get user info
        String userId = currentUser.getUid();
        DatabaseReference userRef = userDatabaseRepository.getUserReference(userId);

        // Listen for changes and get user info
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    if (user.getGender().equals("Male")) {
                        textViewCaloriesToBurn.setText(String.valueOf((10 * user.getWeight() + (6.25 * user.getHeight()) - 100)));
                    } else if (user.getGender().equals("Female")) {
                        textViewCaloriesToBurn.setText(String.valueOf((10 * user.getWeight() + (6.25 * user.getHeight()) - 260)));
                    } else {
                        // this estimated calculation is the average of male and female BMR
                        textViewCaloriesToBurn.setText(String.valueOf((10 * user.getWeight() + (6.25 * user.getHeight()) - 180)));
                    }
                } else {
                    textViewBurntCalories.setText("0");
                    textViewCaloriesToBurn.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // say error about not getting userData
                Toast.makeText(CalorieTracking.this, "Failed to load user information", Toast.LENGTH_SHORT).show();
            }
        });

    }
}