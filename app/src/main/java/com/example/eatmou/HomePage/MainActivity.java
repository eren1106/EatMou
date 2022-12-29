package com.example.eatmou.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.eatmou.Authentication.ForgotPassPage;
import com.example.eatmou.Authentication.LoginPage;
import com.example.eatmou.Authentication.SignUpPage;
import com.example.eatmou.Authentication.UpdatePassPage;
import com.example.eatmou.ProfilePage.ProfilePage;
import com.example.eatmou.ProfilePage.ProfilePageFrame;
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
        partyPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.party:
                    selectedFragment = new HomeFragment();

                case R.id.profile:
                    selectedFragment = new ProfilePageFrame();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}