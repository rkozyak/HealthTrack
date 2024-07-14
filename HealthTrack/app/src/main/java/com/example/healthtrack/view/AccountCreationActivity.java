package com.example.healthtrack.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtrack.R;
import com.example.healthtrack.viewModel.UserViewModel;

public class AccountCreationActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_creation);

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        textResult = findViewById(R.id.text_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getAccountCreationResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String result) {
                if (result.isEmpty()) {
                    // Create an Intent to go back to Login
                    Intent intent = new Intent(AccountCreationActivity.this, LoginActivity.class);
                    // Go to Login
                    startActivity(intent);
                } else {
                    textResult.setText(result);
                }
            }
        });

        // Find the button by its ID
        Button buttonCreateAccount = findViewById(R.id.btn_create_account);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                userViewModel.createAccount(username, password, confirmPassword);
            }
        });

        // Find the button by its ID
        Button buttonToLogin = findViewById(R.id.btn_to_login);

        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to go back to Login
                Intent intent = new Intent(AccountCreationActivity.this, LoginActivity.class);
                // Go to Login
                startActivity(intent);
            }
        });
    }
}
