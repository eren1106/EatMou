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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutFragment extends Fragment {

    Restaurant restaurant;
    RatingSystem ratingSystem;

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

        displayOverallRating(restaurant.getId());

        return view;
    }



    private void displayOverallRating(String restaurantId) {

        CollectionReference reviewsRef = FirebaseFirestore.getInstance().collection("Reviews");

        int[] reviewsCount = new int[5];

        Task<QuerySnapshot> task1 = reviewsRef.whereEqualTo("restaurantId", restaurantId)
                .whereEqualTo("userRating", 1).get();

        Task<QuerySnapshot> task2 = reviewsRef.whereEqualTo("restaurantId", restaurantId)
                .whereEqualTo("userRating", 2).get();

        Task<QuerySnapshot> task3 = reviewsRef.whereEqualTo("restaurantId", restaurantId)
                .whereEqualTo("userRating", 3).get();

        Task<QuerySnapshot> task4 = reviewsRef.whereEqualTo("restaurantId", restaurantId)
                .whereEqualTo("userRating", 4).get();

        Task<QuerySnapshot> task5 = reviewsRef.whereEqualTo("restaurantId", restaurantId)
                .whereEqualTo("userRating", 5).get();

        Task<List<Task<?>>> allTasks = Tasks.whenAllComplete(task1, task2, task3, task4, task5)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        Log.i("Rating System", "allTasks completed");
                        reviewsCount[0] = task1.getResult().size();
                        reviewsCount[1] = task2.getResult().size();
                        reviewsCount[2] = task3.getResult().size();
                        reviewsCount[3] = task4.getResult().size();
                        reviewsCount[4] = task5.getResult().size();
                        Log.i("Rating 1: " , String.valueOf(reviewsCount[0]));
                        Log.i("Rating 2: " , String.valueOf(reviewsCount[1]));
                        Log.i("Rating 3: " , String.valueOf(reviewsCount[2]));
                        Log.i("Rating 4: " , String.valueOf(reviewsCount[3]));
                        Log.i("Rating 5: " , String.valueOf(reviewsCount[4]));
                    }
                });

        int [] totalReviewsCount = new int[1];

        Log.i("Method called:"," getAllReviews()");
        Task<QuerySnapshot> allReviews = reviewsRef.whereEqualTo("restaurantId", restaurantId).get();

        allReviews.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i("Task completed: ", String.valueOf(task.isComplete()));
                totalReviewsCount[0] = allReviews.getResult().size();
                Log.i("Total Reviews Count: ", String.valueOf(totalReviewsCount[0]));
            }
        });


        Task<List<Task<?>>> displayTask = Tasks.whenAllComplete(allTasks, allReviews)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        if (totalReviewsCount[0] == 0)
                            overallRating.setText("--");
                        else
                            overallRating.setText(restaurant.displayRating());

                        // avoid zero division error
                        if (totalReviewsCount[0] != 0) {
                            rating5star.setProgress((int) (100 * ((double)reviewsCount[4] / (double)totalReviewsCount[0])));
                            rating4star.setProgress((int) (100 * ((double)reviewsCount[3] / (double)totalReviewsCount[0])));
                            rating3star.setProgress((int) (100 * ((double)reviewsCount[2] / (double)totalReviewsCount[0])));
                            rating2star.setProgress((int) (100 * ((double)reviewsCount[1] / (double)totalReviewsCount[0])));
                            rating1star.setProgress((int) (100 * ((double)reviewsCount[0] / (double)totalReviewsCount[0])));
                        }
                    }
                });


    }

    private String setOperatingHours(ArrayList<Integer> openingHours, ArrayList<Integer> closingHours) {
        String str =String.format("%02d:%02d - %02d:%02d\n(Monday - Sunday)",
                openingHours.get(0), openingHours.get(1), closingHours.get(0), closingHours.get(1));
        return str;
    }
}