package com.example.eatmou.HomePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eatmou.R;
import com.example.eatmou.data.userMatching;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView user_matching_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //List for tester
        List<userMatching> userMatchingList = new ArrayList<>();
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));
        userMatchingList.add(new userMatching("Ernest Henningways", R.drawable.test2));

        //Set up the recycler view
        user_matching_list = view.findViewById(R.id.user_matching_list);
        userMatchingAdapter userMatchingAdapter = new userMatchingAdapter(getActivity(), userMatchingList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        user_matching_list.setLayoutManager(gridLayoutManager);
        user_matching_list.setAdapter(userMatchingAdapter);

        return view;
    }
}