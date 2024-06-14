package com.example.healthtrack.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountCreationViewModel extends ViewModel {
    private MutableLiveData<String> accountCreationResult;
    public AccountCreationViewModel() {
        accountCreationResult = new MutableLiveData<>();
    }

    public void createAccount(String username, String password) {
        if (username == null || username.contains(" ")) {
            accountCreationResult.setValue("Invalid Username");
            return;
        }

        if (password == null || password.contains(" ")) {
            accountCreationResult.setValue("Invalid Password");
            return;
        }
    }

    public MutableLiveData<String> getAccountCreationResult() {
        return accountCreationResult;
    }
}
