package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtrack.R;
import com.example.healthtrack.model.WorkoutDatabaseRepository;
import com.example.healthtrack.viewModel.WorkoutViewModel;
import com.example.healthtrack.model.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WorkoutTracker extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private WorkoutDatabaseRepository workoutDatabaseRepository;
    private WorkoutViewModel workoutViewModel;
    private ListView workoutListView;
    ArrayList<Workout> workoutArrayList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_tracker);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = currentUser.getUid();
        workoutDatabaseRepository = new WorkoutDatabaseRepository();
        mRef = workoutDatabaseRepository.getWorkoutsReference(userId);
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        workoutArrayList = new ArrayList<>();
        WorkoutAdapter myArrayAdapter = new WorkoutAdapter(this, workoutArrayList);
        workoutListView = (ListView) findViewById(R.id.listView);
        workoutListView.setAdapter(myArrayAdapter);
        workoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showItemWindow(position);
            }
        });

        mRef.orderByChild("date").limitToLast(5).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (workoutArrayList.size() < 5) {
                    HashMap<String, Object> workoutData = (HashMap<String, Object>) snapshot.getValue();
                    HashMap<String, Object> dateMap = (HashMap<String, Object>) workoutData.get("date");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, Integer.parseInt(dateMap.get("year").toString()) + 1900);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateMap.get("date").toString()));
                    calendar.set(Calendar.MONTH, Integer.parseInt(dateMap.get("month").toString()));

                    Date date = calendar.getTime();
                    String UID = (String) workoutData.get("userId");
                    String name = (String) workoutData.get("name");
                    String notes = (String) workoutData.get("notes");
                    Integer calsPerSet = Integer.parseInt(workoutData.get("caloriesPerSet").toString());
                    Integer repsPerSet = Integer.parseInt(workoutData.get("repsPerSet").toString());
                    Integer sets = Integer.parseInt(workoutData.get("sets").toString());

                    Workout workout = new Workout(UID, name, calsPerSet,
                            sets, repsPerSet, notes, date);
                    workoutArrayList.add(workout);
                    myArrayAdapter.notifyDataSetChanged();
                } else {
                    workoutArrayList.remove(0);

                    HashMap<String, Object> workoutData = (HashMap<String, Object>) snapshot.getValue();
                    HashMap<String, Object> dateMap = (HashMap<String, Object>) workoutData.get("date");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, Integer.parseInt(dateMap.get("year").toString()) + 1900);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateMap.get("date").toString()));
                    calendar.set(Calendar.MONTH, Integer.parseInt(dateMap.get("month").toString()));

                    Date date = calendar.getTime();
                    String UID = (String) workoutData.get("userId");
                    String name = (String) workoutData.get("name");
                    String notes = (String) workoutData.get("notes");
                    Integer calsPerSet = Integer.parseInt(workoutData.get("caloriesPerSet").toString());
                    Integer repsPerSet = Integer.parseInt(workoutData.get("repsPerSet").toString());
                    Integer sets = Integer.parseInt(workoutData.get("sets").toString());

                    Workout workout = new Workout(UID, name, calsPerSet,
                            sets, repsPerSet, notes, date);
                    workoutArrayList.add(workout);
                    myArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });


        Button backButton = findViewById(R.id.btn_tracker_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkoutTracker.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Calorie Button
        Button openCalorieButton = findViewById(R.id.caloriesButton);
        openCalorieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, CalorieTracking.class);
                startActivity(intent);
            }
        });

        // Tracker Button
        Button openTrackerButton = findViewById(R.id.trackerButton);
        openTrackerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, WorkoutTracker.class);
                startActivity(intent);
            }
        });

        // Workouts Button
        Button openWorkoutsButton = findViewById(R.id.workoutsButton);
        openWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, WorkoutPlans.class);
                startActivity(intent);
            }
        });

        // Community Button
        Button openCommunityButton = findViewById(R.id.communityButton);
        openCommunityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent  = new Intent(WorkoutTracker.this, CommunityScreen.class);
                startActivity(intent);
            }
        });

        Button addWorkoutButton = findViewById(R.id.addWorkout);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopupWindow();
            }
        });


    }

    private void showPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.workout_popout, null);

        // Create the PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // Lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(getWindow().findViewById(android.R.id.content), Gravity.CENTER, 0, 0);

        // Set up the submit button inside the popup
        Button submitButton = popupView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from EditTexts
                EditText editName = popupView.findViewById(R.id.editName);
                EditText editSets = popupView.findViewById(R.id.editSets);
                EditText editReps = popupView.findViewById(R.id.editSetReps);
                EditText editCals = popupView.findViewById(R.id.editCaloriesPerSet);
                EditText editNotes = popupView.findViewById(R.id.editNotes);

                String name = editName.getText().toString();
                String sets = editSets.getText().toString();
                String reps = editReps.getText().toString();
                String cals = editCals.getText().toString();
                String notes = editNotes.getText().toString();


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(sets) || TextUtils.isEmpty(reps)
                        || TextUtils.isEmpty(cals)) {
                    Toast.makeText(WorkoutTracker.this, "Please fill in all necessary fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int setsInt = Integer.parseInt(sets);
                int repsInt = Integer.parseInt(reps);
                int calsInt = Integer.parseInt(cals);

                FirebaseUser currentUser = mAuth.getCurrentUser();

                String userId = currentUser.getUid();

                Workout workout = new Workout(userId, name, setsInt, repsInt, calsInt, notes);
                // Save data to Firebase
                workoutViewModel.addWorkout(userId, workout);

                // Dismiss the popup window
                popupWindow.dismiss();
            }
        });
    }

    private void showItemWindow(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.workout_item_popout, null);

        // Create the PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // Lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(getWindow().findViewById(android.R.id.content), Gravity.CENTER, 0, 0);

        TextView name = popupView.findViewById(R.id.textName);
        TextView sets = popupView.findViewById(R.id.textSets);
        TextView reps = popupView.findViewById(R.id.textReps);
        TextView cals = popupView.findViewById(R.id.textCaloriesPerSet);
        TextView notes = popupView.findViewById(R.id.textNotes);

        Workout currentWorkout = workoutArrayList.get(workoutArrayList.size() - 5 + position);

        name.setText(currentWorkout.getName());
        sets.setText(currentWorkout.getSets());
        reps.setText(currentWorkout.getRepsPerSet());
        cals.setText(currentWorkout.getCaloriesPerSet());
        notes.setText(currentWorkout.getNotes());
    }



}