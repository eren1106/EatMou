package com.example.eatmou.ui.Inbox;


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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.example.eatmou.model.Users;
import com.example.eatmou.ui.Inbox.joined.JoinedFragment;
import com.example.eatmou.ui.Inbox.received.ReceivedFragment;
import com.example.eatmou.ui.Inbox.sent.SentFragment;
import com.example.eatmou.ui.homePage.InviteDialog.InviteDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class InboxUserProfileFragment extends Fragment {
    ImageView backBtn, user_image;
    FloatingActionButton inviteBtn;
    TextView user_title, user_bio, user_location, info1, info2, info3;
    TextView about_me, location, basicinfo;
    Users user = new Users();
    String InboxUserID;
    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_matching_profile, container, false);
        // Inflate the layout for this fragment

        about_me = view.findViewById(R.id.about_me);
        location = view.findViewById(R.id.user_location_title);
        basicinfo = view.findViewById(R.id.user_info_title);

        args = this.getArguments();
        backBtn = view.findViewById(R.id.back_Btn);
        backBtn.setOnClickListener(view12 -> {
            Bundle args2 = new Bundle();
            Fragment previousFragment = null;

            switch (args.getString("FragmentID")){
                case "ReceivedFragment":
                    previousFragment = new ReceivedFragment();
                    args2.putString("button", "1");
                    break;
                case "SentFragment":
                    previousFragment = new SentFragment();
                    args2.putString("button", "2");
                    break;
                case "JoinedFragment":
                    previousFragment = new JoinedFragment();
                    args2.putString("button", "3");
                    break;
            }
            InboxFragment inboxFragment = new InboxFragment(previousFragment);
            inboxFragment.setArguments(args2);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout,inboxFragment);
            fragmentTransaction.commit();
        });

        if(args != null) InboxUserID = args.getString("InboxUserID");

        //Set the view of fragment by fetching data from firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(InboxUserID);
        docRef.get().addOnCompleteListener(task -> {
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
        });


        //Add logic for invite button
        inviteBtn = view.findViewById(R.id.inviteBtn);
        inviteBtn.setOnClickListener(view1 -> openDialog());

        return view;
    }

    public void openDialog() {
        InviteDialog inviteDialog = new InviteDialog();
        inviteDialog.show(getActivity().getSupportFragmentManager(), "Invite Dialog");
    }

    private void changeFontSize(){

        SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(getContext());
        int size = fontPreference.getInt("FONT_SP",0)+4;
        user_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        user_bio.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        user_location.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        info1.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        info2.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        info3.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        about_me.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        location.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        basicinfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}