package com.example.eatmou.ui.Restaurant;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eatmou.UserModel;

public class RestaurantDetailsVPAdapter extends FragmentStateAdapter {

    Restaurant restaurant;
    UserModel currentUser;

    public RestaurantDetailsVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
                                      Restaurant restaurant, UserModel currentUser) {
        super(fragmentManager, lifecycle);
        this.restaurant = restaurant;
        this.currentUser = currentUser;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1)
            return new ReviewFragment(restaurant, currentUser);
        else
            return new AboutFragment(restaurant);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
