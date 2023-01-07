package com.example.eatmou.ui.Restaurant;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingSystem {

    static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    static CollectionReference restaurantsRef = firestore.collection("Restaurants");
    static CollectionReference reviewsRef = firestore.collection("Reviews");


    public static void updateRestaurantRating(String restaurantId) {

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


        Task<List<Task<?>>> updateTask = Tasks.whenAllComplete(allTasks, allReviews)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if (totalReviewsCount[0] != 0) {
                    double overallRating = calculateOverallRating(totalReviewsCount[0], reviewsCount);
                    restaurantsRef.document(restaurantId).update("rating", overallRating);
                } else
                    restaurantsRef.document(restaurantId).update("rating", 0);

            }
        });

    }

    private static double calculateOverallRating(int totalReviewsCount, int[] reviewsCount) {
        double overallRating = ((reviewsCount[0]) + (reviewsCount[1] * 2) + (reviewsCount[2] * 3)
                + (reviewsCount[3] * 4) + (reviewsCount[4] * 5)) / (double) totalReviewsCount;
        Log.i("Overall Score: " , String.valueOf(overallRating));

        return overallRating;
    }


}
