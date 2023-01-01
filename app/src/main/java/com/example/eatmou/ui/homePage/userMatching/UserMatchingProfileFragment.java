package com.example.eatmou.ui.homePage.userMatching;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eatmou.model.Users;
import com.example.eatmou.repository.home.HomeUserRepoImp;
import com.example.eatmou.ui.homePage.InviteDialog.InviteDialog;
import com.example.eatmou.ui.homePage.MainActivity;
import com.example.eatmou.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class UserMatchingProfileFragment extends Fragment {
    ImageView backBtn, user_image;
    Context context = getContext();
    FloatingActionButton inviteBtn;
    TextView user_title, user_bio, user_location, info1, info2, info3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_matching_profile, container, false);

        // Inflate the layout for this fragment
        backBtn = view.findViewById(R.id.back_Btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        //Get username from share preference from adapter for particular user
        SharedPreferences sharedPref = context.getSharedPreferences("SHARED_PREF_USER",Context.MODE_PRIVATE);
        String tap_Username = sharedPref.getString("USERNAME_SHARED_PREF","");

        //Set the view of fragment by fetching data from firebase
        HomeUserRepoImp repo = new HomeUserRepoImp();
        Users user = repo.getUser(tap_Username);

        //Set profile pic
        user_image = view.findViewById(R.id.user_image);
        Glide.with(view).load(user.getProfilePicUrl()).into(user_image);

        //Set username
        user_title = view.findViewById(R.id.user_title);
        user_title.setText(user.getUsername());

        //Set bio
        user_bio = view.findViewById(R.id.user_bio);
        user_bio.setText(user.getBio());

        //Set location
        user_location = view.findViewById(R.id.user_location);
        user_location.setText(user.getLocation());

        //Set user basic info
        info1 = view.findViewById(R.id.info1);
        info2 = view.findViewById(R.id.info2);
        info3 = view.findViewById(R.id.info3);
        String[] basic_info = user.getBio().split(" ");
        info1.setText(basic_info[0]);
        info2.setText(basic_info[1]);
        info3.setText(basic_info[2]);

        //Add logic for invite button
        inviteBtn = view.findViewById(R.id.inviteBtn);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        return view;
    }

    public void openDialog() {
        InviteDialog inviteDialog = new InviteDialog();
        inviteDialog.show(getActivity().getSupportFragmentManager(), "Invite Dialog");
    }
}