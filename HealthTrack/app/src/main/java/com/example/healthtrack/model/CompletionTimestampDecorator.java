package com.example.healthtrack.model;

import java.time.LocalDateTime;

public class CompletionTimestampDecorator extends WorkoutDecorator {
    private LocalDateTime completionTimestamp;

    public CompletionTimestampDecorator(WorkoutComponent workout) {
        super(workout);
    }

    public void markCompleted() {
        completionTimestamp = LocalDateTime.now();
    }

    @Override
    public void perform() {
        super.perform();
        if (completionTimestamp != null) {
            System.out.println("Workout completed at: " + completionTimestamp);
        }
    }
}
