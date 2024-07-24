package com.example.healthtrack.model;

import java.util.ArrayList;

public class CommunityChallenge {
    private String userId;
    private String name;
    private ArrayList<WorkoutPlan> workoutPlans;
    private int deadlineDay;
    private int deadlineMonth;
    private int deadlineYear;
    private ArrayList<String> participants;

    public CommunityChallenge() {
    }

    public CommunityChallenge(String userId, String name, ArrayList<WorkoutPlan> workoutPlans,
                              int deadlineDay, int deadlineMonth, int deadlineYear) {
        this.userId = userId;
        this.name = name;
        this.workoutPlans = workoutPlans;
        this.deadlineDay = deadlineDay;
        this.deadlineMonth = deadlineMonth;
        this.deadlineYear = deadlineYear;
        participants = new ArrayList<String>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<WorkoutPlan> getWorkoutPlans() {
        return workoutPlans;
    }

    public int getDeadlineDay() {
        return deadlineDay;
    }

    public int getDeadlineMonth() {
        return deadlineMonth;
    }

    public int getDeadlineYear() {
        return deadlineYear;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void addParticipant(String userId) {
        participants.add(userId);
    }
}
