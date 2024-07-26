package com.example.healthtrack.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.healthtrack.model.NameSortStrategy;
import com.example.healthtrack.model.RefreshSortStrategy;
import com.example.healthtrack.model.SortingStrategy;
import com.example.healthtrack.model.WorkoutPlan;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtrack.R;
import com.example.healthtrack.viewModel.WorkoutPlanViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddWorkoutCommunity extends AppCompatActivity {
    private Dialog dialog;
    private Button btnDialogAdd;
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private ArrayList<WorkoutPlan> planList;
    private ArrayList<WorkoutPlan> unfilteredList;
    private static ArrayList<WorkoutPlan> returnList;
    private WorkoutPlanViewModel workoutPlanViewModel;
    private SearchView searchBar;
    private SortingStrategy search = new NameSortStrategy();
    private String lastQuery = "";
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_plans);


        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.planList);
        database = FirebaseDatabase.getInstance().getReference("workout plans");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        workoutPlanViewModel = new ViewModelProvider(this).get(WorkoutPlanViewModel.class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planList = new ArrayList<>();
        unfilteredList = new ArrayList<>();
        returnList = new ArrayList<>();
        CommunityWorkoutAdapter communityWorkoutAdapter = new CommunityWorkoutAdapter(this,
                planList, returnList);
        recyclerView.setAdapter(communityWorkoutAdapter);
        searchBar = findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    lastQuery = newText;
                    NameSortStrategy newSearch = new NameSortStrategy();
                    newSearch.setName(newText);
                    search = newSearch;
                    planList = searchList(unfilteredList);
                    recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                            planList, returnList));
                    return true;
                } else {
                    planList = refreshList(unfilteredList);
                    recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                            planList, returnList));
                    return true;
                }
            }
        });

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {
                HashMap<String, HashMap<String, Object>> data =
                        (HashMap<String, HashMap<String, Object>>) snapshot.getValue();
                ArrayList<HashMap<String, Object>> dataList = new ArrayList(data.values());
                for (int i = 0; i < dataList.size(); i++) {
                    HashMap<String, Object> dataMap = dataList.get(i);
                    String userId = (String) dataMap.get("userId");
                    String name = (String) dataMap.get("name");
                    Integer calsPerSet =
                            Integer.parseInt(dataMap.get("caloriesPerSet").toString());
                    Integer repsPerSet = Integer.parseInt(dataMap.get("repsPerSet").toString());
                    Integer sets = Integer.parseInt(dataMap.get("sets").toString());
                    String notes = (String) dataMap.get("notes");
                    Integer time = Integer.parseInt(dataMap.get("time").toString());

                    WorkoutPlan plan = new WorkoutPlan(userId, name, calsPerSet, sets,
                            repsPerSet, time, notes);
                    planList.add(plan);
                    unfilteredList.add(plan);
                    recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                            planList, returnList));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       @Nullable String previousChildName) {
                HashMap<String, HashMap<String, Object>> data =
                        (HashMap<String, HashMap<String, Object>>) snapshot.getValue();
                ArrayList<HashMap<String, Object>> dataList = new ArrayList(data.values());
                for (int i = 0; i < dataList.size(); i++) {
                    HashMap<String, Object> dataMap = dataList.get(i);
                    String userId = (String) dataMap.get("userId");
                    String name = (String) dataMap.get("name");
                    Integer calsPerSet =
                            Integer.parseInt(dataMap.get("caloriesPerSet").toString());
                    Integer repsPerSet = Integer.parseInt(dataMap.get("repsPerSet").toString());
                    Integer sets = Integer.parseInt(dataMap.get("sets").toString());
                    String notes = (String) dataMap.get("notes");
                    Integer time = Integer.parseInt(dataMap.get("time").toString());

                    WorkoutPlan plan = new WorkoutPlan(userId, name, calsPerSet, sets,
                            repsPerSet, time, notes);
                    if (!planList.contains(plan)) {
                        planList.add(plan);
                        unfilteredList.add(plan);
                    }
                    recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                            planList, returnList));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                        planList, returnList));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {
                recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                        planList, returnList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                        planList, returnList));
            }
        });

        dialog = new Dialog(AddWorkoutCommunity.this);
        dialog.setContentView(R.layout.workout_plan_popout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        btnDialogAdd = dialog.findViewById(R.id.publishPlan);

        btnDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText workoutNameInput = dialog.findViewById(R.id.editWorkoutName);
                EditText notesInput = dialog.findViewById(R.id.notes);
                EditText setsInput = dialog.findViewById(R.id.sets);
                EditText repsInput = dialog.findViewById(R.id.reps);
                EditText timeInput = dialog.findViewById(R.id.time);
                EditText caloriesInput = dialog.findViewById(R.id.calories);

                String workoutName = workoutNameInput.getText().toString();
                String notes = notesInput.getText().toString();
                String sets = setsInput.getText().toString();
                String reps = repsInput.getText().toString();
                String time = timeInput.getText().toString();
                String calories = caloriesInput.getText().toString();

                if (TextUtils.isEmpty(workoutName) || TextUtils.isEmpty(sets)
                        || TextUtils.isEmpty(reps) || TextUtils.isEmpty(calories)
                        || TextUtils.isEmpty(time)) {
                    Toast.makeText(AddWorkoutCommunity.this,
                            "Please fill in all necessary fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int setsInt = Integer.parseInt(sets);
                int repsInt = Integer.parseInt(reps);
                int calsInt = Integer.parseInt(calories);
                int timeInt = Integer.parseInt(time);

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String userId = currentUser.getUid();

                WorkoutPlan workoutPlan = new WorkoutPlan(userId, workoutName, calsInt, setsInt,
                        repsInt, timeInt, notes);

                if (planList.contains(workoutPlan)) {
                    Toast.makeText(AddWorkoutCommunity.this,
                            "Duplicate plans not allowed", Toast.LENGTH_LONG).show();
                    return;
                }

                // Save data to Firebase
                workoutPlanViewModel.addWorkoutPlan(userId, workoutPlan);

                Toast.makeText(AddWorkoutCommunity.this,
                        "Plan created; Click the respective item button to add",
                        Toast.LENGTH_SHORT).show();

                workoutNameInput.setText("");
                notesInput.setText("");
                setsInput.setText("");
                repsInput.setText("");
                timeInput.setText("");
                caloriesInput.setText("");

                dialog.dismiss();
            }
        });

        Button backButton = findViewById(R.id.btn_plans_back);
        backButton.setText("Back");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWorkoutCommunity.this, CommunityScreen.class);
                startActivity(intent);
                finish();
            }
        });

        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(AddWorkoutCommunity.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(AddWorkoutCommunity.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(AddWorkoutCommunity.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(AddWorkoutCommunity.this, CommunityScreen.class);
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

        ImageButton refreshButton = findViewById(R.id.buttonRefresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planList = refreshList(unfilteredList);
                recyclerView.setAdapter(new CommunityWorkoutAdapter(AddWorkoutCommunity.this,
                        planList, returnList));
            }
        });
    }


    // sort methods to call
    public ArrayList<WorkoutPlan> refreshList(ArrayList<WorkoutPlan> list) {
        SortingStrategy refresh = new RefreshSortStrategy();
        return workoutPlanViewModel.filter(refresh, list);
    }

    public ArrayList<WorkoutPlan> searchList(ArrayList<WorkoutPlan> list) {
        return workoutPlanViewModel.filter(search, list);
    }

    public static ArrayList<WorkoutPlan> getReturnList() {
        return returnList;
    }
}