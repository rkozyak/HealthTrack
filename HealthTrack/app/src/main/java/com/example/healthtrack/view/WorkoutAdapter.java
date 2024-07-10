package com.example.healthtrack.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.healthtrack.R;
import com.example.healthtrack.model.Workout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Workout> {
    private Context mContext;
    private List<Workout> workoutList = new ArrayList<>();

    public WorkoutAdapter(@NonNull Context context, ArrayList<Workout> list) {
        super(context, 0, list);
        mContext = context;
        workoutList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.workout_list_item, parent,
                    false);
        }

        Workout currentWorkout = workoutList.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentWorkout.getDate());

        TextView date = (TextView) listItem.findViewById(R.id.date);
        String dateFormat = calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH)
                + "/" + calendar.get(Calendar.YEAR);

        date.setText(dateFormat);

        TextView name = (TextView) listItem.findViewById(R.id.itemName);
        name.setText(currentWorkout.getName());

        return listItem;
    }
}
