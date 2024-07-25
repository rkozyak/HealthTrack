package com.example.healthtrack.model;

import java.util.ArrayList;
import java.util.Date;

public class Challenge {
    private String name;
    private String description;
    private Date deadline;
    private ArrayList<WorkoutPlan> workoutPlans;

    public Challenge(String name, String description, Date deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.workoutPlans = new ArrayList<WorkoutPlan>();
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<WorkoutPlan> getWorkoutPlans() {
        return workoutPlans;
    }

    public void addWorkoutPlan(WorkoutPlan workoutPlan) {
        workoutPlans.add(workoutPlan);
    }
}
