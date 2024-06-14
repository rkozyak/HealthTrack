package com.example.healthtrack.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountCreationViewModel extends ViewModel {
    private MutableLiveData<String> accountCreationResult;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public AccountCreationViewModel() {
        accountCreationResult = new MutableLiveData<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public void createAccount(String username, String password, String confirmPassword) {
        if (username == null || username.contains(" ")) {
            accountCreationResult.setValue("Invalid Username");
            return;
        }

        if (password == null || password.contains(" ")) {
            accountCreationResult.setValue("Invalid Password");
            return;
        }

        if (confirmPassword == null || confirmPassword.contains(" ") || !confirmPassword.equals(password)) {
            accountCreationResult.setValue("Password Does Not Match");
            return;
        }

        accountCreationResult.setValue("");
    }

    public MutableLiveData<String> getAccountCreationResult() {
        return accountCreationResult;
    }
}
