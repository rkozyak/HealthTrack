package com.example.healthtrack.view;

import com.example.healthtrack.model.User;
import com.example.healthtrack.model.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.healthtrack.R;

import java.util.ArrayList;

public class ChallengeDetails extends AppCompatActivity {
    private DatabaseReference database;
    private DatabaseReference workoutDatabase;
    private DatabaseReference userDatabase;
    private FirebaseAuth mAuth;
    private String challenger;
    private String deadline;
    private String desc;
    private ArrayList<Workout> workout;
    private ArrayList<User> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_challenge_details);


    }
}
