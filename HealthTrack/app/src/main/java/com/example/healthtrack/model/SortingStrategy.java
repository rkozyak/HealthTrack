package com.example.healthtrack.model;

import java.util.ArrayList;

public interface SortingStrategy {
    public ArrayList<WorkoutPlan> sort(ArrayList<WorkoutPlan> list);
}
