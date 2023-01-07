package com.example.eatmou.ui.Restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.eatmou.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantLeaderboard extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private CollectionReference restaurantRef;

    private ImageButton backBtn;
    private RecyclerView restaurantRankingRecView;
    private RestaurantRankingRecViewAdapter restaurantRankingRecViewAdapter;

    private List<Restaurant> restaurantsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_leaderboard);

        firestore = FirebaseFirestore.getInstance();
        restaurantRef = firestore.collection("Restaurants");

        backBtn = findViewById(R.id.back_Btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        restaurantRankingRecView = findViewById(R.id.restaurantRankingRecView);
        restaurantRankingRecView.setHasFixedSize(true);
        restaurantRankingRecView.setLayoutManager(new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false));

        restaurantsList = new ArrayList<>();

        restaurantRankingRecViewAdapter = new RestaurantRankingRecViewAdapter(RestaurantLeaderboard.this, restaurantsList);

        restaurantRankingRecView.setAdapter(restaurantRankingRecViewAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {


        restaurantsList.clear();
        Log.i("Method called: ", "Event Change Listener");

        Query query = restaurantRef.orderBy("rating", Query.Direction.DESCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }

                Log.e("Check error: ", String.valueOf(value.isEmpty()));
                Log.e("Query snapshot: ", value.toString());

                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    QueryDocumentSnapshot documentSnapshot = documentChange.getDocument();
                    Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                    restaurant.setId(documentSnapshot.getId());

                    Log.i("Restaurant ID: ", restaurant.getId());


                    switch (documentChange.getType()) {
                        case ADDED:
                            restaurantsList.add(restaurant);
                            Log.i("Added: ", restaurant.getId());
                            Log.i("Restaurant list:", String.valueOf(restaurantsList.size()));
                            break;
                        case MODIFIED:
                            for (int i = 0; i < restaurantsList.size(); i++) {
                                if (restaurantsList.get(i).getId().equals(restaurant.getId())) {
                                    restaurantsList.set(i, restaurant);
                                    break;
                                }
                            }
                            Log.i("Modified: ", restaurant.getId());
                            Log.i("Restaurant list:", String.valueOf(restaurantsList.size()));
                            break;
                        case REMOVED:
                            for (int i = 0; i < restaurantsList.size(); i++) {
                                if (restaurantsList.get(i).getId().equals(restaurant.getId())) {
                                    restaurantsList.remove(i);
                                    break;
                                }
                            }
                            Log.i("Removed: ", restaurant.getId());
                            Log.i("Restaurant list:", String.valueOf(restaurantsList.size()));
                            break;
                    }
                    restaurantRankingRecViewAdapter.notifyDataSetChanged();
                }
                Log.i("For loop ended le", "Thank you dajia");
            }
        });
    }
}