package com.example.healthtrack.model;

import java.util.ArrayList;

public class NameSortStrategy implements SortingStrategy {
    private String name;

    public NameSortStrategy() {
        this.name = "";
    }

    @Override
    public ArrayList<WorkoutPlan> sort(ArrayList<WorkoutPlan> list) {
        ArrayList<WorkoutPlan> newList = new ArrayList<WorkoutPlan>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().contains(this.name)) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    public ArrayList<String> challengeSort(ArrayList<String> list) {
        ArrayList<String> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(this.name)) {
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    public void setName(String name) {
        this.name = name;
    }
}
