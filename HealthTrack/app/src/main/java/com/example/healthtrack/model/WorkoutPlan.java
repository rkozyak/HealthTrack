package com.example.healthtrack.model;

import java.util.Date;

public class WorkoutPlan {
    private String userId;

    private String workoutName;
    private int sets;
    private int reps;
    private String time;
    private int calories;

    private String notes;

    public WorkoutPlan(String userId, String workoutName, String notes, int sets,
                   int reps, String time, int calories) {
        this.userId = userId;
        this.workoutName = workoutName;
        this.calories = calories;
        this.sets = sets;
        this.reps = reps;
        this.notes = notes;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public String getTime() {
        return time;
    }

    public int getCalories() {
        return calories;
    }

    public String getNotes() {
        return notes;
    }
}
