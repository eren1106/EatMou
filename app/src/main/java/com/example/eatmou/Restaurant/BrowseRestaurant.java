package com.example.eatmou.Restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.eatmou.R;
import com.example.eatmou.ui.homePage.MainActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BrowseRestaurant extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private CollectionReference restaurantRef;

    private RecyclerView categoryFilterRecView;
    private RecyclerView restaurantItemRecView;
    private RestaurantItemRecViewAdapter restaurantItemRecViewAdapter;

    private List<Restaurant> restaurantsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_restaurant);

        firestore = FirebaseFirestore.getInstance();
        restaurantRef = firestore.collection("Restaurants");

        Log.i("Browse Restaurant", "Create");

        setUpRestaurantItemRecView();

        categoryFilterRecView = findViewById(R.id.categoryFilterRecView);



        // Hardcoded
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Coffee");
        categories.add("Beverage");
        categories.add("Pizza");
        categories.add("Cheese");
        categories.add("Fish");
        categories.add("Soup");

        CategoryFilterRecViewAdapter categoryFilterRecViewAdapter = new CategoryFilterRecViewAdapter(this, categories);
        categoryFilterRecView.setAdapter(categoryFilterRecViewAdapter);
        categoryFilterRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

//
//        restaurants = new ArrayList<>();
//        restaurants.add(new Restaurant(1, "StarBuck Coffee", 5.0, "Coffee", "KL", "I love bucks", new int[]{16, 0}, new int[]{23, 0}, false));
//        restaurants.add(new Restaurant(2, "StarBug Coffee", 5.0, "Coffee", "Penang", "I love bugs", new int[]{0, 0}, new int[]{16, 0}, true));
//        restaurants.add(new Restaurant(3, "StarButt Coffee", 4.0, "Coffee", "KL", "I love butts", new int[]{0, 0}, new int[]{16, 0}, true));
//        restaurants.add(new Restaurant(4, "StarDebug Coffee", 4.0, "Coffee", "Melaka", "I love debug", new int[]{16, 0}, new int[]{23, 0}, false));
//        restaurants.add(new Restaurant(5, "StarFixBug Coffee", 5.0, "Beverage", "Sarawak", "I love fix bug", new int[]{0, 0}, new int[]{16, 0}, true));
//        restaurants.add(new Restaurant(6, "StarNoBug Coffee", 4.0, "Beverage", "Sabah", "
//        ", new int[]{16, 0}, new int[]{23, 0}, false));
//
//        RestaurantItemRecViewAdapter restaurantItemRecViewAdapter = new RestaurantItemRecViewAdapter(this, restaurants);
//        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);
//        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));
////

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Browse Restaurant", "Start");
        Log.i("Adapter is not null", String.valueOf((restaurantItemRecViewAdapter != null)));
//        restaurantItemRecViewAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Browse Restaurant", "Stop");
//        restaurantItemRecViewAdapter.stopListening();
    }

    private void setUpRestaurantItemRecView() {
        restaurantItemRecView = findViewById(R.id.restaurantItemRecView);
        restaurantItemRecView.setHasFixedSize(true);
        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));

        restaurantsList = new ArrayList<Restaurant>();
        restaurantItemRecViewAdapter = new RestaurantItemRecViewAdapter(BrowseRestaurant.this, restaurantsList);

        EventChangeListener();

        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);
//
//        restaurantItemRecViewAdapter.setOnItemClickListener(new RestaurantItemRecViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Restaurant restaurant, int position) {
//                String id = restaurant.getId();
//                Toast.makeText(BrowseRestaurant.this, "ID: " + id, Toast.LENGTH_SHORT).show();
//            }
//        });


//        Log.i("Method called: ", "setUpRestaurantItemRecView");
//        Query query = restaurantRef.orderBy("rating");
//
//        FirestoreRecyclerOptions<Restaurant> options = new FirestoreRecyclerOptions.Builder<Restaurant>()
//                .setQuery(query, Restaurant.class).build();
//
//        System.out.println(options.getSnapshots());
//
//        Log.e("Options is not null", String.valueOf((options != null)));
//
//        restaurantItemRecViewAdapter = new RestaurantItemRecViewAdapter(options);
//
//        RecyclerView restaurantItemRecView = findViewById(R.id.restaurantItemRecView);
//        restaurantItemRecView.setHasFixedSize(true);
//        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);
//        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));
//
//        restaurantItemRecViewAdapter.setOnItemClickListener(new RestaurantItemRecViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                String id = documentSnapshot.getId();
//                Toast.makeText(BrowseRestaurant.this, "ID: " + id, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void EventChangeListener() {

        restaurantRef.orderBy("rating", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    Restaurant restaurant = documentChange.getDocument().toObject(Restaurant.class);
                    QueryDocumentSnapshot documentSnapshot = documentChange.getDocument();
                    restaurant.setId(documentSnapshot.getId());
                    System.out.println(restaurant.getId());

                    switch (documentChange.getType()) {
                        case ADDED:
                            restaurantsList.add(restaurant);
                            break;
                        case MODIFIED:
                            for (int i=0; i < restaurantsList.size(); i++){
                                if (restaurantsList.get(i).getId().equals(restaurant.getId())) {
                                    restaurantsList.set(i, restaurant);
                                    break;
                                }
                            }
                    }
                    restaurantItemRecViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}