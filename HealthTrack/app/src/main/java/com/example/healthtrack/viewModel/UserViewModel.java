package com.example.healthtrack.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtrack.model.User;
import com.example.healthtrack.model.UserDatabaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class UserViewModel extends ViewModel {
    private MutableLiveData<String> accountCreationResult;
    private MutableLiveData<String> updateUserInformationResult;
    private UserDatabaseRepository userDatabaseRepository;
    private FirebaseAuth auth;

    public UserViewModel() {
        accountCreationResult = new MutableLiveData<>();
        userDatabaseRepository = new UserDatabaseRepository();
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

        if (confirmPassword == null || confirmPassword.isEmpty() || confirmPassword.contains(" ")
                || !confirmPassword.equals(password)) {
            accountCreationResult.setValue("Passwords Do Not Match");
            return;
        }

        auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            User userDetails = new User(user.getUid(), username);
                            addUser(userDetails);
                        }
                    } else {
                        accountCreationResult.setValue(task.getException().getMessage());
                    }
                });
    }

    public void addUser(User user) {
        userDatabaseRepository.addUser(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   DatabaseReference databaseReference) {
                if (databaseError != null) {
                    accountCreationResult.setValue("Database Error");
                } else {
                    accountCreationResult.setValue("User Added Successfully");
                }
            }
        });
    }

    public void updateUserInformation(String userId, Integer height, Integer weight, String gender,
                                      DatabaseReference.CompletionListener completionListener) {
        userDatabaseRepository.updateUserInformation(userId, height, weight, gender,
                new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError,
                                       DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        updateUserInformationResult.setValue("Error Updating User Information");
                    } else {
                        updateUserInformationResult.
                                setValue("User Information Updated Successfully");
                    }
                }
            });
    }

    public MutableLiveData<String> getAccountCreationResult() {
        return accountCreationResult;
    }

    public MutableLiveData<String> getUpdateUserInformationResult() {
        return updateUserInformationResult;
    }
}
