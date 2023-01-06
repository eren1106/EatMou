package com.example.eatmou.ui.Restaurant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eatmou.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {

    Restaurant restaurant;

    private TextView restaurantName;
    private TextView restaurantCategory;
    private TextView restaurantLocation;
    private TextView restaurantOperatingHours;
    private TextView restaurantDescription;

    private TextView overallRating;
    private ProgressBar rating5star;
    private ProgressBar rating4star;
    private ProgressBar rating3star;
    private ProgressBar rating2star;
    private ProgressBar rating1star;

    OnRestaurantListener restaurantListener;

    public AboutFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public interface OnRestaurantListener {
        void setRestaurantDetails(Restaurant restaurant);
    }

    public void setOnRestaurantListener (OnRestaurantListener restaurantListener) {
        this.restaurantListener = restaurantListener;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        Activity activity = (Activity) context;
//
//        try {
//            restaurantListener = (OnRestaurantListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must be override setRestaurantDetails");
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container);

        restaurantName = view.findViewById(R.id.restaurantName);
        restaurantCategory = view.findViewById(R.id.restaurantCategory);
        restaurantLocation = view.findViewById(R.id.restaurantLocation);
        restaurantOperatingHours = view.findViewById(R.id.restaurantOperatingHours);
        restaurantDescription = view.findViewById(R.id.restaurantDescription);


        restaurantName.setText(restaurant.getName());
        restaurantCategory.setText(restaurant.getCategory());
        restaurantLocation.setText(restaurant.getLocation());
        restaurantOperatingHours.setText(setOperatingHours(restaurant.getOpeningHours(), restaurant.getClosingHours()));
        restaurantDescription.setText(restaurant.getDescription());

        overallRating = view.findViewById(R.id.overallRating);
        rating5star = view.findViewById(R.id.rating5starProgress);
        rating4star = view.findViewById(R.id.rating4starProgress);
        rating3star = view.findViewById(R.id.rating3starProgress);
        rating2star = view.findViewById(R.id.rating2starProgress);
        rating1star = view.findViewById(R.id.rating1starProgress);

        loadRatingData();

        return view;
    }

    private void loadRatingData() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Reviews");

        Task<QuerySnapshot> task1 = collectionReference.whereEqualTo("restaurantId", restaurant.getId())
                .whereEqualTo("userRating", 1).get();

        Task<QuerySnapshot> task2 = collectionReference.whereEqualTo("restaurantId", restaurant.getId())
                .whereEqualTo("userRating", 2).get();

        Task<QuerySnapshot> task3 = collectionReference.whereEqualTo("restaurantId", restaurant.getId())
                .whereEqualTo("userRating", 3).get();

        Task<QuerySnapshot> task4 = collectionReference.whereEqualTo("restaurantId", restaurant.getId())
                .whereEqualTo("userRating", 4).get();

        Task<QuerySnapshot> task5 = collectionReference.whereEqualTo("restaurantId", restaurant.getId())
                .whereEqualTo("userRating", 5).get();

        Task<List<Task<?>>> allTasks = Tasks.whenAllComplete(task1, task2, task3, task4, task5)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        Log.i("All task complete: ", "Yeah");
                        int rating1 = task1.getResult().size();
                        int rating2 = task2.getResult().size();
                        int rating3 = task3.getResult().size();
                        int rating4 = task4.getResult().size();
                        int rating5 = task5.getResult().size();
                        Log.i("Rating 1: " , String.valueOf(rating1));
                        Log.i("Rating 2: " , String.valueOf(rating2));
                        Log.i("Rating 3: " , String.valueOf(rating3));
                        Log.i("Rating 4: " , String.valueOf(rating4));
                        Log.i("Rating 5: " , String.valueOf(rating5));

                        displayOverallRating(rating1, rating2, rating3, rating4, rating5);

                    }
                });




    }

//    private void updateRestaurantData(double overallScore) {
//        DocumentReference documentReference = FirebaseFirestore.getInstance()
//                .collection("Restaurants").document(restaurant.getId());
//
//        documentReference.update("rating", overallScore);
//    }

    private void displayOverallRating(int rating1, int rating2, int rating3, int rating4, int rating5) {
        int totalReviews = rating1 + rating2 + rating3 + rating4 + rating5;
        Log.i("Total reviews: " , String.valueOf(totalReviews));
        double overallScore = ((rating1) + (rating2 * 2) + (rating3 * 3) + (rating4 * 4) + (rating5 * 5)) / (double) totalReviews;
        restaurant.setRating(overallScore);
//        updateRestaurantData(overallScore);

        if (totalReviews == 0)
            overallRating.setText("--");
        else
            overallRating.setText(String.valueOf(restaurant.getRating()));

        // avoid zero division error
        if (totalReviews != 0) {
            rating5star.setProgress((int) (100 * ((double)rating5 / (double)totalReviews)));
            rating4star.setProgress((int) (100 * ((double)rating4 / (double)totalReviews)));
            rating3star.setProgress((int) (100 * ((double)rating3 / (double)totalReviews)));
            rating2star.setProgress((int) (100 * ((double)rating2 / (double)totalReviews)));
            rating1star.setProgress((int) (100 * ((double)rating1 / (double)totalReviews)));
        }
    }

    private String setOperatingHours(ArrayList<Integer> openingHours, ArrayList<Integer> closingHours) {
        String str =String.format("%02d:%02d - %02d:%02d\n(Monday - Sunday)",
                openingHours.get(0), openingHours.get(1), closingHours.get(0), closingHours.get(1));
        return str;
    }
}