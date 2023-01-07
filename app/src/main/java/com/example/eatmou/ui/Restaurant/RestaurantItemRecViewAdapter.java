package com.example.eatmou.ui.Restaurant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class RestaurantItemRecViewAdapter extends RecyclerView.Adapter<RestaurantItemRecViewAdapter.ViewHolder> {

    private List<Restaurant> restaurants;
    private Context context;

    public void setFilteredList(List<Restaurant> filteredList) {
        this.restaurants = filteredList;
    }

    public RestaurantItemRecViewAdapter(Context context, List<Restaurant> restaurants) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantStatus.setText(restaurant.getStatus());
        holder.restaurantCategory.setText(restaurant.getCategory());

        holder.restaurantImage.setImageResource(R.drawable.samanja);

        // set restaurant rating
        if (restaurant.getRating() == 0) {
            holder.restaurantRating.setText("--");
        } else {
            holder.restaurantRating.setText(String.valueOf(restaurant.getRating()));
        }

        // change restaurant color
        if (holder.restaurantStatus.getText().toString().equals("OPEN")) {
            holder.restaurantStatus.setTextColor(Color.parseColor("#00D408"));  //green
        } else {
            holder.restaurantStatus.setTextColor(Color.parseColor("#D40000"));  // dark red
        }

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