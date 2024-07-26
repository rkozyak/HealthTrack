package com.example.healthtrack.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtrack.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> list;

    public UserAdapter(@NonNull Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.challenge_participant_item, parent,
                false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        String userId = list.get(position);
        holder.userID.setText(userId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.itemTextView);
        }
    }
}
