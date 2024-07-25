package com.example.healthtrack.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthtrack.model.NameSortStrategy;
import com.example.healthtrack.model.RefreshSortStrategy;
import com.example.healthtrack.model.SortingStrategy;
import com.example.healthtrack.model.WorkoutDatabaseRepository;
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

public class WorkoutPlans extends AppCompatActivity {
    private Dialog dialog;
    private Button btnDialogAdd;
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private DatabaseReference workoutDatabase;
    private ArrayList<WorkoutPlan> planList;
    private ArrayList<WorkoutPlan> unfilteredList;
    private ArrayList<String> workoutNameArrayList;
    private WorkoutPlanViewModel workoutPlanViewModel;
    private SearchView searchBar;
    private SortingStrategy search = new NameSortStrategy();
    private String lastQuery = "";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_plans);
        initializeFields();
        setupRecyclerView();
        setupSearchBar();
        setupChildEventListeners();
        setupDialog();
        setupButtons();
    }

    private void initializeFields() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("workout plans");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        workoutDatabase = new WorkoutDatabaseRepository().getWorkoutsReference(userId);
        workoutPlanViewModel = new ViewModelProvider(this).get(WorkoutPlanViewModel.class);
        planList = new ArrayList<>();
        unfilteredList = new ArrayList<>();
        workoutNameArrayList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.planList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WorkoutPlanAdapter workoutPlanAdapter = new WorkoutPlanAdapter(this, planList, workoutNameArrayList);
        recyclerView.setAdapter(workoutPlanAdapter);
    }

    private void setupSearchBar() {
        searchBar = findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handleSearchQueryChange(newText);
                return true;
            }
        });
    }

    private void handleSearchQueryChange(String newText) {
        if (!newText.isEmpty()) {
            lastQuery = newText;
            NameSortStrategy newSearch = new NameSortStrategy();
            newSearch.setName(newText);
            search = newSearch;
            planList = searchList(unfilteredList);
        } else {
            planList = refreshList(unfilteredList);
        }
        recyclerView.setAdapter(new WorkoutPlanAdapter(WorkoutPlans.this, planList, workoutNameArrayList));
    }

    private void setupChildEventListeners() {
        setupWorkoutDatabaseListener();
        setupDatabaseListener();
    }

    private void setupWorkoutDatabaseListener() {
        workoutDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HashMap<String, Object> workoutData = (HashMap<String, Object>) snapshot.getValue();
                String name = (String) workoutData.get("name");
                workoutNameArrayList.add(name);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupDatabaseListener() {
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                handleDatabaseChildEvent(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                handleDatabaseChildEvent(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                recyclerView.setAdapter(new WorkoutPlanAdapter(WorkoutPlans.this, planList, workoutNameArrayList));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                recyclerView.setAdapter(new WorkoutPlanAdapter(WorkoutPlans.this, planList, workoutNameArrayList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                recyclerView.setAdapter(new WorkoutPlanAdapter(WorkoutPlans.this, planList, workoutNameArrayList));
            }
        });
    }

    private void handleDatabaseChildEvent(@NonNull DataSnapshot snapshot) {
        HashMap<String, HashMap<String, Object>> data = (HashMap<String, HashMap<String, Object>>) snapshot.getValue();
        ArrayList<HashMap<String, Object>> dataList = new ArrayList<>(data.values());
        for (HashMap<String, Object> dataMap : dataList) {
            WorkoutPlan plan = createWorkoutPlanFromDataMap(dataMap);
            if (!planList.contains(plan)) {
                planList.add(plan);
                unfilteredList.add(plan);
            }
            recyclerView.setAdapter(new WorkoutPlanAdapter(WorkoutPlans.this, planList, workoutNameArrayList));
        }
    }

    private WorkoutPlan createWorkoutPlanFromDataMap(HashMap<String, Object> dataMap) {
        String userId = (String) dataMap.get("userId");
        String name = (String) dataMap.get("name");
        int calsPerSet = Integer.parseInt(dataMap.get("caloriesPerSet").toString());
        int repsPerSet = Integer.parseInt(dataMap.get("repsPerSet").toString());
        int sets = Integer.parseInt(dataMap.get("sets").toString());
        String notes = (String) dataMap.get("notes");
        int time = Integer.parseInt(dataMap.get("time").toString());

        return new WorkoutPlan(userId, name, calsPerSet, sets, repsPerSet, time, notes);
    }

    private void setupDialog() {
        dialog = new Dialog(WorkoutPlans.this);
        dialog.setContentView(R.layout.workout_plan_popout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        btnDialogAdd = dialog.findViewById(R.id.publishPlan);
        btnDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDialogAddButtonClick();
            }
        });
    }

    private void handleDialogAddButtonClick() {
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

        if (areInputsValid(workoutName, sets, reps, calories, time)) {
            createAndSaveWorkoutPlan(workoutName, notes, sets, reps, time, calories);
            clearDialogInputs(workoutNameInput, notesInput, setsInput, repsInput, timeInput, caloriesInput);
            dialog.dismiss();
        } else {
            Toast.makeText(WorkoutPlans.this, "Please fill in all necessary fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean areInputsValid(String workoutName, String sets, String reps, String calories, String time) {
        return !TextUtils.isEmpty(workoutName) && !TextUtils.isEmpty(sets) && !TextUtils.isEmpty(reps)
                && !TextUtils.isEmpty(calories) && !TextUtils.isEmpty(time);
    }

    private void createAndSaveWorkoutPlan(String workoutName, String notes, String sets, String reps, String time, String calories) {
        int setsInt = Integer.parseInt(sets);
        int repsInt = Integer.parseInt(reps);
        int calsInt = Integer.parseInt(calories);
        int timeInt = Integer.parseInt(time);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        WorkoutPlan workoutPlan = new WorkoutPlan(userId, workoutName, calsInt, setsInt, repsInt, timeInt, notes);

        if (planList.contains(workoutPlan)) {
            Toast.makeText(WorkoutPlans.this, "Duplicate plans not allowed", Toast.LENGTH_LONG).show();
        } else {
            workoutPlanViewModel.addWorkoutPlan(userId, workoutPlan);
        }
    }

    private void clearDialogInputs(EditText... inputs) {
        for (EditText input : inputs) {
            input.setText("");
        }
    }

    private void setupButtons() {
        findViewById(R.id.btn_plans_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutPlans.this, HomeActivity.class));
            }
        });
        setupNavigationButtons();
        setupCreatePlanButton();
        setupRefreshButton();
    }

    private void setupNavigationButtons() {
        findViewById(R.id.caloriesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutPlans.this, CalorieTracking.class));
            }
        });
        findViewById(R.id.trackerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutPlans.this, WorkoutTracker.class));
            }
        });
        findViewById(R.id.workoutsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutPlans.this, WorkoutPlans.class));
            }
        });
        findViewById(R.id.communityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutPlans.this, CommunityScreen.class));
            }
        });
    }

    private void setupCreatePlanButton() {
        findViewById(R.id.plan_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void setupRefreshButton() {
        findViewById(R.id.buttonRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planList = refreshList(unfilteredList);
                recyclerView.setAdapter(new WorkoutPlanAdapter(WorkoutPlans.this, planList, workoutNameArrayList));
            }
        });
    }

    public ArrayList<WorkoutPlan> refreshList(ArrayList<WorkoutPlan> list) {
        SortingStrategy refresh = new RefreshSortStrategy();
        return workoutPlanViewModel.filter(refresh, list);
    }

    public ArrayList<WorkoutPlan> searchList(ArrayList<WorkoutPlan> list) {
        return workoutPlanViewModel.filter(search, list);
    }
}
