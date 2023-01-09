package com.example.eatmou.ui.Inbox.received;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
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

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.MyViewHolder> {

    private ArrayList<Invitation> invitationList;
    private FirebaseFirestore db;
    private String userID;
    private Context context;

    public ReceivedAdapter(ArrayList<Invitation> invitationList, String userID, Context context) {
        this.invitationList = invitationList;
        this.userID = userID;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImgView;
        private TextView usernameTxt;
        private TextView locationTxt;
        private TextView dateTxt;
        private TextView startTimeTxt;
        private TextView endTimeTxt;

        private Button acceptBtn;
        private Button declineBtn;
        private String InboxUserID;

        private TextView location, date, startTime, endTime;

        LinearLayout cardView_linearLayout;
        RelativeLayout cardView_expandable;
        RelativeLayout cardView_mainBar;


        public MyViewHolder(final View view) {
            super(view);
            userImgView = view.findViewById(R.id.userImgView);
            usernameTxt = view.findViewById(R.id.usernameTxt);
            locationTxt = view.findViewById(R.id.locationTxt);
            dateTxt = view.findViewById(R.id.dateTxt);
            startTimeTxt = view.findViewById(R.id.startTimeTxt);
            endTimeTxt = view.findViewById(R.id.endTimeTxt);

            location = view.findViewById(R.id.location);
            date = view.findViewById(R.id.date);
            startTime = view.findViewById(R.id.startTime);
            endTime = view.findViewById(R.id.endTime);

            acceptBtn = view.findViewById(R.id.acceptBtn);
            declineBtn = view.findViewById(R.id.declineBtn);

            cardView_mainBar = view.findViewById(R.id.cardView_mainBar);
            cardView_linearLayout = view.findViewById(R.id.cardView_linearLayout);
            cardView_expandable = view.findViewById(R.id.cardView_expandable);

            cardView_linearLayout.setOnClickListener(v -> {
                Invitation invitation = invitationList.get(getAdapterPosition());
                invitation.setExpandable(!invitation.isExpandable());
                notifyItemChanged(getAdapterPosition());
            });

            acceptBtn.setOnClickListener(v -> {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Accept Invitation")
                        .setMessage("Are you sure you want to accept this invitation?")
                        .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                            Invitation invitation = invitationList.get(getAdapterPosition());
                            invitation.setAccepted(true);
                            invitationList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            db.collection("Invitations")
                                    .document(invitation.getInvitationID())
                                    .update("Status", "Accepted");

                            Toast.makeText(view.getContext(), "Check out your joined invitations!", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            });

            declineBtn.setOnClickListener(v -> {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Decline Invitation")
                        .setMessage("Are you sure you want to decline this invitation?\nThis action cannot be undone!")
                        .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                            Invitation invitation = invitationList.get(getAdapterPosition());
                            invitation.setDeclined(true);
                            invitationList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            db.collection("Invitations")
                                    .document(invitation.getInvitationID())
                                    .update("Status", "Declined");

                            Toast.makeText(view.getContext(), "You have declined an invitation", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            });

            usernameTxt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Invitation invitation = invitationList.get(getAdapterPosition());
                    String name = invitation.getInvitedID() + "'s ";
                    Toast.makeText(view.getContext(), "View " + name + "profile", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


            userImgView.setOnClickListener(view1 -> {
                Fragment fragment = new InboxUserProfileFragment();
                Bundle args = new Bundle();
                args.putString("InboxUserID", InboxUserID);
                args.putString("FragmentID", "ReceivedFragment");
                fragment.setArguments(args);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            });

            changeFontSize();
        }

        private void changeFontSize(){
            SharedPreferences fontPreference = PreferenceManager.getDefaultSharedPreferences(context);
            int size = fontPreference.getInt("FONT_SP",0);
            usernameTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            locationTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            dateTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            startTimeTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            endTimeTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            location.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            date.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            startTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            endTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View invitationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_item, parent, false);
            return new MyViewHolder(invitationView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String preText = "Invitation from ";
            Invitation invitation = invitationList.get(position);

            db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(invitation.getOrganiserID());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("username");
                        holder.usernameTxt.setText(preText + name);

                        String profilePicUrl = document.getString("profilePicUrl");
                        Glide.with(context).load(profilePicUrl).into(holder.userImgView);
                    } else Log.d(TAG, "No such document");
                } else Log.d(TAG, "get failed with ", task.getException());
            });
//        System.out.println(invitation);
//        holder.usernameTxt.setText(preText + invitation.getOrganiserName());
            holder.locationTxt.setText(invitation.getLocation());
            holder.dateTxt.setText(invitation.getDateText());
            holder.startTimeTxt.setText(invitation.getStartTimeText());
            holder.endTimeTxt.setText(invitation.getEndTimeText());
            holder.InboxUserID = invitation.getOrganiserID();

            boolean isExpandable = invitationList.get(position).isExpandable();
            holder.cardView_expandable.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
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
