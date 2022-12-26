package com.example.eatmou.Inbox.sent;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.Inbox.Invitation;
import com.example.eatmou.R;

import java.util.ArrayList;


public class SentAdapter extends RecyclerView.Adapter<SentAdapter.MyViewHolder> {

    private ArrayList<Invitation> invitationList;

    public SentAdapter(ArrayList<Invitation> invitationList){
        this.invitationList = invitationList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTxt;
        private TextView locationTxt;
        private TextView dateTxt;
        private TextView timeTxt;

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
            timeTxt = view.findViewById(R.id.timeTxt);

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
                String name = invitation.getUsername() + "'s ";
                Toast.makeText(view.getContext(), "View " + name + "profile", Toast.LENGTH_SHORT).show();
                return true;
            });


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
        holder.usernameTxt.setText(preText + invitation.getUsername());
        holder.locationTxt.setText(invitation.getLocation());
        holder.dateTxt.setText(invitation.getDate().toString());
        holder.timeTxt.setText(invitation.getTime().toString());

        String status = "Pending";
        if(invitation.isAccepted()) {
            status = "Accepted";
            holder.statusTxt.setTextColor(Color.parseColor("#00FF00"));
        }
        else if(invitation.isDeclined()) {
            status = "Declined";
            holder.statusTxt.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            holder.statusTxt.setTextColor(Color.parseColor("#000000"));
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
