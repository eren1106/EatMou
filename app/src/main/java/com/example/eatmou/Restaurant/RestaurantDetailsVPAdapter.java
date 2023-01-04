package com.example.eatmou.Restaurant;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RestaurantDetailsVPAdapter extends FragmentStateAdapter {

    Restaurant restaurant;

    public RestaurantDetailsVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Restaurant restaurant) {
        super(fragmentManager, lifecycle);
        this.restaurant = restaurant;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1)
            return new ReviewFragment(restaurant);
        else
            return new AboutFragment(restaurant);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
