package com.example.healthtrack.model;

import com.google.firebase.database.DatabaseReference;

public class ChallengeDatabaseRepository {
    private ChallengeDatabase challengeDatabase;
    private DatabaseReference db;

    public ChallengeDatabaseRepository() {
        challengeDatabase = ChallengeDatabase.getInstance();
        db = challengeDatabase.getDatabaseReference();
    }
}
