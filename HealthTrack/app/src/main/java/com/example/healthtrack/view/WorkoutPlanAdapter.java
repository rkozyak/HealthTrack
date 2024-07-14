package com.example.healthtrack.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtrack.R;
import com.example.healthtrack.model.WorkoutPlan;

import java.util.ArrayList;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.MyViewHolder> {
    Context context;
    ArrayList<WorkoutPlan> list;

    public WorkoutPlanAdapter(@NonNull Context context, ArrayList<WorkoutPlan> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WorkoutPlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workout_plan_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutPlanAdapter.MyViewHolder holder, int position) {
        WorkoutPlan plan = list.get(position);
        holder.userID.setText(plan.getUserId() != null ? plan.getUserId() : "");
        holder.workoutName.setText(plan.getWorkoutName() != null ? plan.getWorkoutName() : "");
        holder.notes.setText(plan.getNotes() != null ? plan.getNotes() : "");
        holder.sets.setText(String.valueOf(plan.getSets()));
        holder.reps.setText(String.valueOf(plan.getReps()));
        holder.time.setText(String.valueOf(plan.getTime()));
        holder.calories.setText(String.valueOf(plan.getCalories()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userID, workoutName, sets, reps, time, calories, notes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.planItemTextView);
            workoutName = itemView.findViewById(R.id.editItemWorkoutName);
            sets = itemView.findViewById(R.id.setsItem);
            reps = itemView.findViewById(R.id.repsItem);
            time = itemView.findViewById(R.id.timeItem);
            calories = itemView.findViewById(R.id.caloriesItem);
            notes = itemView.findViewById(R.id.notesItem);
        }
    }
}
