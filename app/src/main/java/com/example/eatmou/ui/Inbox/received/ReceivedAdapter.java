package com.example.eatmou.ui.Inbox.received;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.model.Invitation;
import com.example.eatmou.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReceivedAdapter extends RecyclerView.Adapter<ReceivedAdapter.MyViewHolder> {

    private ArrayList<Invitation> invitationList;
    private FirebaseFirestore db;

    public ReceivedAdapter(ArrayList<Invitation> invitationList){
        this.invitationList = invitationList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTxt;
        private TextView locationTxt;
        private TextView dateTxt;
        private TextView startTimeTxt;
        private TextView endTimeTxt;

//        private ImageView cardView_arrow;

        private Button acceptBtn;
        private Button declineBtn;
        private ImageView empty_image;
        private TextView empty_text;

        LinearLayout cardView_linearLayout;
        RelativeLayout cardView_expandable;
        RelativeLayout cardView_mainBar;


        public MyViewHolder(final View view){
            super(view);
            usernameTxt = view.findViewById(R.id.usernameTxt);
            locationTxt = view.findViewById(R.id.locationTxt);
            dateTxt = view.findViewById(R.id.dateTxt);
            startTimeTxt = view.findViewById(R.id.startTimeTxt);
            endTimeTxt = view.findViewById(R.id.endTimeTxt);

//            cardView_arrow = view.findViewById(R.id.cardView_arrow);

            acceptBtn = view.findViewById(R.id.acceptBtn);
            declineBtn = view.findViewById(R.id.declineBtn);

            cardView_mainBar = view.findViewById(R.id.cardView_mainBar);
            cardView_linearLayout = view.findViewById(R.id.cardView_linearLayout);
            cardView_expandable = view.findViewById(R.id.cardView_expandable);

//            empty_image = view.findViewById(R.id.empty_image);
//            empty_text = view.findViewById(R.id.empty_text);

            cardView_linearLayout.setOnClickListener( v -> {
                Invitation invitation =  invitationList.get(getAdapterPosition());
                invitation.setExpandable(!invitation.isExpandable());
                notifyItemChanged(getAdapterPosition());
//                cardView_arrow.animate().rotation(!invitation.isExpandable()?90:0).start();
            });

            acceptBtn.setOnClickListener( v -> {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Accept Invitation")
                        .setMessage("Are you sure you want to accept this invitation?")
                        .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                            Invitation invitation =  invitationList.get(getAdapterPosition());
                            invitation.setAccepted(true);
                            //
                            //
                            invitationList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            //need to update database or the data will reappear
                            db.collection("Invitations")
                                    .document(invitation.getInvitationID())
                                    .update("Status", "Accepted");

                            Toast.makeText(view.getContext(), "Check out your joined invitations!", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            });

            declineBtn.setOnClickListener( v -> {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Decline Invitation")
                        .setMessage("Are you sure you want to decline this invitation?\nThis action cannot be undone!")
                        .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                            Invitation invitation =  invitationList.get(getAdapterPosition());
                            invitation.setDeclined(true);
                            //
                            //
                            invitationList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            //need to update database or the data will reappear
                            db.collection("Invitations")
                                    .document(invitation.getInvitationID())
                                    .update("Status", "Declined");

                            Toast.makeText(view.getContext(), "You have declined an invitation", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            });

            usernameTxt.setOnLongClickListener(v -> {
                Invitation invitation = invitationList.get(getAdapterPosition());
                String name = invitation.getInvitedID() + "'s ";
                Toast.makeText(view.getContext(), "View " + name + "profile", Toast.LENGTH_SHORT).show();
                return true;
            });
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
        DocumentReference docRef = db.collection("Users").document(invitation.getOrganiserID());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("Username");
                        holder.usernameTxt.setText(preText + name);
                    }
                    else Log.d(TAG, "No such document");
                }
                else Log.d(TAG, "get failed with ", task.getException());
            }
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

}
