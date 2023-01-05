package com.example.eatmou.Restaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eatmou.R;
import com.example.eatmou.ui.homePage.MainActivity;

public class RestaurantFragment extends Fragment {

    private Button findRestaurantBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        findRestaurantBtn = view.findViewById(R.id.find_restaurant_btn);

        findRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBrowseRestaurant();
            }
        });
        return view;
    }

    private void startBrowseRestaurant() {
        Intent intent = new Intent(getActivity(), BrowseRestaurant.class);
        startActivity(intent);

    }
}