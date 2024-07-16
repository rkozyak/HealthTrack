package com.example.healthtrack.model;

import com.example.healthtrack.model.SortingStrategy;

import java.util.ArrayList;

public class SortingContext {
    public static ArrayList<WorkoutPlan> filter(SortingStrategy strategy, ArrayList<WorkoutPlan> list) {
        return strategy.sort(list);
    }
}
