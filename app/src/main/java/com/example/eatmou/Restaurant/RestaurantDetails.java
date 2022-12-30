package com.example.eatmou.Restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatmou.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

public class RestaurantDetails extends AppCompatActivity {

    private TextView restaurantName;        // temp code
    private ImageView restaurantImage;
    private TabLayout TL_details;
    private ViewPager2 viewPager2;
    private MaterialButton holdPartyBtn;
    RestaurantDetailsVPAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        restaurantName = findViewById(R.id.restaurantName);     // temp code
        restaurantImage = findViewById(R.id.restaurantImage);
        TL_details = findViewById(R.id.TL_details);
        viewPager2 = findViewById(R.id.VP2_details);
        holdPartyBtn = findViewById(R.id.holdPartyBtn);

        Intent intent = getIntent();        // temp code
        String name = intent.getStringExtra("restaurant");    // temp code

        restaurantName.setText(name);       // temp code

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new RestaurantDetailsVPAdapter(fragmentManager, getLifecycle());
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
                Toast.makeText(getApplicationContext(), "Hold Party clicked", Toast.LENGTH_SHORT).show();
            }
        });


    }
}