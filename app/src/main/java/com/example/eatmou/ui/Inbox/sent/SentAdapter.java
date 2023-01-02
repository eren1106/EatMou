package com.example.eatmou.ui.Inbox.sent;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class SentAdapter extends RecyclerView.Adapter<SentAdapter.MyViewHolder> {

    private ArrayList<Invitation> invitationList;
    private FirebaseFirestore db;

    public SentAdapter(ArrayList<Invitation> invitationList){
        this.invitationList = invitationList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTxt;
        private TextView locationTxt;
        private TextView dateTxt;
        private TextView startTimeTxt;
        private TextView endTimeTxt;
        private Button cancelBtn;

        private TextView statusTxt;

//        private ImageView cardView_arrow;

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
            cancelBtn = view.findViewById(R.id.cancelBtn);

            statusTxt = view.findViewById(R.id.statusTxt);

//            cardView_arrow = view.findViewById(R.id.cardView_arrow);

            cardView_mainBar = view.findViewById(R.id.cardView_mainBar);
            cardView_linearLayout = view.findViewById(R.id.cardView_linearLayout);
            cardView_expandable = view.findViewById(R.id.cardView_expandable);

            cardView_linearLayout.setOnClickListener( v -> {
                Invitation invitation =  invitationList.get(getAdapterPosition());
                invitation.setExpandable(!invitation.isExpandable());
                notifyItemChanged(getAdapterPosition());
//                cardView_arrow.animate().rotation(!invitation.isExpandable()?90:0).start();
            });

            usernameTxt.setOnLongClickListener(v -> {
                Invitation invitation = invitationList.get(getAdapterPosition());
                String name = invitation.getInvitedID() + "'s ";
                Toast.makeText(view.getContext(), "View " + name + "profile", Toast.LENGTH_SHORT).show();
                return true;
            });
            cancelBtn.setOnClickListener( v -> new AlertDialog.Builder(view.getContext())
                    .setTitle("Cancel Invitation")
                    .setMessage("Are you sure you want to cancel this invitation?\nThis action cannot be undone!")
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                        Invitation invitation =  invitationList.get(getAdapterPosition());
                        invitation.setCanceled(true);
                        invitation.setStatus("Canceled");
                        db.collection("Invitations")
                                .document(invitation.getInvitationID())
                                .update("Status", "Canceled");
                        cancelBtn.setEnabled(false);
                        notifyItemChanged(getAdapterPosition());
                        Toast.makeText(view.getContext(), "You have canceled your invitation", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View invitationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_item, parent, false);
        return new MyViewHolder(invitationView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String preText = "Invited ";
        Invitation invitation = invitationList.get(position);

        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(invitation.getInvitedID());
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

        //no need is accepted/is declined boolean
        String status = invitation.getStatus();
//        if (invitation.isCanceled()){
//            status = "Canceled";
//            holder.statusTxt.setTextColor(Color.parseColor("#9FA3A6"));
//            holder.cancelBtn.setEnabled(false);
//        }
//        else if(invitation.isDeclined()) {
//            status = "Declined";
//            holder.statusTxt.setTextColor(Color.parseColor("#FF0000"));
//            holder.cancelBtn.setEnabled(false);
//        }
//        else if(invitation.isAccepted()) {
//            status = "Accepted";
//            holder.statusTxt.setTextColor(Color.parseColor("#00FF00"));
//        }
//        else{
//            holder.statusTxt.setTextColor(Color.parseColor("#000000"));
//        }
        switch (status) {
            case "Canceled":
                holder.statusTxt.setTextColor(Color.parseColor("#9FA3A6"));
                holder.cancelBtn.setEnabled(false);
                break;
            case "Declined":
                holder.statusTxt.setTextColor(Color.parseColor("#FF0000"));
                holder.cancelBtn.setEnabled(false);
                break;
            case "Accepted":
                holder.statusTxt.setTextColor(Color.parseColor("#00FF00"));
                holder.cancelBtn.setEnabled(true);
                break;
            default:
                holder.statusTxt.setTextColor(Color.parseColor("#000000"));
                holder.cancelBtn.setEnabled(true);
                break;
        }
        holder.statusTxt.setText(status);

        boolean isExpandable = invitationList.get(position).isExpandable();
        holder.cardView_expandable.setVisibility(isExpandable? View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return invitationList.size();
    }
}
