package com.example.eatmou.HomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;
import com.example.eatmou.data.userMatching;

import java.util.List;

public class userMatchingAdapter extends RecyclerView.Adapter<userMatchingAdapter.MyViewHolder> {
    Context context;
    List<userMatching> userMatchingList;

    public userMatchingAdapter(Context context, List<userMatching> userMatchingList) {
        this.context = context;
        this.userMatchingList = userMatchingList;
    }

    @NonNull
    @Override
    public userMatchingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_matching_card, parent, false);
        return new userMatchingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userMatchingAdapter.MyViewHolder holder, int position) {
        holder.image.setBackgroundResource(userMatchingList.get(position).getImage());
        holder.name.setText(userMatchingList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return userMatchingList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.card_image);
            name = itemView.findViewById(R.id.card_name);
        }
    }
}
