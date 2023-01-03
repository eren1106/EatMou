package com.example.eatmou.ui.homePage;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.example.eatmou.model.Users;
import com.example.eatmou.ui.homePage.userMatching.UserMatchingProfileFragment;
import com.example.eatmou.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {
    RecyclerView user_matching_list;
    FirestoreRecyclerAdapter<Users, UserViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Testing","Testing");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        user_matching_list = view.findViewById(R.id.user_matching_list);

        //Get data from fireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Query
        Query query = db.collection("users");

        //RecyclerOption
        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<Users, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.user_matching_card, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model) {
                holder.name.setText(model.getUsername());
                Glide.with(container).load(model.getProfilePicUrl()).into(holder.image);
                holder.userIDTitle.setText(model.getUserID());
            }
        };

        user_matching_list.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        user_matching_list.setLayoutManager(gridLayoutManager);
        user_matching_list.setAdapter(adapter);
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

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}