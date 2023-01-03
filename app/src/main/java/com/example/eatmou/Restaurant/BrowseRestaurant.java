package com.example.eatmou.Restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.eatmou.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BrowseRestaurant extends AppCompatActivity {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference restaurantRef = firestore.collection("Restaurants");

    private RecyclerView categoryFilterRecView;
    private RestaurantItemRecViewAdapter restaurantItemRecViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_restaurant);

        setUpRestaurantItemRecView();


        categoryFilterRecView = findViewById(R.id.categoryFilterRecView);



        // Hardcoded
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Coffee");
        categories.add("Beverage");
        categories.add("Pizza");
        categories.add("Cheese");
        categories.add("Fish");
        categories.add("Soup");

        CategoryFilterRecViewAdapter categoryFilterRecViewAdapter = new CategoryFilterRecViewAdapter(this, categories);
        categoryFilterRecView.setAdapter(categoryFilterRecViewAdapter);
        categoryFilterRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

//
//        restaurants = new ArrayList<>();
//        restaurants.add(new Restaurant(1, "StarBuck Coffee", 5.0, "Coffee", "KL", "I love bucks", new int[]{16, 0}, new int[]{23, 0}, false));
//        restaurants.add(new Restaurant(2, "StarBug Coffee", 5.0, "Coffee", "Penang", "I love bugs", new int[]{0, 0}, new int[]{16, 0}, true));
//        restaurants.add(new Restaurant(3, "StarButt Coffee", 4.0, "Coffee", "KL", "I love butts", new int[]{0, 0}, new int[]{16, 0}, true));
//        restaurants.add(new Restaurant(4, "StarDebug Coffee", 4.0, "Coffee", "Melaka", "I love debug", new int[]{16, 0}, new int[]{23, 0}, false));
//        restaurants.add(new Restaurant(5, "StarFixBug Coffee", 5.0, "Beverage", "Sarawak", "I love fix bug", new int[]{0, 0}, new int[]{16, 0}, true));
//        restaurants.add(new Restaurant(6, "StarNoBug Coffee", 4.0, "Beverage", "Sabah", "
//        ", new int[]{16, 0}, new int[]{23, 0}, false));
//
//        RestaurantItemRecViewAdapter restaurantItemRecViewAdapter = new RestaurantItemRecViewAdapter(this, restaurants);
//        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);
//        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));
////



    }

    private void setUpRestaurantItemRecView() {
        Query query = restaurantRef.orderBy("rating");

        FirestoreRecyclerOptions<Restaurant> options = new FirestoreRecyclerOptions.Builder<Restaurant>()
                .setQuery(query, Restaurant.class).build();

        RecyclerView restaurantItemRecView = findViewById(R.id.restaurantItemRecView);
        restaurantItemRecView.setHasFixedSize(true);
        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);
        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}