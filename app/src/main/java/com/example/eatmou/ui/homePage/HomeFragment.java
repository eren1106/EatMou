package com.example.eatmou.ui.homePage;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eatmou.model.Users;
import com.example.eatmou.ui.FoodParty.FoodPartyModel;
import com.example.eatmou.ui.homePage.userMatching.Adapter;
import com.example.eatmou.ui.homePage.userMatching.UserMatchingProfileFragment;
import com.example.eatmou.R;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView user_matching_list;
    List<Users> usersList = new ArrayList<>();

    //Current userID
    String currentUserID = FirebaseAuth.getInstance().getUid();

    //Firebase instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EventListener<QuerySnapshot> eventListener;
    ListenerRegistration listenerRegistration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        user_matching_list = view.findViewById(R.id.user_matching_list);

        //Set up the recycler view using adapter
        Adapter adapter = new Adapter(getContext(), usersList);
        user_matching_list.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        user_matching_list.setLayoutManager(gridLayoutManager);
        user_matching_list.setAdapter(adapter);

       eventListener = new EventListener<QuerySnapshot>() {
           @SuppressLint("NotifyDataSetChanged")
           @Override
           public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if (value != null && !value.isEmpty()) {
                   for (DocumentChange dc : value.getDocumentChanges()) {
                       if (dc.getType() == DocumentChange.Type.ADDED) {
                           Users users = Users.toObject(dc.getDocument().getData());
                           if(!users.getUserID().equals(currentUserID)) {
                               usersList.add(users);
                           }
                       }
                   }
               }
               if (error != null) {
//                    if(progressDialog.isShowing())
//                        progressDialog.dismiss();
                   Log.e("FireStore error", error.getMessage());
                   return;
               }
               adapter.notifyDataSetChanged();
           }
       };

        listenerRegistration = db.collection("users").addSnapshotListener(eventListener);
        return view;
    }

    private class UserViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, userIDTitle;
        ConstraintLayout user_item;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            name = itemView.findViewById(R.id.card_name);
            user_item = itemView.findViewById(R.id.user_item);
            userIDTitle = itemView.findViewById(R.id.userIDTile);

            user_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Share Preferences
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //Set username clicked into the share preference
                    editor.putString("USERNAME_SHARED_PREF", userIDTitle.getText().toString());
                    editor.apply();
                    Log.d("userID", userIDTitle.getText().toString());
                    //Switch to next fragment
                    Fragment fragment = new UserMatchingProfileFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frameLayout, fragment).commit();
                }
            });
        }
    }
}