package com.example.healthtrack.model;

public class Workout {
    private String userId;
    private String name;
    private Integer caloriesPerSet;
    private Integer sets;
    private Integer repsPerSet;
    private String notes;

    public Workout(String userId, String name, Integer caloriesPerSet,
                   Integer sets, Integer repsPerSet, String notes) {
        this.userId = userId;
        this.name = name;
        this.caloriesPerSet = caloriesPerSet;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.notes = notes;
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
}
