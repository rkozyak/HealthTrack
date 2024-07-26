package com.example.healthtrack.model;

public class ParticipantTrackingDecorator extends WorkoutDecorator {
    private int participants;

    public ParticipantTrackingDecorator(WorkoutComponent workout) {
        super(workout);
    }

    public void addParticipant() {
        participants++;
    }

    @Override
    public void perform() {
        super.perform();
        System.out.println("Number of participants: " + participants);
    }
}