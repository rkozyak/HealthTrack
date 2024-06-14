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

import com.example.healthtrack.BR;
import com.example.healthtrack.R;
import com.example.healthtrack.databinding.ActivityMainBinding;
import com.example.healthtrack.viewModel.AccountCreationViewModel;

import org.w3c.dom.Text;

public class AccountCreationActivity extends AppCompatActivity {
    private AccountCreationViewModel accountCreationViewModel;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_creation);

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        textResult = findViewById(R.id.text_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create ViewModel
        accountCreationViewModel = new ViewModelProvider(this).get(AccountCreationViewModel.class);

        accountCreationViewModel.getAccountCreationResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String result) {
                textResult.setText(result);
            }
        });

        // Find the button by its ID
        Button buttonCreateAccount = findViewById(R.id.btn_create_account);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                accountCreationViewModel.createAccount(username, password);

                // Create an Intent to go back to Login
                Intent intent = new Intent(AccountCreationActivity.this, login.class);

                // Go to Login
                startActivity(intent);
            }
        });
    }
}