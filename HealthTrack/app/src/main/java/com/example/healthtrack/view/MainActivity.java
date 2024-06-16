package com.example.healthtrack.view;

import android.os.Bundle;

import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtrack.R;
import com.example.healthtrack.databinding.ActivityMainBinding;
import com.example.healthtrack.viewModel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use data binding to inflate the layout
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "On Create Call");

        // Main Viewmode
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Bind Videmodel to layout (does not work)
        //binding.setVariable(BR.viewModel, viewModel);
        //binding.setLifecycleOwner(this);

    }
}