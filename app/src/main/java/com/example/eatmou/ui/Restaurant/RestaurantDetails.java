package com.example.eatmou.ui.Restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.example.eatmou.model.UserModel;
import com.example.eatmou.ui.FoodParty.CreateFoodPartyActivity;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class RestaurantDetails extends AppCompatActivity {

    private ImageView backBtn;
    private ImageView restaurantImage;
    private MaterialButton holdPartyBtn;
    TabLayout TL_details;
    ViewPager2 viewPager2;
    RestaurantDetailsVPAdapter adapter;

    Restaurant restaurant;

    Intent intent;
    UserModel currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        currentUser = MainActivity.getUser();
        if (currentUser!= null) {
            Log.i("Login user:" , currentUser.getUserID());
            Log.i("Login username: " , currentUser.getUsername());
        } else Log.i("Login user: " , "No login user");


        intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        double rating = intent.getDoubleExtra("rating",0);
        String category = intent.getStringExtra("category");
        String location = intent.getStringExtra("location");
        ArrayList<Integer> openingHours = intent.getIntegerArrayListExtra("openingHours");
        ArrayList<Integer> closingHours = intent.getIntegerArrayListExtra("closingHours");
        String description = intent.getStringExtra("description");
        String imageURL = intent.getStringExtra("imageURL");

        restaurant = new Restaurant(name, rating, category, location, description, openingHours, closingHours);
        restaurant.setId(id);
        restaurant.setImageURL(imageURL);

        RatingSystem.updateRestaurantRating(restaurant.getId());

        System.out.println(restaurant.getId());

        restaurantImage = findViewById(R.id.restaurantImage);
        TL_details = findViewById(R.id.TL_details);
        viewPager2 = findViewById(R.id.VP2_details);
        holdPartyBtn = findViewById(R.id.holdPartyBtn);
        backBtn = findViewById(R.id.back_Btn);

        Glide.with(getApplicationContext()).load(imageURL).into(restaurantImage);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new RestaurantDetailsVPAdapter(fragmentManager, getLifecycle(), restaurant, currentUser);
        viewPager2.setAdapter(adapter);

        TL_details.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                TL_details.selectTab(TL_details.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        holdPartyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // temporary code
//                Toast.makeText(getApplicationContext(), "Hold Party clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RestaurantDetails.this, CreateFoodPartyActivity.class);
                intent.putExtra("restaurant name", restaurant.getName());
                startActivity(intent);
            }
        });


    }
}