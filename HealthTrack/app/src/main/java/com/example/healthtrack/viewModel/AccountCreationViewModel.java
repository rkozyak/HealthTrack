package com.example.healthtrack.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtrack.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountCreationViewModel extends ViewModel {
    private MutableLiveData<String> accountCreationResult;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth auth;

    public AccountCreationViewModel() {
        accountCreationResult = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();
    }

    public void createAccount(String username, String password, String confirmPassword) {
        if (username == null || username.isEmpty() || username.contains(" ")) {
            accountCreationResult.setValue("Invalid Username");
            return;
        }

        if (password == null || password.isEmpty() || password.contains(" ")) {
            accountCreationResult.setValue("Invalid Password");
            return;
        }

        if (confirmPassword == null || confirmPassword.isEmpty() || confirmPassword.contains(" ") || !confirmPassword.equals(password)) {
            accountCreationResult.setValue("Password Does Not Match");
            return;
        }

        auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            User userDetails = new User(username, password);
                            myRef.child("users").child(user.getUid()).setValue(userDetails)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            accountCreationResult.setValue("");
                                        } else {
                                            accountCreationResult.setValue("Database Error");
                                        }
                                    });
                        }
                    } else {
                        accountCreationResult.setValue("Authentication Error");
                    }
                });
    }

    public MutableLiveData<String> getAccountCreationResult() {
        return accountCreationResult;
    }
}
