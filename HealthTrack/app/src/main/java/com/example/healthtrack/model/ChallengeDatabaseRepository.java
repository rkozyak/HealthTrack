package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;

public class ChallengeDatabaseRepository {
    private ChallengeDatabase challengeDatabase;
    private DatabaseReference db;

    public ChallengeDatabaseRepository() {
        challengeDatabase = ChallengeDatabase.getInstance();
        db = challengeDatabase.getDatabaseReference();
    }

    public void addChallenge(CommunityChallenge communityChallenge,
                           DatabaseReference.CompletionListener completionListener) {
        String challengeId = db.push().getKey();

        if (challengeId != null) {
            db.child(challengeId).setValue(communityChallenge, completionListener);
        }
    }

    public DatabaseReference getDatabaseReference() {
        return db;
    }
}
