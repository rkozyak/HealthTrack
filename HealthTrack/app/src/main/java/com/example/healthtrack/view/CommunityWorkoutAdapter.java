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

public class CommunityWorkoutAdapter extends RecyclerView.Adapter<CommunityWorkoutAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<WorkoutPlan> list;
    private ArrayList<WorkoutPlan> returnList;

    public CommunityWorkoutAdapter(@NonNull Context context, ArrayList<WorkoutPlan> list, ArrayList<WorkoutPlan> returnList) {
        this.context = context;
        this.list = list;
        this.returnList = returnList;
    }

    @NonNull
    @Override
    public CommunityWorkoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workout_plan_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityWorkoutAdapter.MyViewHolder holder, int position) {
        WorkoutPlan plan = list.get(position);
        String added = "Not Added";
        if (returnList.contains(plan)) {
            added = "Added";
        }
        holder.userID.setText(plan.getUserId());
        holder.workoutName.setText(plan.getName());
        holder.notes.setText(plan.getNotes());
        holder.sets.setText(String.valueOf(plan.getSets()));
        holder.reps.setText(String.valueOf(plan.getRepsPerSet()));
        holder.time.setText(String.valueOf(plan.getTime()));
        holder.calories.setText(String.valueOf(plan.getCaloriesPerSet()));
        holder.added.setText(added);
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
        private TextView added;
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
            added = itemView.findViewById(R.id.logged);

            addButton = itemView.findViewById(R.id.addPlanItem);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (added.getText().toString().equals("Added")) {
                        Toast.makeText(v.getContext(), "Already added", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        String userId = currentUser.getUid();

                        String workoutNameItem = workoutName.getText().toString();
                        int caloriesItem = Integer.parseInt(calories.getText().toString());
                        int setsItem = Integer.parseInt(sets.getText().toString());
                        int repsItem = Integer.parseInt(reps.getText().toString());
                        int timeItem = Integer.parseInt(time.getText().toString());
                        String notesItem = notes.getText().toString();
                        WorkoutPlan workoutPlan = new WorkoutPlan(userId, workoutNameItem, caloriesItem,
                                setsItem, repsItem, timeItem, notesItem);

                        AddWorkoutCommunity.returnList.add(workoutPlan);

                        added.setText("Added");
                    }
                }
            });
        }
    }
}
