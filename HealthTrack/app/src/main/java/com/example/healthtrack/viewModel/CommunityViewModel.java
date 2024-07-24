package com.example.healthtrack.viewModel;

import com.example.healthtrack.model.CommunityChallenge;
import com.example.healthtrack.view.CommunityScreen;
import com.example.healthtrack.view.WorkoutPlans;

public class CommunityViewModel {
    private CommunityChallenge communityChallenge;

    public CommunityViewModel() {
        communityChallenge = new CommunityChallenge();
        communityChallenge.addObserver(new CommunityScreen());
        communityChallenge.addObserver(new WorkoutPlans());
    }

    public void updateChallengeStatus(String status) {
        communityChallenge.setChallengeStatus(status);
    }
}
