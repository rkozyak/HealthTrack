package com.example.healthtrack.model;

import java.util.ArrayList;

public class SortingContext {
    public static ArrayList<WorkoutPlan> filter(SortingStrategy strategy,
                                                ArrayList<WorkoutPlan> list) {
        return strategy.sort(list);
    }

    public static ArrayList<String> challengeFilter(SortingStrategy strategy, ArrayList<String> list) {
        return strategy.challengeSort(list);
    }
}
