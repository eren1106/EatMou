package com.example.eatmou.ui.ProfilePage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.example.eatmou.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class ProfilePage extends Fragment {

    Button btnEditProfileFragment, btnManagePwFragment, btnSettingsFragment;
    View view;

    private ImageView profileImage;
    private ImageView profileBgImage;
    private TextView username;
    private TextView bio;
    private Users currentUser;

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

        profileBgImage = view.findViewById(R.id.bg_img);
        profileImage = view.findViewById(R.id.profile_img);
        username = view.findViewById(R.id.user_name);
        bio = view.findViewById(R.id.bio_info);
        currentUser = new Users();

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    currentUser = document.toObject(Users.class);
                    Log.d("User document", "DocumentSnapshot data: " + document.getData());

                    Glide.with(view).load(currentUser.getProfilePicUrl()).into(profileImage);
                    Glide.with(view).load(currentUser.getProfileBgPicUrl()).into(profileBgImage);
                    username.setText(currentUser.getUsername());
                    bio.setText(currentUser.getBio());

                } else Log.d("User document", "No such document");
            } else Log.d("User document", "get failed with ", task.getException());
    });

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

