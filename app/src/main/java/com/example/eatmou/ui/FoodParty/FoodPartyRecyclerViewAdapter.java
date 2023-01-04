package com.example.eatmou.ui.FoodParty;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FoodPartyRecyclerViewAdapter extends RecyclerView.Adapter<FoodPartyRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<FoodPartyModel> foodPartyModels;
    UserModel currentUser = MainActivity.user;
    FirebaseMethods firebaseMethods;
    int currentPosition;
    String organiserName;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodPartyRecyclerViewAdapter.MyViewHolder holder, int position) {
        firebaseMethods = new FirebaseMethods();
        currentPosition = holder.getAdapterPosition();
        fetchOrganiserName(holder);
        holder.title.setText(foodPartyModels.get(currentPosition).getTitle());
        holder.location.setText(foodPartyModels.get(currentPosition).getLocation());
        holder.date.setText(foodPartyModels.get(currentPosition).getDateText());
        holder.time.setText(foodPartyModels.get(currentPosition).getStartTimeText() + " - " + foodPartyModels.get(currentPosition).getEndTimeText());
        holder.personNumber.setText(foodPartyModels.get(currentPosition).getJoinedPersons().size() + "/" + foodPartyModels.get(currentPosition).getMaxParticipant());

        setCardButton(holder, currentPosition);
    }

    private boolean checkJoined(int currentPosition) {
        for(String joinedPerson : foodPartyModels.get(currentPosition).getJoinedPersons()) {
            if(joinedPerson.equals(currentUser.getUserID())) return true;
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
        else if(checkJoined(currentPosition)){
            holder.cardBtn.setText("Leave");
            holder.cardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> tempList = foodPartyModels.get(currentPosition).getJoinedPersons();
                    for (int i = 0; i < tempList.size(); i++) {
                        if(tempList.get(i).equals(currentUser.getUserID())){
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
                    ArrayList<String> tempList = foodPartyModels.get(currentPosition).getJoinedPersons();
                    tempList.add(currentUser.getUserID());
                    foodPartyModels.get(currentPosition).setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModels.get(currentPosition));
                    setCardButton(holder, currentPosition);
                }
            });
        }
    }

    private void fetchOrganiserName (FoodPartyRecyclerViewAdapter.MyViewHolder holder) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").document(foodPartyModels.get(currentPosition).getOrganiserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel tempUser = UserModel.toObject(document.getData());
                        holder.organizer.setText(tempUser.getUsername());
                    } else {
                        // Document with the specified ID does not exist
                    }
                } else {
                    // Task failed
                }
            }
        });
    }
}

