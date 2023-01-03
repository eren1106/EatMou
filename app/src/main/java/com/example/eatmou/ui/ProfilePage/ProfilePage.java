package com.example.eatmou.ui.ProfilePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eatmou.R;
import com.example.eatmou.model.Users;

public class ProfilePage extends Fragment {

    Button btnEditProfileFragment, btnManagePwFragment, btnSettingsFragment;
    ImageView userBgImg, userProfileImg;
    TextView userName, userBio;
    View view;
    Users user = new Users();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_profile_page, container, false);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        btnEditProfileFragment = view.findViewById(R.id.btnEditProfile);
        btnManagePwFragment = view.findViewById(R.id.btnManagePw);
        btnSettingsFragment = view.findViewById(R.id.btnSettings);

        btnEditProfileFragment.setOnClickListener(v -> replaceFragment(new EditProfileFragment()));
        btnManagePwFragment.setOnClickListener(v -> replaceFragment(new ManagePwFragment()));
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profilePageFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

