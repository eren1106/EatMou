package com.example.eatmou.Restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RestaurantItemRecViewAdapter extends FirestoreRecyclerAdapter<Restaurant, RestaurantItemRecViewAdapter.RestaurantHolder> {

    public RestaurantItemRecViewAdapter(@NonNull FirestoreRecyclerOptions<Restaurant> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RestaurantHolder holder, int position, @NonNull Restaurant model) {
        holder.restaurantName.setText(model.getName());
        holder.restaurantRating.setText(String.valueOf(model.getRating()));
        holder.restaurantStatus.setText(model.isOpen());
        holder.restaurantCategory.setText(model.getCategory());

        holder.restaurantImage.setImageResource(R.drawable.samanja);
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_card, parent, false);
        return new RestaurantHolder(view);
    }

    class RestaurantHolder extends RecyclerView.ViewHolder {

        private TextView restaurantName;
        private TextView restaurantRating;
        private TextView restaurantStatus;
        private TextView restaurantCategory;

        private ImageView restaurantImage;

        public RestaurantHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restaurantName);
            restaurantRating = itemView.findViewById(R.id.restaurantRating);
            restaurantStatus = itemView.findViewById(R.id.restaurantStatus);
            restaurantCategory = itemView.findViewById(R.id.restaurantCategory);

            restaurantImage = itemView.findViewById(R.id.restaurantImage);
        }
    }




//    private final ArrayList<Restaurant> restaurants;
//
//    private Context context;
//
//    public RestaurantItemRecViewAdapter(Context context, ArrayList<Restaurant> restaurants) {
//        this.context = context;
//        this.restaurants = restaurants;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.restaurant_item_card, parent, false
//        );
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.restaurantName.setText(restaurants.get(position).getName());
//        holder.restaurantRating.setText(String.valueOf(restaurants.get(position).getRating()));
//        holder.restaurantCategory.setText(restaurants.get(position).getCategory());
//        holder.restaurantImage.setImageResource(R.drawable.samanja);
//
//        if (restaurants.get(position).isOpen(LocalTime.))
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, RestaurantDetails.class);
//
//                Restaurant restaurant = restaurants.get(holder.getAdapterPosition());
//                intent.putExtra("name", restaurant.getName());
//                intent.putExtra("category", restaurant.getCategory());
//                intent.putExtra("location", restaurant.getLocation());
//                intent.putExtra("description", restaurant.getDescription());
//                intent.putExtra("rating", restaurant.getRating());
//                context.startActivity(intent);
//
//                // temporary code
//                Toast.makeText(context, "Item clicked is " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return restaurants.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView restaurantName, restaurantRating, restaurantStatus, restaurantCategory;
//        private ImageView restaurantImage;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            restaurantName = itemView.findViewById(R.id.restaurantName);
//            restaurantRating = itemView.findViewById(R.id.restaurantRating);
//            restaurantStatus = itemView.findViewById(R.id.restaurantStatus);
//            restaurantCategory = itemView.findViewById(R.id.restaurantCategory);
//            restaurantImage = itemView.findViewById(R.id.restaurantImage);
//
//            itemView.setClickable(true);
//
//        }
//    }


}
