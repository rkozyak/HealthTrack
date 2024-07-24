package com.example.healthtrack.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.healthtrack.model.CommunityChallenge;
import com.example.healthtrack.view.CommunityScreen;
import com.example.healthtrack.view.WorkoutPlans;

public class CommunityViewModel extends ViewModel {
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
