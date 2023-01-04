package com.example.eatmou.ui.FoodParty;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eatmou.FirebaseMethods;
import com.example.eatmou.R;
import com.example.eatmou.UserModel;
import com.example.eatmou.ui.homePage.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class FoodPartyDetailActivity extends AppCompatActivity implements Serializable {

    UserModel currentUser;
    TextView title, organizer, location, date, time, joinedPersonNumber;
    ImageButton backBtn;
    Button deleteBtn, bottomBtn;

    FoodPartyModel foodPartyModel;
    FirebaseMethods firebaseMethods;

    int RESULT_OK = 6969;

    RecyclerView recyclerView;
    JoinedPersonRecyclerViewAdapter adapter;

    boolean joined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_party_detail);

        currentUser = MainActivity.user;
        foodPartyModel = (FoodPartyModel) getIntent().getSerializableExtra("FoodPartyObject"); //get data pass from previous activity/fragment
        joined = checkJoined(currentUser.getUserID());
        firebaseMethods = new FirebaseMethods();

        title = findViewById(R.id.TV_CreatePartyTitle);
        organizer = findViewById(R.id.TV_OrganizerText);
        location = findViewById(R.id.TV_LocationText);
        date = findViewById(R.id.TV_DateText);
        time = findViewById(R.id.TV_TimeText);
        backBtn = findViewById(R.id.B_BackBtn);
        joinedPersonNumber = findViewById(R.id.TV_JoinedPersonNumber);
        deleteBtn = findViewById(R.id.B_Delete);
        bottomBtn = findViewById(R.id.B_BottomBtn);

        setData();

        setDeleteBtnVisibility();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // exit this activity
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseMethods.deleteFoodParty(foodPartyModel.getId());
                finish();
            }
        });

        setBottomBtn();

        recyclerView = findViewById(R.id.RV_PersonList);
        adapter = new JoinedPersonRecyclerViewAdapter(this, foodPartyModel.getJoinedPersons());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDeleteBtnVisibility() {
        if(foodPartyModel.getOrganiserId().equals(currentUser.getUserID())) {
            deleteBtn.setVisibility(View.VISIBLE);
        }
        else{
            deleteBtn.setVisibility(View.GONE);
        }
    }

    private void setBottomBtn() {
        if(foodPartyModel.getOrganiserId().equals(currentUser.getUserID())) { // YOU CREATED THE PARTY
            bottomBtn.setText("Manage");
            bottomBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FoodPartyDetailActivity.this, CreateFoodPartyActivity.class);
                    intent.putExtra("FoodPartyObject", foodPartyModel);
                    startActivityForResult(intent, 1);
                }
            });
        }
        else if(joined){ // YOU JOINED THE PARTY
            bottomBtn.setText("Leave");
            bottomBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<JoinedPersonModel> tempList = foodPartyModel.getJoinedPersons();
                    for (int i = 0; i < tempList.size(); i++) {
                        if(tempList.get(i).getUserId().equals(currentUser.getUserID())){
                            tempList.remove(i);
                            break;
                        }
                    }
                    foodPartyModel.setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModel);
                    adapter.notifyDataSetChanged();
                    joinedPersonNumber.setText(foodPartyModel.getJoinedPersons().size() + "/" + foodPartyModel.getMaxParticipant());
                    joined = false;
                    setBottomBtn();
                }
            });
        }
        else { // YOU HAVEN'T JOIN THE PARTY
            bottomBtn.setText("Join");
            bottomBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    ArrayList<JoinedPersonModel> tempList = foodPartyModel.getJoinedPersons();
                    JoinedPersonModel me = new JoinedPersonModel(currentUser.getUserID(), currentUser.getUsername(), currentUser.getProfilePicUrl()); // R.drawable.eren juz temporary image
                    tempList.add(me);
                    foodPartyModel.setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModel);
                    adapter.notifyDataSetChanged();
                    joinedPersonNumber.setText(foodPartyModel.getJoinedPersons().size() + "/" + foodPartyModel.getMaxParticipant());
                    joined = true;
                    setBottomBtn();
                }
            });
        }
    }

    private boolean checkJoined(String userId) {
        for(JoinedPersonModel joinedPerson : foodPartyModel.getJoinedPersons()) {
            if(joinedPerson.getUserId().equals(userId)) return true;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                foodPartyModel = (FoodPartyModel) data.getSerializableExtra("UpdatedFoodPartyObject");
                setData();
            }
        }
    }

    private void setData() {
        title.setText(foodPartyModel.getTitle());
        organizer.setText(foodPartyModel.getOrganiserName());
        location.setText(foodPartyModel.getLocation());
        date.setText(foodPartyModel.getDateText());
        time.setText(foodPartyModel.getStartTimeText() + " - " + foodPartyModel.getEndTimeText());
        joinedPersonNumber.setText(foodPartyModel.getJoinedPersons().size() + "/" + foodPartyModel.getMaxParticipant());
    }
}