package com.example.eatmou.ui.Restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eatmou.R;

public class RestaurantFragment extends Fragment {

    private Button findRestaurantBtn;
    private Button viewLeaderboardBtn;
    private TextView browseText, leadText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        findRestaurantBtn = view.findViewById(R.id.find_restaurant_btn);
        viewLeaderboardBtn = view.findViewById(R.id.view_leaderboard_btn);
        browseText = view.findViewById(R.id.headerMessage);
        leadText = view.findViewById(R.id.leaderboardMessage);

        findRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBrowseRestaurant();
            }
        });


        viewLeaderboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RestaurantLeaderboard.class);
                startActivity(intent);
            }
        });

        changeFontSize();

        return view;
    }

    private void startBrowseRestaurant() {
        Intent intent = new Intent(getActivity(), BrowseRestaurant.class);
        startActivity(intent);

    }

    private void changeFontSize(){
        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        int size = fontPreference.getInt("FONT_SP",0)-1;
        browseText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        leadText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}