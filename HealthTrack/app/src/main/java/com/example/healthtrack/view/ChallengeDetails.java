package com.example.healthtrack.view;

import com.example.healthtrack.model.ChallengeDatabase;
import com.example.healthtrack.model.Workout;
import com.example.healthtrack.model.WorkoutPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtrack.R;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChallengeDetails extends AppCompatActivity {
    private DatabaseReference database;
    private FirebaseAuth mAuth;
    private String challenger;
    private String name;
    private String deadline;
    private String desc;
    private ArrayList<Workout> workout;
    private ArrayList<String> participants;
    private ArrayList<String> completed;
    private String challengeId;
    private RecyclerView workoutView;
    private ArrayList<WorkoutPlan> workoutPlanArrayList;
    private RecyclerView participantView;
    private RecyclerView completedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_challenge_details);

        workoutView = findViewById(R.id.planList);
        workoutView.setHasFixedSize(true);
        workoutView.setLayoutManager(new LinearLayoutManager(this));
        participantView = findViewById(R.id.participantList);
        participantView.setHasFixedSize(true);
        participantView.setLayoutManager(new LinearLayoutManager(this));
        completedView = findViewById(R.id.completedList);
        completedView.setHasFixedSize(true);
        completedView.setLayoutManager(new LinearLayoutManager(this));
        TextView challengerNameView = findViewById(R.id.nameView);
        TextView nameView = findViewById(R.id.name);
        TextView deadlineView = findViewById(R.id.deadlineView);
        TextView descriptionView = findViewById(R.id.descView);
        workoutPlanArrayList = new ArrayList<>();
        participants = new ArrayList<>();
        UserAdapter participantAdapter = new UserAdapter(this, participants);
        participantView.setAdapter(participantAdapter);
        completed = new ArrayList<>();
        UserAdapter completedAdapter = new UserAdapter(this, completed);
        database = ChallengeDatabase.getInstance().getDatabaseReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        challengeId = getIntent().getStringExtra("challengeId");
        DatabaseReference database = ChallengeDatabase.getInstance().getDatabaseReference().child(challengeId);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
                challenger = data.get("userId").toString();
                deadline = data.get("deadlineMonth").toString() + "/" + data.get("deadlineDay").toString()
                        + "/" + data.get("deadlineYear").toString();
                desc = data.get("notes").toString();
                name = data.get("name").toString();
                challengerNameView.setText(challenger);
                nameView.setText(name);
                deadlineView.setText(deadline);
                descriptionView.setText(desc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals("workoutPlans")) {
                    ArrayList<HashMap<String, Object>> data =
                            (ArrayList<HashMap<String, Object>>) snapshot.getValue();

                    for (int i = 0; i < data.size(); i++) {
                        HashMap<String, Object> dataMap = data.get(i);
                        String userId = (String) dataMap.get("userId");
                        String name = (String) dataMap.get("name");
                        int calsPerSet = Integer.parseInt(dataMap.get("caloriesPerSet").toString());
                        int repsPerSet = Integer.parseInt(dataMap.get("repsPerSet").toString());
                        int sets = Integer.parseInt(dataMap.get("sets").toString());
                        String notes = (String) dataMap.get("notes");
                        int time = Integer.parseInt(dataMap.get("time").toString());
                        WorkoutPlan workoutPlan = new WorkoutPlan(userId, name, calsPerSet, sets, repsPerSet, time, notes);

                        workoutPlanArrayList.add(workoutPlan);
                    }
                } else if (snapshot.getKey().equals("participants")) {
                    HashMap<String, String> participantData = (HashMap<String, String>) snapshot.getValue();
                    ArrayList<String> participantArray = new ArrayList<>(participantData.values());

                    for (int i = 0; i < participantArray.size(); i++) {
                        if (!participants.contains(participantArray.get(i))) {
                            participants.add(participantArray.get(i));
                        }
                    }
                } else if (snapshot.getKey().equals("completed")) {
                    HashMap<String, String> completedData = (HashMap<String, String>) snapshot.getValue();
                    ArrayList<String> completedArray = new ArrayList<>(completedData.values());
                    for (int i = 0; i < completedArray.size(); i++) {
                        if (!completed.contains(completedArray.get(i))) {
                            completed.add(completedArray.get(i));
                        }
                    }
                }
                participantView.setAdapter(new UserAdapter(ChallengeDetails.this, participants));
                completedView.setAdapter(new UserAdapter(ChallengeDetails.this, completed));
                workoutView.setAdapter(new WorkoutPlanAdapter(ChallengeDetails.this, workoutPlanArrayList));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals("participants")) {
                    HashMap<String, String> participantData = (HashMap<String, String>) snapshot.getValue();
                    ArrayList<String> participantArray = new ArrayList<>(participantData.values());

                    for (int i = 0; i < participantArray.size(); i++) {
                        if (!participants.contains(participantArray.get(i))) {
                            participants.add(participantArray.get(i));
                        }
                    }
                } else if (snapshot.getKey().equals("completed")) {
                    HashMap<String, String> completedData = (HashMap<String, String>) snapshot.getValue();
                    ArrayList<String> completedArray = new ArrayList<>(completedData.values());
                    for (int i = 0; i < completedArray.size(); i++) {
                        if (!completed.contains(completedArray.get(i))) {
                            completed.add(completedArray.get(i));
                        }
                    }
                }
                participantView.setAdapter(new UserAdapter(ChallengeDetails.this, participants));
                completedView.setAdapter(new UserAdapter(ChallengeDetails.this, completed));
                workoutView.setAdapter(new WorkoutPlanAdapter(ChallengeDetails.this, workoutPlanArrayList));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                workoutView.setAdapter(new WorkoutPlanAdapter(ChallengeDetails.this, workoutPlanArrayList));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                workoutView.setAdapter(new WorkoutPlanAdapter(ChallengeDetails.this, workoutPlanArrayList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                workoutView.setAdapter(new WorkoutPlanAdapter(ChallengeDetails.this, workoutPlanArrayList));
            }
        });


        Button addChallengeButton = findViewById(R.id.addChallengeButton);
        addChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (completed.contains(userId)) {
                    Toast.makeText(ChallengeDetails.this,
                            "Already completed", Toast.LENGTH_SHORT).show();
                    return;
                } else if (participants.contains(userId)) {
                    Toast.makeText(ChallengeDetails.this,
                            "Already participating", Toast.LENGTH_SHORT).show();
                } else {
                    database.child("participants").child(userId).setValue(userId);
                }
            }
        });

        Button completeChallengeButton = findViewById(R.id.completeChallengeButton);
        completeChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (participants.contains(userId)) {
                    database.child("participants").child(userId).removeValue();
                    database.child("completed").child(userId).setValue(userId);
                    participants.remove(userId);
                    completed.add(userId);
                } else if (completed.contains(userId)) {
                    Toast.makeText(ChallengeDetails.this,
                            "Already completed", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(ChallengeDetails.this,
                            "Add challenge before completing", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        Button backButton = findViewById(R.id.btn_community_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeDetails.this, CommunityScreen.class);
                startActivity(intent);
            }
        });

        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(ChallengeDetails.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(ChallengeDetails.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(ChallengeDetails.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(ChallengeDetails.this, CommunityScreen.class);
                startActivity(intent);
            }
        });
    }


}
