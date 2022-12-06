package com.example.eatmou.FoodParty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;

import java.util.ArrayList;

public class FoodPartyRecyclerViewAdapter extends RecyclerView.Adapter<FoodPartyRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<FoodPartyModel> foodPartyModels;

    private OnCardListener onCardListener;

    public FoodPartyRecyclerViewAdapter(Context context, ArrayList<FoodPartyModel> foodPartyModels, OnCardListener onCardListener) {
        this.context = context;
        this.foodPartyModels = foodPartyModels;
        this.onCardListener = onCardListener;
    }

    @NonNull
    @Override
    public FoodPartyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_party_card, parent, false);
        return new FoodPartyRecyclerViewAdapter.MyViewHolder(view, this.onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPartyRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.title.setText(foodPartyModels.get(position).getTitle());
        holder.organizer.setText(foodPartyModels.get(position).getOrganizer());
        holder.location.setText(foodPartyModels.get(position).getLocation());
        holder.date.setText(foodPartyModels.get(position).getDate());
        holder.time.setText(foodPartyModels.get(position).getTime());
        holder.personNumber.setText(foodPartyModels.get(position).getJoinedPersonModels().size() + "/9");
    }

    @Override
    public int getItemCount() {
        return foodPartyModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // grabbing views from food_party_card layout file
        // kinda like in onCreate method

        TextView title, organizer, location, date, time, personNumber;
        OnCardListener onCardListener;

        public MyViewHolder(@NonNull View itemView, OnCardListener onCardListener) {
            super(itemView);

            title = itemView.findViewById(R.id.TV_FoodPartyTitle);
            organizer = itemView.findViewById(R.id.TV_OrganizerText);
            location = itemView.findViewById(R.id.TV_LocationText);
            date = itemView.findViewById(R.id.TV_DateText);
            time = itemView.findViewById(R.id.TV_TimeText);
            personNumber = itemView.findViewById(R.id.TV_JoinedPersonNumber);

            this.onCardListener = onCardListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener{
        void onCardClick(int position);
    }
}
