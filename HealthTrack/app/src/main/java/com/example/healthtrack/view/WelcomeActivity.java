package com.example.healthtrack.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.healthtrack.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button btnBrowse = findViewById(R.id.btnBrowse);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        // Intent should switch the activity on screen to HomeActivity when btnBrowse is clicked
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create an intent to start a second activity
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Intent should switch the activity on screen to LoginActivity when btnLogin is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create an intent to start a second activity
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Intent should switch the activity on screen to AccountCreationActivity when btnRegister is clicked
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create an intent to start a second activity
                Intent intent = new Intent(WelcomeActivity.this, AccountCreationActivity.class);
                startActivity(intent);
            }
        });

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}