package com.example.healthtrack.model;

import java.util.ArrayList;
import java.util.List;

public class CommunityChallenge {
    private List<Observer> observers = new ArrayList<>();
    private String challengeStatus;

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(challengeStatus);
        }
    }

    public void setChallengeStatus(String status) {
        this.challengeStatus = status;
        notifyObservers();
    }
}
