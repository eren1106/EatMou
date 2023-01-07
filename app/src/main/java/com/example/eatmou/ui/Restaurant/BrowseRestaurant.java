package com.example.eatmou.ui.Restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class BrowseRestaurant extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private CollectionReference restaurantRef;

    private SearchView searchRestaurantBar;
    private RecyclerView categoryFilterRecView;
    private RecyclerView restaurantItemRecView;
    private RestaurantItemRecViewAdapter restaurantItemRecViewAdapter;
    private TextView noResultFoundText;

    private List<Restaurant> restaurantsList;
    ArrayList<String> filteredCategory = new ArrayList<>();

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_restaurant);

        Log.i("Browse Restaurant", "Create");

        firestore = FirebaseFirestore.getInstance();
        Log.i("firestore instance", String.valueOf(firestore.getFirestoreSettings()));
        restaurantRef = firestore.collection("Restaurants");
        Log.i("restaurantRef", restaurantRef.getId());


        noResultFoundText = findViewById(R.id.noResultFoundText);
        restaurantItemRecView = findViewById(R.id.restaurantItemRecView);
        restaurantItemRecView.setHasFixedSize(true);
        restaurantItemRecView.setLayoutManager(new GridLayoutManager(this, 2));

        restaurantsList = new ArrayList<Restaurant>();
        restaurantItemRecViewAdapter = new RestaurantItemRecViewAdapter(BrowseRestaurant.this, restaurantsList);


        restaurantItemRecView.setAdapter(restaurantItemRecViewAdapter);

        EventChangeListener();

        Log.i("Adapter: ", String.valueOf(restaurantItemRecViewAdapter.getItemCount()));
        Log.i("Restaurant list: ", String.valueOf(restaurantsList.size()));


        searchRestaurantBar = findViewById(R.id.searchRestaurantBar);
        searchRestaurantBar.clearFocus();
        searchRestaurantBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return false;
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

        categoryFilterRecViewAdapter.setOnItemClickListener(new CategoryFilterRecViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String filter, boolean isChecked) {
                if (isChecked)
                    filteredCategory.add(filter);
                else
                    filteredCategory.remove(filter);

                if (!filteredCategory.isEmpty())
                    filterData();
                else {
                    // temp code
                    int index = 0;
                    for (Restaurant r : restaurantsList) {
                        Log.i("Index: ", index + ": " + r.getName());
                        index++;
                    }
                    restaurantItemRecViewAdapter.setFilteredList(restaurantsList);
                    restaurantItemRecViewAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void filterData(String newText) {
        List<Restaurant> filteredList = new ArrayList<>();
        for (Restaurant restaurant : restaurantsList) {
            if (restaurant.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }

        // temp code
        int index = 0;
        for (Restaurant r : filteredList) {
            Log.i("Index: ", index + ": " + r.getName());
            index++;
        }

        if (filteredList.isEmpty()) {
            restaurantItemRecView.setVisibility(View.GONE);
            noResultFoundText.setVisibility(View.VISIBLE);
        } else {
            restaurantItemRecViewAdapter.setFilteredList(filteredList);
            restaurantItemRecView.setVisibility(View.VISIBLE);
            noResultFoundText.setVisibility(View.GONE);
        }
        restaurantItemRecViewAdapter.notifyDataSetChanged();

    }

    private void filterData() {
        List<Restaurant> filteredList = new ArrayList<>();

        for (String filter: filteredCategory) {
            query = setQuery(filter);

            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e("Firestore Error", error.getMessage());
                        return;
                    }

                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                        restaurant.setId(documentSnapshot.getId());
                        filteredList.add(restaurant);

                    }

                    // temp code
                    int index = 0;
                    for (Restaurant r : filteredList) {
                        Log.i("Index: ", index + ": " + r.getName());
                        index++;
                    }
                    restaurantItemRecViewAdapter.setFilteredList(filteredList);
                    restaurantItemRecViewAdapter.notifyDataSetChanged();

                }
            });


        }

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




    public void EventChangeListener() {

        restaurantsList.clear();
        Log.i("Method called: ", "Event Change Listener");

        query = restaurantRef.orderBy("rating", Query.Direction.DESCENDING);

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
                    restaurantItemRecViewAdapter.notifyDataSetChanged();
                }
                Log.i("For loop ended le", "Thank you dajia");
            }
        });

    }

    private Query setQuery(String filter) {
        Query query = restaurantRef.orderBy("rating", Query.Direction.DESCENDING).whereEqualTo("category", filter);
        System.out.println(filter);
        return query;
    }


}