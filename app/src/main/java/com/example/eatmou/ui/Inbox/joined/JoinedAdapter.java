package com.example.eatmou.ui.Inbox.joined;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.example.eatmou.model.Invitation;
import com.example.eatmou.ui.Inbox.InboxUserProfileFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class JoinedAdapter extends RecyclerView.Adapter<JoinedAdapter.MyViewHolder> {

    private ArrayList<Invitation> invitationList;
    private FirebaseFirestore db;
    private String userID;
    private Context context;

    public JoinedAdapter(ArrayList<Invitation> invitationList, String userID, Context context) {
        this.invitationList = invitationList;
        this.userID = userID;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView userImgView;
        private TextView usernameTxt;
        private TextView locationTxt;
        private TextView dateTxt;
        private TextView startTimeTxt;
        private TextView endTimeTxt;
        private String InboxUserID;

        LinearLayout cardView_linearLayout;
        RelativeLayout cardView_expandable;
        RelativeLayout cardView_mainBar;

        public MyViewHolder(final View view){
            super(view);
            userImgView = view.findViewById(R.id.userImgView);
            usernameTxt = view.findViewById(R.id.usernameTxt);
            locationTxt = view.findViewById(R.id.locationTxt);
            dateTxt = view.findViewById(R.id.dateTxt);
            startTimeTxt = view.findViewById(R.id.startTimeTxt);
            endTimeTxt = view.findViewById(R.id.endTimeTxt);

            cardView_mainBar = view.findViewById(R.id.cardView_mainBar);
            cardView_linearLayout = view.findViewById(R.id.cardView_linearLayout);
            cardView_expandable = view.findViewById(R.id.cardView_expandable);

            cardView_linearLayout.setOnClickListener( v -> {
                Invitation invitation =  invitationList.get(getAdapterPosition());
                invitation.setExpandable(!invitation.isExpandable());
                notifyItemChanged(getAdapterPosition());
            });

            userImgView.setOnClickListener(v -> {
                Fragment fragment = new InboxUserProfileFragment();
                Bundle args = new Bundle();
                args.putString("InboxUserID", InboxUserID);
                args.putString("FragmentID", "JoinedFragment");
                fragment.setArguments(args);

                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout,fragment);
                fragmentTransaction.commit();
            });
        }
    }

    @NonNull
    @Override
    public JoinedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View invitationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_item, parent, false);
        return new JoinedAdapter.MyViewHolder(invitationView);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedAdapter.MyViewHolder holder, int position) {
        //
        String preText;
        Invitation invitation = invitationList.get(position);

        db = FirebaseFirestore.getInstance();

        DocumentReference docRef;
        if(invitation.getInvitedID().equals(userID)){
            preText = "Invitation from ";
            docRef = db.collection("users").document(invitation.getOrganiserID());
        }
        else {
            preText = "You invited ";
            docRef = db.collection("users").document(invitation.getInvitedID());
        }
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = document.getString("username");
                    String userID = document.getString("userID");
                    holder.usernameTxt.setText(preText + name);
                    holder.InboxUserID = userID;

                    String profilePicUrl = document.getString("profilePicUrl");
                    Glide.with(context).load(profilePicUrl).into(holder.userImgView);
                } else  Log.d(TAG, "No such document");
            } else Log.d(TAG, "get failed with ", task.getException());
        });

        holder.locationTxt.setText(invitation.getLocation());
        holder.dateTxt.setText(invitation.getDateText());
        holder.startTimeTxt.setText(invitation.getStartTimeText());
        holder.endTimeTxt.setText(invitation.getEndTimeText());

        boolean isExpandable = invitationList.get(position).isExpandable();
        holder.cardView_expandable.setVisibility(isExpandable? View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return invitationList.size();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
