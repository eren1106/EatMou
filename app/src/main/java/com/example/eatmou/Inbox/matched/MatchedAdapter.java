package com.example.eatmou.Inbox.matched;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.Inbox.Invitation;
import com.example.eatmou.R;

import java.util.ArrayList;


public class MatchedAdapter extends RecyclerView.Adapter<MatchedAdapter.MyViewHolder> {

    //need to change to user class
    private ArrayList<Invitation> invitationList;

    public MatchedAdapter(ArrayList<Invitation> invitationList) {
        this.invitationList = invitationList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTxt;

        RelativeLayout cardView_mainBar;

        public MyViewHolder(final View view){
            super(view);
            usernameTxt = view.findViewById(R.id.usernameTxt);
            cardView_mainBar = view.findViewById(R.id.cardView_mainBar);

            cardView_mainBar.setOnClickListener(v -> {
                Invitation invitation = invitationList.get(getAdapterPosition());
                String name = invitation.getUsername() + "'s ";
                Toast.makeText(view.getContext(), "View " + name + "profile", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View invitationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.matched_item, parent, false);
        return new MyViewHolder(invitationView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Invitation invitation = invitationList.get(position);
        holder.usernameTxt.setText(invitation.getUsername());
    }

    @Override
    public int getItemCount() {
        return invitationList.size();
    }
}
