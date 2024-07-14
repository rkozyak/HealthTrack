package com.example.healthtrack.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtrack.R;
import com.example.healthtrack.model.Workout;
import com.example.healthtrack.model.WorkoutPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkoutPlans extends AppCompatActivity {
    Dialog dialog;
    Button btnDialogAdd;
    RecyclerView recyclerView;
    DatabaseReference database;
    WorkoutPlanAdapter workoutPlanAdapter;
    ArrayList<WorkoutPlan> planList;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_plans);

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.planList);
        database = FirebaseDatabase.getInstance().getReference("workout plans");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        planList = new ArrayList<>();
        workoutPlanAdapter = new WorkoutPlanAdapter(this, planList);
        recyclerView.setAdapter(workoutPlanAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    WorkoutPlan workoutPlan = dataSnapshot.getValue(WorkoutPlan.class);
                    planList.add(workoutPlan);
                }
                workoutPlanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialog = new Dialog(WorkoutPlans.this);
        dialog.setContentView(R.layout.workout_plan_popout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        btnDialogAdd = dialog.findViewById(R.id.publishPlan);

        btnDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button backButton = findViewById(R.id.btn_plans_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkoutPlans.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutPlans.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutPlans.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutPlans.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutPlans.this, CommunityScreen.class);
                startActivity(intent);
            }
        });

        Button createPlan = findViewById(R.id.plan_btn);
        createPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.workout_plan_popout, null);
        Button publish_btn = popupView.findViewById(R.id.publishPlan);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText workoutNameInput = popupView.findViewById(R.id.editWorkoutName);
                EditText notesInput = popupView.findViewById(R.id.notes);
                EditText setsInput = popupView.findViewById(R.id.sets);
                EditText repsInput = popupView.findViewById(R.id.reps);
                EditText timeInput = popupView.findViewById(R.id.time);
                EditText caloriesInput = popupView.findViewById(R.id.calories);

                String workoutName = workoutNameInput.getText().toString();
                String notes = notesInput.getText().toString();
                String sets = setsInput.getText().toString();
                String reps = repsInput.getText().toString();
                String time = timeInput.getText().toString();
                String calories = caloriesInput.getText().toString();

                if (TextUtils.isEmpty(workoutName) || TextUtils.isEmpty(sets) || TextUtils.isEmpty(reps)
                        || TextUtils.isEmpty(calories)) {
                    Toast.makeText(WorkoutPlans.this,
                            "Please fill in all necessary fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int setsInt = Integer.parseInt(sets);
                int repsInt = Integer.parseInt(reps);
                int calsInt = Integer.parseInt(calories);

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String userId = currentUser.getUid();

                WorkoutPlan workout = new WorkoutPlan(userId, workoutName, notes, setsInt, repsInt, time, calsInt);
                // Save data to Firebase
                workoutViewModel.addWorkout(userId, workout);
                
            }
        });
    }
}