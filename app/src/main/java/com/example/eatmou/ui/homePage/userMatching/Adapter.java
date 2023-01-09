package com.example.eatmou.ui.homePage.userMatching;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.example.eatmou.model.Users;
import com.example.eatmou.ui.FoodParty.FoodPartyRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    static private Context context;
    private List<Users> usersList;
    String currentUserID = FirebaseAuth.getInstance().getUid();

    public Adapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_matching_card, parent, false);
        return new Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Users users = usersList.get(position);
        if(!users.getUserID().equals(currentUserID)) {
            holder.name.setText(users.getUsername());
            holder.userIDTitle.setText(users.getUserID());
            Glide.with(context).load(users.getProfilePicUrl()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, userIDTitle;
        ConstraintLayout user_item;
        public MyViewHolder(@NonNull View itemView) {
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
                            .getDefaultSharedPreferences((AppCompatActivity)context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //Set username clicked into the share preference
                    editor.putString("USERNAME_SHARED_PREF", userIDTitle.getText().toString());
                    editor.apply();
                    Log.d("userID", userIDTitle.getText().toString());
                    //Switch to next fragment
                    Fragment fragment = new UserMatchingProfileFragment();
                    ((AppCompatActivity)context).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frameLayout, fragment).commit();
                }
            });

            changeFontSize();
        }

        private void changeFontSize(){
            SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(context);
            int size = fontPreference.getInt("FONT_SP",0);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, size+2);
        }
    }
}
