package com.example.eatmou.ui.ProfilePage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
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

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.example.eatmou.model.Users;
import com.example.eatmou.ui.ProfilePage.settings.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends Fragment {

    Button btnEditProfileFragment, btnManagePwFragment, btnSettingsFragment;
    ImageView userBgImg, userProfileImg;
    TextView userName, userBio;
    View view;
    Users currentUser;

    private FirebaseFirestore db;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = null;
        if (user != null) userID = user.getUid();

        userBgImg = view.findViewById(R.id.userBgImg);
        userProfileImg = view.findViewById(R.id.userProfileImg);
        userName = view.findViewById(R.id.userName);
        userBio = view.findViewById(R.id.userBio);
        currentUser = new Users();

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    currentUser = Users.toObject(document.getData());
                    HashMap<String, String> list = (HashMap<String, String>) document.get("Keywords");
//                    ArrayList<String> list = new ArrayList<String>(map.values());
                    currentUser.setKeywords(list);
//                    System.out.println(list);

                    Log.d("User document", "DocumentSnapshot data: " + document.getData());

                    Glide.with(view).load(currentUser.getProfilePicUrl()).into(userProfileImg);
                    Glide.with(view).load(currentUser.getProfileBgPicUrl()).into(userBgImg);
                    userName.setText(currentUser.getUsername());
                    userBio.setText(currentUser.getBio());

                } else Log.d("User document", "No such document");
            } else Log.d("User document", "get failed with ", task.getException());
    });

        btnEditProfileFragment = view.findViewById(R.id.btnEditProfile);
        btnManagePwFragment = view.findViewById(R.id.btnManagePw);
        btnSettingsFragment = view.findViewById(R.id.btnSettings);

        btnEditProfileFragment.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("Email", user.getEmail());
            args.putString("UserID", user.getUid());
            args.putString("Username", currentUser.getUsername());
            args.putString("Dob", currentUser.getDOBText());
            args.putString("ProfilePicUrl", currentUser.getProfilePicUrl());
            args.putString("ProfileBgPicUrl", currentUser.getProfileBgPicUrl());
            args.putString("Bio", currentUser.getBio());
            args.putString("Location", currentUser.getLocation());
            HashMap<String, String> map = currentUser.getKeywords();
            String[] arr = new String[3];
            for(int i = 0; i < 3; i++) arr[i] = map.get(""+i);
            args.putStringArray("Keywords", arr);

            EditProfileFragment editProfileFragment = new EditProfileFragment();
            editProfileFragment.setArguments(args);
            replaceFragment(editProfileFragment);
        });
        btnManagePwFragment.setOnClickListener(v -> {
            ManagePwFragment managePwFragment = new ManagePwFragment();
            replaceFragment(managePwFragment);
        });
        btnSettingsFragment.setOnClickListener(v -> replaceFragment(new SettingsFragment()));

        changeFontSize();
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

    private void changeFontSize(){
        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        int size = fontPreference.getInt("FONT_SP",0);
        userName.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        userBio.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        btnEditProfileFragment.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        btnManagePwFragment.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        btnSettingsFragment.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}

