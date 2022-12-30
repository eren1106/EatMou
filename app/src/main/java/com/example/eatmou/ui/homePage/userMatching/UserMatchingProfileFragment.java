package com.example.eatmou.ui.homePage.userMatching;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eatmou.ui.homePage.InviteDialog.InviteDialog;
import com.example.eatmou.ui.homePage.MainActivity;
import com.example.eatmou.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserMatchingProfileFragment extends Fragment {
    ImageView backBtn;
    Context context = getContext();
    FloatingActionButton inviteBtn;

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