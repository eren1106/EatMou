package com.example.eatmou.ui.FoodParty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eatmou.FirebaseMethods;
import com.example.eatmou.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class FoodPartyDetailActivity extends AppCompatActivity implements Serializable {

    TextView title, organizer, location, date, time, joinedPersonNumber;
    ImageButton backBtn;
    Button deleteBtn, bottomBtn;

    FoodPartyModel foodPartyModel;
    FirebaseMethods firebaseMethods;

    int RESULT_OK = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_party_detail);

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

        foodPartyModel = (FoodPartyModel) getIntent().getSerializableExtra("FoodPartyObject"); //get data pass from previous activity/fragment

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

        RecyclerView recyclerView = findViewById(R.id.RV_PersonList);
        JoinedPersonRecyclerViewAdapter adapter = new JoinedPersonRecyclerViewAdapter(this, foodPartyModel.getJoinedPersons());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setDeleteBtnVisibility() {
        if(foodPartyModel.getOrganiserId().equals("myid")) {
            deleteBtn.setVisibility(View.VISIBLE);
        }
        else{
            deleteBtn.setVisibility(View.GONE);
        }
    }

    private void setBottomBtn() {
        if(foodPartyModel.getOrganiserId().equals("myid")) {
            bottomBtn.setText("Manage");
            bottomBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FoodPartyDetailActivity.this, CreateFoodPartyActivity.class);
                    intent.putExtra("FoodPartyObject", foodPartyModel);
                    startActivityForResult(intent, 1);
//                    FoodPartyDetailActivity.this.startActivity(intent);
                }
            });
        }
        else{
            bottomBtn.setText("Join");
            bottomBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // join food party
                }
            });
        }

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
        organizer.setText(foodPartyModel.getOrganiserId());
        location.setText(foodPartyModel.getLocation());
        date.setText(foodPartyModel.getDateText());
        time.setText(foodPartyModel.getStartTimeText() + " - " + foodPartyModel.getEndTimeText());
        joinedPersonNumber.setText(foodPartyModel.getJoinedPersons().size() + "/9");
    }
}