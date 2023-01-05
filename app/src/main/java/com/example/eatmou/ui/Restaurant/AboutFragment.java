package com.example.eatmou.ui.Restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eatmou.R;

import java.util.ArrayList;

public class AboutFragment extends Fragment {

    Restaurant restaurant;

    private TextView restaurantName;
    private TextView restaurantCategory;
    private TextView restaurantLocation;
    private TextView restaurantOperatingHours;
    private TextView restaurantDescription;

    OnRestaurantListener restaurantListener;

    public AboutFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public interface OnRestaurantListener {
        void setRestaurantDetails(Restaurant restaurant);
    }

    public void setOnRestaurantListener (OnRestaurantListener restaurantListener) {
        this.restaurantListener = restaurantListener;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        Activity activity = (Activity) context;
//
//        try {
//            restaurantListener = (OnRestaurantListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must be override setRestaurantDetails");
//        }
//    }


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
        restaurantName.setText(restaurant.getName());
        restaurantCategory.setText(restaurant.getCategory());
        restaurantLocation.setText(restaurant.getLocation());
        restaurantOperatingHours.setText(setOperatingHours(restaurant.getOpeningHours(), restaurant.getClosingHours()));
        restaurantDescription.setText(restaurant.getDescription());

        return view;
    }

    private String setOperatingHours(ArrayList<Integer> openingHours, ArrayList<Integer> closingHours) {
        String str =String.format("%02d:%02d - %02d:%02d\n(Monday - Sunday)",
                openingHours.get(0), openingHours.get(1), closingHours.get(0), closingHours.get(1));
        return str;
    }
}