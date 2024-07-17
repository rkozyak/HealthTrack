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

    public Integer getCaloriesPerSet() {
        return caloriesPerSet;
    }

    public Integer getSets() {
        return sets;
    }

    public Integer getTime() {
        return time;
    }

    public Integer getRepsPerSet() {
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

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        WorkoutPlan temp = (WorkoutPlan) o;
        return name.equals(temp.name) && caloriesPerSet == temp.caloriesPerSet && sets == temp.sets
                && repsPerSet == temp.repsPerSet && notes.equals(temp.notes) && time == temp.time;
    }
}
