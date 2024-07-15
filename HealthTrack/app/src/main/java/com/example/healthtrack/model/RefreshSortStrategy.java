package com.example.healthtrack.model;

import java.util.ArrayList;
import java.util.Collections;

public class RefreshSortStrategy implements SortingStrategy {
    public RefreshSortStrategy() {
    }

    @Override
    public ArrayList<WorkoutPlan> sort(ArrayList<WorkoutPlan> list) {
        Collections.shuffle(list);
        return list;
    }
}
