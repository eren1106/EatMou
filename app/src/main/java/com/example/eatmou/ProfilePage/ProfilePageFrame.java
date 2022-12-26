package com.example.eatmou.ProfilePage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eatmou.R;


public class ProfilePageFrame extends Fragment {


    Button editProfileFragmentBtn, managePwFragmentBtn, settingsFragmentBtn;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        editProfileFragmentBtn = view.findViewById(R.id.editProfileBtn);
        managePwFragmentBtn = view.findViewById(R.id.manage_pw_btn);
        settingsFragmentBtn = view.findViewById(R.id.settings_btn);

        replaceFragment(new ProfilePage());


        editProfileFragmentBtn.setOnClickListener(v -> replaceFragment(new EditProfileFragment()));

//        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, new EditProfileFragment()).commit();

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profilePageFrame, fragment);
        fragmentTransaction.commit();
    }
}