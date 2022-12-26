package com.example.eatmou.HomePage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.eatmou.FoodParty.FoodPartyListFragment;
import com.example.eatmou.Inbox.InboxFragment;
import com.example.eatmou.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomBarChart;
    BottomNavigationView bottomBarViewChart;
    FloatingActionButton partyPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the background of bottomAppBar as null
        bottomBarChart = findViewById(R.id.bottomBarChart);
        bottomBarChart.setBackground(null);

        //Set the fragment replace layout
        bottomBarViewChart = findViewById(R.id.bottomBarViewChart);
        bottomBarViewChart.setOnNavigationItemSelectedListener(navListener);
        //TODO: Disable the default selected item in bottomNavBar

        //Default launch matching page
        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();

        //Click to launch Matching page
        partyPageBtn = findViewById(R.id.partyPageBtn);
        partyPageBtn.setOnClickListener(view -> {
            Fragment selectedFragment1 = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment1).commit();
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()){
            case R.id.party:
                selectedFragment = new FoodPartyListFragment();
                break;
            case R.id.inbox:
                selectedFragment = new InboxFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
        return true;
    };
}