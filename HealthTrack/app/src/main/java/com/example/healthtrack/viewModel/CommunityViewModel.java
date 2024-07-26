package com.example.healthtrack.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtrack.model.ChallengeDatabaseRepository;
import com.example.healthtrack.model.CommunityChallenge;
import com.example.healthtrack.view.CommunityScreen;
import com.example.healthtrack.view.WorkoutPlans;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class CommunityViewModel extends ViewModel {
    private CommunityChallenge communityChallenge;
    private ChallengeDatabaseRepository challengeDatabaseRepository;
    private MutableLiveData<String> addChallengeResult;

    public CommunityViewModel() {
        communityChallenge = new CommunityChallenge();
        challengeDatabaseRepository = new ChallengeDatabaseRepository();
        addChallengeResult = new MutableLiveData<>();
        communityChallenge.addObserver(new CommunityScreen());
        communityChallenge.addObserver(new WorkoutPlans());
    }

    public void addChallenge(CommunityChallenge communityChallenge) {
        challengeDatabaseRepository.addChallenge(communityChallenge,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            addChallengeResult.setValue("Database Error");
                        } else {
                            addChallengeResult.setValue("Challenge Added Successfully");
                        }
                    }
                });
    }

    public void updateChallengeStatus(String status) {
        communityChallenge.setChallengeStatus(status);
    }
}
