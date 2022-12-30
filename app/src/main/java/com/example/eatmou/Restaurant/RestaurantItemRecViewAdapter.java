package com.example.eatmou.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;

import java.util.ArrayList;

public class RestaurantItemRecViewAdapter extends RecyclerView.Adapter<RestaurantItemRecViewAdapter.ViewHolder> {

    private final ArrayList<Restaurant> restaurants;

    private Context context;

    public RestaurantItemRecViewAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.restaurant_item_card, parent, false
        );
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.restaurantName.setText(restaurants.get(position).getName());
        holder.restaurantRating.setText(String.valueOf(restaurants.get(position).getRating()));
        holder.restaurantStatus.setText(restaurants.get(position).isOpen());
        holder.restaurantCategory.setText(restaurants.get(position).getCategory());
        holder.restaurantImage.setImageResource(R.drawable.samanja);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RestaurantDetails.class);

                Restaurant restaurant = restaurants.get(holder.getAdapterPosition());
                intent.putExtra("name", restaurant.getName());
                intent.putExtra("category", restaurant.getCategory());
                intent.putExtra("location", restaurant.getLocation());
                intent.putExtra("description", restaurant.getDescription());
                intent.putExtra("rating", restaurant.getRating());
                context.startActivity(intent);

                // temporary code
                Toast.makeText(context, "Item clicked is " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView restaurantName, restaurantRating, restaurantStatus, restaurantCategory;
        private ImageView restaurantImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantRating = itemView.findViewById(R.id.restaurantRating);
            restaurantStatus = itemView.findViewById(R.id.restaurantStatus);
            restaurantCategory = itemView.findViewById(R.id.restaurantCategory);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);

            itemView.setClickable(true);

        }
    }


}
