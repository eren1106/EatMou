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

        Log.i("Browse Restaurant", "Create");


        restaurantItemRecView = findViewById(R.id.restaurantItemRecView);
        restaurantItemRecView.setHasFixedSize(true);
        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));

        restaurantsList = new ArrayList<Restaurant>();
        restaurantItemRecViewAdapter = new RestaurantItemRecViewAdapter(BrowseRestaurant.this, restaurantsList);

        firestore = FirebaseFirestore.getInstance();
        Log.i("firestore instance", String.valueOf(firestore.getFirestoreSettings()));
        restaurantRef = firestore.collection("Restaurants");
        Log.i("restaurantRef", restaurantRef.getId());

        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);

        EventChangeListener();

        Log.i("Adapter: ", String.valueOf(restaurantItemRecViewAdapter.getItemCount()));
        Log.i("Restaurant list: ", String.valueOf(restaurantsList.size()));




        restaurantItemRecViewAdapter.setOnItemClickListener(new RestaurantItemRecViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Restaurant restaurant, int position) {
                String id = restaurant.getId();
                Toast.makeText(BrowseRestaurant.this, "ID: " + id, Toast.LENGTH_SHORT).show();
            }
        });


        categoryFilterRecView = findViewById(R.id.categoryFilterRecView);
        categoryFilterRecView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

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


    private void EventChangeListener() {

        Log.i("Method called: ", "Event Change Listener");

        restaurantRef.orderBy("category", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                case REMOVED:
                                    for (int i = 0; i < restaurantsList.size(); i++) {
                                        if (restaurantsList.get(i).getId().equals(restaurant.getId())) {
                                            restaurantsList.remove(i);
                                            break;
                                        }
                                    }
                                    Log.i("Removed: ", restaurant.getId());
                                    Log.i("Restaurant list:", String.valueOf(restaurantsList.size()));
                            }
                            restaurantItemRecViewAdapter.notifyDataSetChanged();
                        }
                        Log.i("For loop ended le", "Thank you dajia");
                    }
                });

    }


}