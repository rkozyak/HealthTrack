package com.example.healthtrack.model;

import java.util.Date;

public class WorkoutPlan {
    private String userId;
    private String name;
    private Integer caloriesPerSet;
    private Integer sets;
    private Integer repsPerSet;
    private Integer time;
    private String notes;
    private Date date;

    public WorkoutPlan() {
    }

    public WorkoutPlan(String userId, String name, Integer caloriesPerSet,
                       Integer sets, Integer repsPerSet, Integer time, String notes) {
        this.userId = userId;
        this.name = name;
        this.caloriesPerSet = caloriesPerSet;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.time = time;
        this.notes = notes;
        date = new Date();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getCaloriesPerSet() {
        return caloriesPerSet;
    }

    public int getSets() {
        return sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public String getNotes() {
        return notes;
    }

    public Date getDate() {
        return date;
    }

    public String toString() {
        return name;
    }
}
