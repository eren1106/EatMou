package com.example.eatmou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eatmou.FoodParty.FoodPartyListFragment;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    Button toFoodPartyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toFoodPartyPage = findViewById(R.id.toFoodPartyPage);
        toFoodPartyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFoodPartyPage.setVisibility(View.GONE);

                Fragment fragment = new FoodPartyListFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, fragment).commit();
            }
        });
    }
}