package com.example.eatmou.ui.FoodParty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.FirebaseMethods;
import com.example.eatmou.R;
import com.example.eatmou.UserModel;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FoodPartyRecyclerViewAdapter extends RecyclerView.Adapter<FoodPartyRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<FoodPartyModel> foodPartyModels;
//    FoodPartyModel foodPartyModel;
    UserModel currentUser = MainActivity.user;
    FirebaseMethods firebaseMethods;

    private OnCardListener onCardListener;

    public FoodPartyRecyclerViewAdapter(Context context, ArrayList<FoodPartyModel> foodPartyModels, OnCardListener onCardListener) {
        this.context = context;
        this.foodPartyModels = foodPartyModels;
        this.onCardListener = onCardListener;
    }

    @NonNull
    @Override
    public FoodPartyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_party_card, parent, false);
        return new FoodPartyRecyclerViewAdapter.MyViewHolder(view, this.onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPartyRecyclerViewAdapter.MyViewHolder holder, int position) {
        firebaseMethods = new FirebaseMethods();
        int currentPosition = holder.getAdapterPosition();
//        foodPartyModel = foodPartyModels.get(currentPosition);
        holder.title.setText(foodPartyModels.get(currentPosition).getTitle());
        holder.organizer.setText(foodPartyModels.get(currentPosition).getOrganiserName());
        holder.location.setText(foodPartyModels.get(currentPosition).getLocation());
        holder.date.setText(foodPartyModels.get(currentPosition).getDateText());
        holder.time.setText(foodPartyModels.get(currentPosition).getStartTimeText() + " - " + foodPartyModels.get(currentPosition).getEndTimeText());
        holder.personNumber.setText(foodPartyModels.get(currentPosition).getJoinedPersons().size() + "/" + foodPartyModels.get(currentPosition).getMaxParticipant());

        setCardButton(holder, currentPosition);
    }

    private boolean checkJoined(String userId, int currentPosition) {
        for(JoinedPersonModel joinedPerson : foodPartyModels.get(currentPosition).getJoinedPersons()) {
            if(joinedPerson.getUserId().equals(userId)) return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return foodPartyModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // grabbing views from food_party_card layout file
        // kinda like in onCreate method

        TextView title, organizer, location, date, time, personNumber;
        Button cardBtn;
        OnCardListener onCardListener;

        public MyViewHolder(@NonNull View itemView, OnCardListener onCardListener) {
            super(itemView);

            title = itemView.findViewById(R.id.TV_CreatePartyTitle);
            organizer = itemView.findViewById(R.id.TV_OrganizerText);
            location = itemView.findViewById(R.id.TV_LocationText);
            date = itemView.findViewById(R.id.TV_DateText);
            time = itemView.findViewById(R.id.TV_TimeText);
            personNumber = itemView.findViewById(R.id.TV_JoinedPersonNumber);
            cardBtn = itemView.findViewById(R.id.B_CardBtn);

            this.onCardListener = onCardListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener{
        void onCardClick(int position);
    }

    private void setCardButton (FoodPartyRecyclerViewAdapter.MyViewHolder holder, int currentPosition) {
        if(foodPartyModels.get(currentPosition).getOrganiserId().equals(currentUser.getUserID())) {
            holder.cardBtn.setText("Manage");
            holder.cardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, CreateFoodPartyActivity.class);
                    intent.putExtra("FoodPartyObject", foodPartyModels.get(currentPosition));
                    // extras
                    context.startActivity(intent);
                }
            });
        }
        else if(checkJoined(currentUser.getUserID(), currentPosition)){
            holder.cardBtn.setText("Leave");
            holder.cardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<JoinedPersonModel> tempList = foodPartyModels.get(currentPosition).getJoinedPersons();
                    for (int i = 0; i < tempList.size(); i++) {
                        if(tempList.get(i).getUserId().equals(currentUser.getUserID())){
                            tempList.remove(i);
                            break;
                        }
                    }
                    foodPartyModels.get(currentPosition).setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModels.get(currentPosition));
                    setCardButton(holder, currentPosition);
                }
            });
        }
        else {
            holder.cardBtn.setText("Join");
            holder.cardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<JoinedPersonModel> tempList = foodPartyModels.get(currentPosition).getJoinedPersons();
                    JoinedPersonModel me = new JoinedPersonModel(currentUser.getUserID(), currentUser.getUsername(), R.drawable.eren); // R.drawable.eren juz temporary image
                    tempList.add(me);
                    foodPartyModels.get(currentPosition).setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModels.get(currentPosition));
                    setCardButton(holder, currentPosition);
                }
            });
        }
    }
}

