package com.example.eatmou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eatmou.FoodParty.FoodPartyListFragment;
import com.example.eatmou.FoodParty.JoinedPersonModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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