package com.example.eatmou.ui.FoodParty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eatmou.FirebaseMethods;
import com.example.eatmou.R;
import com.example.eatmou.UserModel;
import com.example.eatmou.ui.homePage.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    ArrayList<JoinedPersonModel> joinedPersonModels = new ArrayList<>();
    JoinedPersonModel myJoinedPersonModel;

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
        fetchOrganiserName();
        fetchDataToJoinedPersons(this);
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
                    ArrayList<String> tempList = foodPartyModel.getJoinedPersons();
                    tempList.remove(currentUser.getUserID());
                    foodPartyModel.setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModel);

                    for (int i = 0; i < joinedPersonModels.size(); i++) {
                        if(joinedPersonModels.get(i).getUserId().equals(currentUser.getUserID())){
                            joinedPersonModels.remove(i);
                            break;
                        }
                    }
                    joinedPersonModels.remove(myJoinedPersonModel);
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
                    ArrayList<String> tempList = foodPartyModel.getJoinedPersons();
                    tempList.add(currentUser.getUserID());
                    foodPartyModel.setJoinedPersons(tempList);
                    firebaseMethods.updateFoodParty(foodPartyModel);
                    joinedPersonModels.add(myJoinedPersonModel);
                    adapter.notifyDataSetChanged();
                    joinedPersonNumber.setText(foodPartyModel.getJoinedPersons().size() + "/" + foodPartyModel.getMaxParticipant());
                    joined = true;
                    setBottomBtn();
                }
            });
        }
    }

    private boolean checkJoined(String userId) {
        for(String joinedPerson : foodPartyModel.getJoinedPersons()) {
            if(joinedPerson.equals(userId)) return true;
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
        location.setText(foodPartyModel.getLocation());
        date.setText(foodPartyModel.getDateText());
        time.setText(foodPartyModel.getStartTimeText() + " - " + foodPartyModel.getEndTimeText());
        joinedPersonNumber.setText(foodPartyModel.getJoinedPersons().size() + "/" + foodPartyModel.getMaxParticipant());
    }

    private void fetchOrganiserName () {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").document(foodPartyModel.getOrganiserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel tempUser = UserModel.toObject(document.getData());
                        organizer.setText(tempUser.getUsername());
                    } else {
                        // Document with the specified ID does not exist
                    }
                } else {
                    // Task failed
                }
            }
        });
    }

    private void fetchDataToJoinedPersons(Context context) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("users").document(currentUser.getUserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel tempUser = UserModel.toObject(document.getData());
                        myJoinedPersonModel = new JoinedPersonModel(tempUser.getUserID(), tempUser.getUsername(), tempUser.getProfilePicUrl());
                    } else {
                        // Document with the specified ID does not exist
                    }
                } else {
                    // Task failed
                }
            }
        });
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String doc : foodPartyModel.getJoinedPersons()) {
            tasks.add(firestore.collection("users").document(doc).get());
        }

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> list) {
                //Do what you need to do with your list
                for (Object object : list) {
//                    FamilyMember fm = ((DocumentSnapshot) object).toObject(FamilyMember.class);
                    UserModel tempUser = UserModel.toObject(((DocumentSnapshot) object).getData());
                    JoinedPersonModel newJoinedPerson = new JoinedPersonModel(tempUser.getUserID(), tempUser.getUsername(), tempUser.getProfilePicUrl());
                    joinedPersonModels.add(newJoinedPerson);
                }
                recyclerView = findViewById(R.id.RV_PersonList);
                adapter = new JoinedPersonRecyclerViewAdapter(context, joinedPersonModels);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        });
    }
}