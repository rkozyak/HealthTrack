package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;

public class ChallengeDatabaseRepository {
    private ChallengeDatabase challengeDatabase;
    private DatabaseReference db;

    public ChallengeDatabaseRepository() {
        challengeDatabase = ChallengeDatabase.getInstance();
        db = challengeDatabase.getDatabaseReference();
    }

    public void addChallenge(String userId, CommunityChallenge communityChallenge,
                           DatabaseReference.CompletionListener completionListener) {
        DatabaseReference userRef = db.child(userId);
        String workoutId = userRef.push().getKey();

        if (workoutId != null) {
            userRef.child(workoutId).setValue(communityChallenge, completionListener);
        }
    }
}
