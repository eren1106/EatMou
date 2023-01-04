package com.example.eatmou.Restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eatmou.R;

import java.util.Arrays;

public class AboutFragment extends Fragment {

    private TextView restaurantName;
    private TextView restaurantCategory;
    private TextView restaurantLocation;
    private TextView restaurantOperatingHours;

    OnRestaurantListener restaurantListener;

    public AboutFragment() {
        // empty public constructor
    }

    public interface OnRestaurantListener {
        void setRestaurantDetails(Restaurant restaurant);
    }

    public void setOnRestaurantListener (OnRestaurantListener restaurantListener) {
        this.restaurantListener = restaurantListener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        try {
            restaurantListener = (OnRestaurantListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must be override setRestaurantDetails");
        }
    }

    private TextView restaurantDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container);

        restaurantName = view.findViewById(R.id.restaurantName);
        restaurantCategory = view.findViewById(R.id.restaurantCategory);
        restaurantLocation = view.findViewById(R.id.restaurantLocation);
        restaurantOperatingHours = view.findViewById(R.id.restaurantOperatingHours);
        restaurantDescription = view.findViewById(R.id.restaurantDescription);

//
//        String name = intent.getStringExtra("name").toString();
//        String category = intent.getStringExtra("category").toString();
//        String location = intent.getStringExtra("location").toString();
//        int[] openingHours = intent.getIntArrayExtra("openingHours");
//        int[] closingHours = intent.getIntArrayExtra("closingHours");
//        String description = intent.getStringExtra("description").toString();
//
//        System.out.println(name + " " + category + " " + location + " " + description);
//        System.out.println(Arrays.toString(openingHours));
//        System.out.println(Arrays.toString(closingHours));
//
//        restaurantName.setText(name);
//        restaurantCategory.setText(category);
//        restaurantLocation.setText(location);
//        restaurantOperatingHours.setText(setOperatingHours(openingHours, closingHours));
//        restaurantDescription.setText(description);

        return view;
    }

    private String setOperatingHours(int[] openingHours, int[] closingHours) {
        return openingHours[0] + ":" + openingHours[1] + " - " + closingHours[0] + ":" + closingHours[1];
    }
}