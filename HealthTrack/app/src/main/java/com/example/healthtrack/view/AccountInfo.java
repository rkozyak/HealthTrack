package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.text.TextUtils;
import android.widget.RadioButton;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtrack.R;
import com.example.healthtrack.model.User;
import com.example.healthtrack.model.UserDatabaseRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

public class AccountInfo extends AppCompatActivity {

    // Variables
    private EditText editTextName;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private RadioGroup radioGroupGender;
    private Button buttonSave;
    private UserDatabaseRepository userDatabaseRepository;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_info);

        // FireBase Stuff
        mAuth = FirebaseAuth.getInstance();
        userDatabaseRepository = new UserDatabaseRepository();

        // Link text to variables
        editTextName = findViewById(R.id.editTextName);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }

    private void saveUserInfo() {
        // save info inside text fields to variables
        String name = editTextName.getText().toString();
        String height = editTextHeight.getText().toString();
        String weight = editTextWeight.getText().toString();
        int genderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(genderId);
        String gender = selectedGender.getText().toString();

        // Check if everything was inputted
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert string to int
        int heightInt = Integer.parseInt(height);
        int weightInt = Integer.parseInt(weight);

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = currentUser.getUid();

        // Create new user object
        User user = new User(userId, name);
        user.updatePersonalInformation(heightInt, weightInt, gender);


        // Save to firebase somehow? i followed a random Youtube tutorial so idk if it works
        userDatabaseRepository.updateUserInformation(userId, heightInt, weightInt, gender,
                new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError,
                                       DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        // yes error
                        Toast.makeText(AccountInfo.this, "Failed to save information",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // no error
                        Toast.makeText(AccountInfo.this, "Information saved successfully",
                                Toast.LENGTH_SHORT).show();

                        // go back to home page
                        Intent intent = new Intent(AccountInfo.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
    }
}