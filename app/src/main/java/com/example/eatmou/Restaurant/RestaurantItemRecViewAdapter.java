package com.example.eatmou.Restaurant;

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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RestaurantItemRecViewAdapter extends RecyclerView.Adapter<RestaurantItemRecViewAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private final List<Restaurant> restaurants;
    private Context context;

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
        holder.restaurantRating.setText(String.valueOf(restaurant.getRating()));
        holder.restaurantStatus.setText(restaurant.getStatus());
        holder.restaurantCategory.setText(restaurant.getCategory());
        holder.restaurantImage.setImageResource(R.drawable.samanja);

        // change restaurant color
        if (holder.restaurantStatus.getText().toString().equals("OPEN")) {
            holder.restaurantStatus.setTextColor(Color.parseColor("#00D408"));  //green
        } else {
            holder.restaurantStatus.setTextColor(Color.parseColor("#D40000"));  // dark red
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getBindingAdapterPosition();
                Restaurant restaurant = restaurants.get(position);
                Intent intent = new Intent(holder.itemView.getContext(), RestaurantDetails.class);
                intent.putExtra("id", restaurant.getId());
                intent.putExtra("name", restaurant.getName());
                intent.putExtra("rating", restaurant.getRating());
                intent.putExtra("category", restaurant.getCategory());
                intent.putExtra("location", restaurant.getLocation());
                intent.putExtra("description", restaurant.getDescription());
                intent.putIntegerArrayListExtra("openingHours", (ArrayList<Integer>) restaurant.getOpeningHours());
                intent.putIntegerArrayListExtra("closingHours", (ArrayList<Integer>) restaurant.getClosingHours());
                holder.itemView.getContext().startActivity(intent);
                Toast.makeText(holder.itemView.getContext(), "Item clicked: " + restaurant.getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = listener;
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



        }
    }


    public interface OnItemClickListener {
        void onItemClick(Restaurant restaurant, int position);
    }


}
