package com.example.healthtrack.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtrack.model.Workout;
import com.example.healthtrack.model.WorkoutDatabaseRepository;
import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.model.WorkoutPlanDatabaseRepository;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class WorkoutViewModel extends ViewModel {
    private WorkoutDatabaseRepository workoutDatabaseRepository;
    private WorkoutPlanDatabaseRepository workoutPlanDatabaseRepository;
    private MutableLiveData<String> addWorkoutResult;

    public WorkoutViewModel() {
        workoutDatabaseRepository = new WorkoutDatabaseRepository();
        workoutPlanDatabaseRepository = new WorkoutPlanDatabaseRepository();
        addWorkoutResult = new MutableLiveData<>();
    }


    public void addWorkout(String userId, Workout workout) {
        workoutDatabaseRepository.addWorkout(userId, workout,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            addWorkoutResult.setValue("Database Error");
                        } else {
                            addWorkoutResult.setValue("Workout Added Successfully");
                        }
                    }
                });
    }

    public void addWorkoutPlan(String userId, WorkoutPlan workoutPlan) {
        workoutPlanDatabaseRepository.addWorkoutPlan(userId, workoutPlan,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            addWorkoutResult.setValue("Database Error");
                        } else {
                            addWorkoutResult.setValue("Workout Added Successfully");
                        }
                    }
                });
    }

    public MutableLiveData<String> getAddWorkoutResult() {
        return addWorkoutResult;
    }
}
