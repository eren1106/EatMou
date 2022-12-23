package com.example.eatmou.FoodParty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eatmou.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class FoodPartyDetailActivity extends AppCompatActivity implements Serializable {

    TextView title, organizer, location, date, time, joinedPersonNumber;
    ImageButton backBtn;


    FoodPartyModel foodPartyModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_party_detail);

        title = findViewById(R.id.TV_CreatePartyTitle);
        organizer = findViewById(R.id.TV_OrganizerText);
        location = findViewById(R.id.TV_LocationText);
        date = findViewById(R.id.TV_DateText);
        time = findViewById(R.id.TV_TimeText);
        backBtn = findViewById(R.id.B_BackBtn);
        joinedPersonNumber = findViewById(R.id.TV_JoinedPersonNumber);

        foodPartyModel = (FoodPartyModel) getIntent().getSerializableExtra("FoodPartyObject"); //get data pass from previous activity/fragment

        title.setText(foodPartyModel.getTitle());
        organizer.setText(foodPartyModel.getOrganiserId());
        location.setText(foodPartyModel.getLocation());
        date.setText(foodPartyModel.getDateText());
        time.setText(foodPartyModel.getStartTimeText() + " - " + foodPartyModel.getEndTimeText());
        joinedPersonNumber.setText(foodPartyModel.getJoinedPersons().size() + "/9");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // exit this activity
            }
        });

        RecyclerView recyclerView = findViewById(R.id.RV_PersonList);
        JoinedPersonRecyclerViewAdapter adapter = new JoinedPersonRecyclerViewAdapter(this, foodPartyModel.getJoinedPersons());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}