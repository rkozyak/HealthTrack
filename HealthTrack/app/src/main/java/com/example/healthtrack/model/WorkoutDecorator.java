package com.example.healthtrack.model;

public abstract class WorkoutDecorator implements WorkoutComponent {
    protected WorkoutComponent decoratedWorkout;

    public WorkoutDecorator(WorkoutComponent workout) {
        this.decoratedWorkout = workout;
    }

    @Override
    public void perform() {
        decoratedWorkout.perform();
    }
}
