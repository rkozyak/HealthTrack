package com.example.healthtrack.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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
import com.example.healthtrack.model.Workout;
import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.viewModel.WorkoutPlanViewModel;
import com.example.healthtrack.viewModel.WorkoutViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkoutPlans extends AppCompatActivity {
    private Dialog dialog;
    private Button btnDialogAdd;
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private WorkoutPlanAdapter workoutPlanAdapter;
    private ArrayList<WorkoutPlan> planList;
    private WorkoutPlanViewModel workoutPlanViewModel;
    private SortingStrategy strategy;
    private SearchView searchBar;
    private Handler searchHandler;
    private Runnable searchRunnable;
    SortingStrategy search = new NameSortStrategy();
    private String lastQuery = "";
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_plans);

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.planList);
        database = FirebaseDatabase.getInstance().getReference("workout plans");
        workoutPlanViewModel = new ViewModelProvider(this).get(WorkoutPlanViewModel.class);
        strategy = new RefreshSortStrategy();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRunnable = new Runnable() {
            @Override
            public void run() {
                String currentQuery = searchBar.getQuery().toString();
                if (!currentQuery.equals(lastQuery)) {
                    lastQuery = currentQuery;
                    NameSortStrategy newSearch = new NameSortStrategy();
                    newSearch.setName(currentQuery);
                    search = newSearch;
                    planList = searchList(planList);
                }
            }
        };
        HandlerThread searchThread = new HandlerThread("SearchViewHandlerThread");
        searchThread.start();
        searchHandler = new Handler(searchThread.getLooper());
        searchHandler.post(searchRunnable);


        planList = new ArrayList<>();
        workoutPlanAdapter = new WorkoutPlanAdapter(this, planList);
        recyclerView.setAdapter(workoutPlanAdapter);

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HashMap<String, HashMap<String, Object>> data =
                        (HashMap<String, HashMap<String, Object>>) snapshot.getValue();
                ArrayList<HashMap<String, Object>> dataList = new ArrayList(data.values());
                for(int i = 0; i < dataList.size(); i++) {
                    HashMap<String, Object> dataMap = dataList.get(i);
                    String userId = (String) dataMap.get("userId");
                    String name = (String) dataMap.get("name");
                    System.out.println(dataMap.get("time"));
                    Integer calsPerSet =
                            Integer.parseInt(dataMap.get("caloriesPerSet").toString());
                    Integer repsPerSet = Integer.parseInt(dataMap.get("repsPerSet").toString());
                    Integer sets = Integer.parseInt(dataMap.get("sets").toString());
                    String notes = (String) dataMap.get("notes");
                    Integer time = Integer.parseInt(dataMap.get("time").toString());

                    WorkoutPlan plan = new WorkoutPlan(userId, name, calsPerSet, sets,
                            repsPerSet, time, notes);
                    planList.add(plan);
                    workoutPlanAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                workoutPlanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                workoutPlanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                workoutPlanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                workoutPlanAdapter.notifyDataSetChanged();
            }
        });

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

                if (TextUtils.isEmpty(workoutName) || TextUtils.isEmpty(sets) || TextUtils.isEmpty(reps)
                        || TextUtils.isEmpty(calories) || TextUtils.isEmpty(time)) {
                    Toast.makeText(WorkoutPlans.this,
                            "Please fill in all necessary fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int setsInt = Integer.parseInt(sets);
                int repsInt = Integer.parseInt(reps);
                int calsInt = Integer.parseInt(calories);
                int timeInt = Integer.parseInt(time);

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String userId = currentUser.getUid();

                WorkoutPlan workoutPlan = new WorkoutPlan(userId, workoutName, calsInt, setsInt, repsInt, timeInt, notes);
                // Save data to Firebase
                workoutPlanViewModel.addWorkoutPlan(userId, workoutPlan);

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

        ImageButton refreshButton = findViewById(R.id.buttonRefresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planList = refreshList(planList);
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
}