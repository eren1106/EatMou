package com.example.eatmou.Restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.eatmou.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;

public class BrowseRestaurant extends AppCompatActivity {

    private RecyclerView categoryFilterRecView;
    private RecyclerView restaurantItemRecView;
    private ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_restaurant);


        categoryFilterRecView = findViewById(R.id.categoryFilterRecView);
        restaurantItemRecView = findViewById(R.id.restaurantItemRecView);


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



        restaurants.add(new Restaurant(1, "StarBuck Coffee", 5.0, "Coffee", "KL", "I love bucks", true));
        restaurants.add(new Restaurant(2, "StarBug Coffee", 5.0, "Coffee", "Penang", "I love bugs", false));
        restaurants.add(new Restaurant(3, "StarButt Coffee", 4.0, "Coffee", "KL", "I love butts", true));
        restaurants.add(new Restaurant(4, "StarDebug Coffee", 4.0, "Coffee", "Melaka", "I love debug", false));
        restaurants.add(new Restaurant(5, "StarFixBug Coffee", 5.0, "Beverage", "Sarawak", "I love fix bug", true));
        restaurants.add(new Restaurant(6, "StarNoBug Coffee", 4.0, "Beverage", "Sabah", "I no bug", true));

        RestaurantItemRecViewAdapter restaurantItemRecViewAdapter = new RestaurantItemRecViewAdapter(this, restaurants);
        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);
        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));




    }
}