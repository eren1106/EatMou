package com.example.eatmou.ui.homePage.userMatching;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.example.eatmou.model.Users;
import com.example.eatmou.ui.homePage.InviteDialog.InviteDialog;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserMatchingProfileFragment extends Fragment {
    ImageView backBtn, user_image;
    FloatingActionButton inviteBtn;
    TextView user_title, user_bio, user_location, info1, info2, info3;
    TextView about_me, location, basicinfo;
    Users user = new Users();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_matching_profile, container, false);

        about_me = view.findViewById(R.id.about_me);
        location = view.findViewById(R.id.user_location_title);
        basicinfo = view.findViewById(R.id.user_info_title);

        // Inflate the layout for this fragment
        backBtn = view.findViewById(R.id.back_Btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Back button not user friendly
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        //Get username from share preference from adapter for particular user
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        String tap_Username = sharedPreferences.getString("USERNAME_SHARED_PREF","");

        //Set the view of fragment by fetching data from firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(tap_Username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(Users.class);
                        Log.d("User document", "DocumentSnapshot data: " + document.getData());

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
                        info1.setText(document.getString("Keywords.0"));
                        info2.setText(document.getString("Keywords.1"));
                        info3.setText(document.getString("Keywords.2"));

                        changeFontSize();
                    } else {
                        Log.d("User document", "No such document");
                    }
                } else {
                    Log.d("User document", "get failed with ", task.getException());
                }
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
        SharedPreferences namePref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = namePref.edit();
        editor.putString("INV_NAME", user.getUsername());
        editor.apply();

        InviteDialog inviteDialog = new InviteDialog();
        inviteDialog.show(getActivity().getSupportFragmentManager(), "Invite Dialog");
    }

    private void changeFontSize(){
        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        int size = fontPreference.getInt("FONT_SP",0)+4;
        user_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        user_bio.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        user_location.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        info1.setTextSize(TypedValue.COMPLEX_UNIT_SP, size-4);
        info2.setTextSize(TypedValue.COMPLEX_UNIT_SP, size-4);
        info3.setTextSize(TypedValue.COMPLEX_UNIT_SP, size-4);
        about_me.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        location.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        basicinfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}