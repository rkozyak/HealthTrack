package com.example.healthtrack.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.model.WorkoutPlanDatabaseRepository;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class WorkoutPlanViewModel extends ViewModel {
    private WorkoutPlanDatabaseRepository workoutPlanDatabaseRepository;
    private MutableLiveData<String> addWorkoutPlanResult;

    public WorkoutPlanViewModel() {
        workoutPlanDatabaseRepository = new WorkoutPlanDatabaseRepository();
        addWorkoutPlanResult = new MutableLiveData<>();
    }


    public void addWorkoutPlan(String userId, WorkoutPlan workoutPlan) {
        workoutPlanDatabaseRepository.addWorkoutPlan(userId, workoutPlan,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            addWorkoutPlanResult.setValue("Database Error");
                        } else {
                            addWorkoutPlanResult.setValue("Workout Added Successfully");
                        }
                    }
                });
    }

    public MutableLiveData<String> getAddWorkoutResult() {
        return addWorkoutPlanResult;
    }
}
