package com.example.healthtrack.model;

import java.util.Date;

public class Workout implements WorkoutComponent {
    private String userId;
    private String name;
    private Integer caloriesPerSet;
    private Integer sets;
    private Integer repsPerSet;
    private String notes;
    private Date date;

    public Workout(String userId, String name, Integer caloriesPerSet,
                   Integer sets, Integer repsPerSet, String notes) {
        this.userId = userId;
        this.name = name;
        this.caloriesPerSet = caloriesPerSet;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.notes = notes;
        date = new Date();
    }

    public Workout(String userId, String name, Integer caloriesPerSet,
                   Integer sets, Integer repsPerSet, String notes, Date date) {
        this.userId = userId;
        this.name = name;
        this.caloriesPerSet = caloriesPerSet;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.notes = notes;
        this.date = date;
    }

    @Override
    public void perform() {
        System.out.println("Performing workout: " + name);
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
