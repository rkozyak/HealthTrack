package com.example.healthtrack.view;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
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
import com.example.healthtrack.model.ChallengeDatabase;
import com.example.healthtrack.model.CommunityChallenge;
import com.example.healthtrack.model.Workout;
import com.example.healthtrack.model.WorkoutPlan;
import com.example.healthtrack.viewModel.WorkoutViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityChallengeAdapter extends RecyclerView.Adapter<CommunityChallengeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> list;

    public CommunityChallengeAdapter(@NonNull Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommunityChallengeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.challenge_item, parent, false);

        CommunityChallengeAdapter.MyViewHolder myViewHolder = new CommunityChallengeAdapter.MyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChallengeDetails.class);
                intent.putExtra("challengeId", myViewHolder.challengeId.getText());
                context.startActivity(intent);
            }
        });

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityChallengeAdapter.MyViewHolder holder, int position) {
        String challengeId = list.get(position);
        DatabaseReference database = ChallengeDatabase.getInstance().getDatabaseReference().child(challengeId);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, String> data = (HashMap<String, String>) snapshot.getValue();
                holder.userID.setText(data.get("userId"));
                holder.challengeName.setText(data.get("name"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.challengeId.setText(challengeId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView userID;
        private TextView challengeName;
        private TextView challengeId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userID = itemView.findViewById(R.id.challengeUser);
            challengeName = itemView.findViewById(R.id.challengeName);
            challengeId = itemView.findViewById(R.id.challengeId);
        }
    }
}