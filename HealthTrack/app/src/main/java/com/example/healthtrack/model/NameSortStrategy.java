package com.example.healthtrack.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
