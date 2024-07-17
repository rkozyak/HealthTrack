package com.example.healthtrack.view;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtrack.R;
import com.example.healthtrack.model.Workout;
import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.viewModel.WorkoutViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<WorkoutPlan> list;
    private ArrayList<String> nameList;

    public WorkoutPlanAdapter(@NonNull Context context, ArrayList<WorkoutPlan> list, ArrayList<String> nameList) {
        this.context = context;
        this.list = list;
        this.nameList = nameList;
    }

    @NonNull
    @Override
    public WorkoutPlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workout_plan_item, parent, false);
        WorkoutPlanAdapter.MyViewHolder myViewHolder = new MyViewHolder(v);
        if (!myViewHolder.logged.getText().toString().equals("Logged")) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View popupView = LayoutInflater.from(v.getContext()).inflate(R.layout.workout_plan_item, null);

                    // Create the PopupWindow
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // Lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    // Show the popup window
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    TextView userID = popupView.findViewById(R.id.editItemWorkoutName);
                    TextView workoutName = popupView.findViewById(R.id.planItemTextView);
                    TextView sets = popupView.findViewById(R.id.setsItem);
                    TextView reps = popupView.findViewById(R.id.repsItem);
                    TextView time = popupView.findViewById(R.id.timeItem);
                    TextView calories = popupView.findViewById(R.id.caloriesItem);
                    TextView notes = popupView.findViewById(R.id.notesItem);
                    TextView logged = popupView.findViewById(R.id.logged);

                    userID.setText(myViewHolder.userID.getText().toString());
                    workoutName.setText(myViewHolder.workoutName.getText().toString());
                    sets.setText(myViewHolder.sets.getText().toString());
                    reps.setText(myViewHolder.reps.getText().toString());
                    time.setText(myViewHolder.time.getText().toString());
                    calories.setText(myViewHolder.calories.getText().toString());
                    notes.setText(myViewHolder.notes.getText().toString());
                    logged.setText(myViewHolder.logged.getText().toString());
                }
            });
        }
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutPlanAdapter.MyViewHolder holder, int position) {
        WorkoutPlan plan = list.get(position);
        String logged = "Not Logged";
        if (nameList.contains(plan.getName())) {
            logged = "Logged";
        }
        holder.userID.setText(plan.getUserId());
        holder.workoutName.setText(plan.getName());
        holder.notes.setText(plan.getNotes());
        holder.sets.setText(String.valueOf(plan.getSets()));
        holder.reps.setText(String.valueOf(plan.getRepsPerSet()));
        holder.time.setText(String.valueOf(plan.getTime()));
        holder.calories.setText(String.valueOf(plan.getCaloriesPerSet()));
        holder.logged.setText(logged);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userID;
        private TextView workoutName;
        private TextView sets;
        private TextView reps;
        private TextView time;
        private TextView calories;
        private TextView notes;
        private TextView logged;
        private Button addButton;
        private FirebaseAuth mAuth;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.editItemWorkoutName);
            workoutName = itemView.findViewById(R.id.planItemTextView);
            sets = itemView.findViewById(R.id.setsItem);
            reps = itemView.findViewById(R.id.repsItem);
            time = itemView.findViewById(R.id.timeItem);
            calories = itemView.findViewById(R.id.caloriesItem);
            notes = itemView.findViewById(R.id.notesItem);
            logged = itemView.findViewById(R.id.logged);

            addButton = itemView.findViewById(R.id.addPlanItem);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (logged.getText().toString().equals("Logged")) {
                        Toast.makeText(v.getContext(), "Already logged", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        String userId = currentUser.getUid();

                        String workoutNameItem = workoutName.getText().toString();
                        int caloriesItem = Integer.parseInt(calories.getText().toString());
                        int setsItem = Integer.parseInt(sets.getText().toString());
                        int repsItem = Integer.parseInt(reps.getText().toString());
                        String notesItem = notes.getText().toString();
                        Workout workout = new Workout(userId, workoutNameItem, caloriesItem,
                                setsItem, repsItem, notesItem);
                        WorkoutViewModel workoutViewModel = new WorkoutViewModel();
                        workoutViewModel.addWorkout(userId, workout);
                        logged.setText("Logged");
                    }
                }
            });
        }
    }
}
