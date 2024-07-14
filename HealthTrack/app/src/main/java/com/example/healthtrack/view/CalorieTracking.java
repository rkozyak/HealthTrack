package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtrack.R;
import com.example.healthtrack.model.User;
import com.example.healthtrack.model.UserDatabaseRepository;
import com.example.healthtrack.model.WorkoutDatabaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

public class CalorieTracking extends AppCompatActivity {

    private TextView textViewCaloriesToBurn;
    private TextView textViewBurntCalories;
    private UserDatabaseRepository userDatabaseRepository;
    private FirebaseAuth mAuth;
    private WorkoutDatabaseRepository workoutDatabaseRepository;
    private int totalCaloriesBurnt = 0;
    private double totalCaloriesGoal = 0;
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

        // Update Calories
        mAuth = FirebaseAuth.getInstance();
        userDatabaseRepository = new UserDatabaseRepository();
        loadUserInfo();

        Button showChartBtn = findViewById(R.id.showChartButton);
        showChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PieChart pieChart = findViewById(R.id.chart);

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(totalCaloriesBurnt, "Calories Burnt"));

                entries.add(new PieEntry((int) totalCaloriesGoal - totalCaloriesBurnt, "Calories to Burn"));

                PieDataSet pieDataSet = new PieDataSet(entries, "Key");
                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);

                pieChart.getDescription().setEnabled(false);
                pieChart.animateY(1000);
                pieChart.invalidate();
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
                        totalCaloriesGoal += 10 * user.getWeight()
                                + (6.25 * user.getHeight()) - 100;
                        textViewCaloriesToBurn.setText(String.valueOf(totalCaloriesGoal));
                    } else if (user.getGender().equals("Female")) {
                        totalCaloriesGoal += 10 * user.getWeight()
                                + (6.25 * user.getHeight()) - 260;
                        textViewCaloriesToBurn.setText(String.valueOf(totalCaloriesGoal));
                    } else {
                        // this estimated calculation is the average of male and female BMR
                        totalCaloriesGoal += 10 * user.getWeight()
                                + (6.25 * user.getHeight()) - 180;
                        textViewCaloriesToBurn.setText(String.valueOf(totalCaloriesGoal));
                    }
                } else {
                    totalCaloriesGoal = 0;
                    textViewBurntCalories.setText(String.valueOf(totalCaloriesGoal));
                    textViewCaloriesToBurn.setText(String.valueOf(totalCaloriesGoal));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // say error about not getting userData
                Toast.makeText(CalorieTracking.this, "Failed to load user information",
                        Toast.LENGTH_SHORT).show();
            }
        });
        workoutDatabaseRepository = new WorkoutDatabaseRepository();
        DatabaseReference workout = workoutDatabaseRepository.getWorkoutsReference(userId);
        workout.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {
                HashMap<String,  Object> workoutData
                        = (HashMap<String, Object>) snapshot.getValue();
                Integer calPerSet = Integer.parseInt(workoutData.get("caloriesPerSet").toString());
                Integer numSet = Integer.parseInt(workoutData.get("sets").toString());
                totalCaloriesBurnt += calPerSet * numSet;
                textViewBurntCalories.setText(String.valueOf(totalCaloriesBurnt));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}