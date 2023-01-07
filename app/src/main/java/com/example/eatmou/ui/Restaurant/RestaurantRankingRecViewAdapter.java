package com.example.eatmou.ui.Restaurant;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

public class RestaurantRankingRecViewAdapter extends RecyclerView.Adapter<RestaurantRankingRecViewAdapter.ViewHolder>{

    private List<Restaurant> restaurants;
    private Context context;

    public RestaurantRankingRecViewAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_ranking_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        holder.rankNum.setText(String.valueOf(position + 1));
        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantCategory.setText(restaurant.getCategory());
        holder.restaurantImage.setImageResource(R.drawable.samanja);

        // set restaurant rating
        if (restaurant.getRating() == 0) {
            holder.restaurantRating.setText("--");
        } else {
            holder.restaurantRating.setText(String.valueOf(restaurant.getRating()));
        }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView rankNum;
        private ImageView restaurantImage;
        private TextView restaurantName;
        private TextView restaurantRating;
        private TextView restaurantCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rankNum = itemView.findViewById(R.id.rankNum);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantRating = itemView.findViewById(R.id.restaurantRating);
            restaurantCategory = itemView.findViewById(R.id.restaurantCategory);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    Restaurant restaurant = restaurants.get(position);
                    Intent intent = new Intent(context, RestaurantDetails.class);
                    intent.putExtra("id", restaurant.getId());
                    intent.putExtra("name", restaurant.getName());
                    intent.putExtra("rating", restaurant.getRating());
                    intent.putExtra("category", restaurant.getCategory());
                    intent.putExtra("location", restaurant.getLocation());
                    intent.putExtra("description", restaurant.getDescription());
                    intent.putIntegerArrayListExtra("openingHours", (ArrayList<Integer>) restaurant.getOpeningHours());
                    intent.putIntegerArrayListExtra("closingHours", (ArrayList<Integer>) restaurant.getClosingHours());

                    itemView.getContext().startActivity(intent);
                    Toast.makeText(itemView.getContext(), "Item clicked: " + restaurant.getId(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
