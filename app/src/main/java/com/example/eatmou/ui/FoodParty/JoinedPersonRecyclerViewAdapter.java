package com.example.eatmou.ui.FoodParty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinedPersonRecyclerViewAdapter extends RecyclerView.Adapter<JoinedPersonRecyclerViewAdapter.JoinedPersonViewHolder> {

    Context context;
    ArrayList<JoinedPersonModel> joinedPersonModels;

    public JoinedPersonRecyclerViewAdapter(Context context, ArrayList<JoinedPersonModel> joinedPersonModels) {
        this.context = context;
        this.joinedPersonModels = joinedPersonModels;
    }

    @NonNull
    @Override
    public JoinedPersonRecyclerViewAdapter.JoinedPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_party_joined_person, parent, false);
        return new JoinedPersonRecyclerViewAdapter.JoinedPersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedPersonRecyclerViewAdapter.JoinedPersonViewHolder holder, int position) {
//        holder.profilePic.setImageResource(joinedPersonModels.get(position).getProfilePicUrl());
        Glide.with(context).load(joinedPersonModels.get(position).getProfilePicUrl()).into(holder.profilePic);
        holder.name.setText(joinedPersonModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return joinedPersonModels.size();
    }

    public static class JoinedPersonViewHolder extends RecyclerView.ViewHolder{
        // grabbing views from food_party_card layout file
        // kinda like in onCreate method

        CircleImageView profilePic;
        TextView name;

        public JoinedPersonViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.CIV_ProfilePic);
            name = itemView.findViewById(R.id.TV_PersonName);
        }
    }
}
