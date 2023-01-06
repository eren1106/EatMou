package com.example.eatmou.ui.Restaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eatmou.R;
import com.example.eatmou.UserModel;
import com.google.android.material.button.MaterialButton;
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

public class ReviewFragment extends Fragment {

    FirebaseFirestore firestore;
    CollectionReference reviewsRef;

    private MaterialButton leaveReviewBtn;
    private MaterialButton editReviewBtn;
    private RecyclerView userCommentRecView;
    private RecyclerView yourReviewRecView;
    private LinearLayout yourReviewSection;
    private UserCommentRecViewAdapter userCommentRecViewAdapter;

    Restaurant restaurant;
    UserModel currentUser;

    boolean isReviewed = false;
    String yourReviewId = "";
    private List<Review> reviews;

    Query query;

    public ReviewFragment(Restaurant restaurant, UserModel user) {
        this.restaurant = restaurant;
        this.currentUser = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container);


        userCommentRecView = view.findViewById(R.id.userCommentRecView);
        yourReviewRecView = view.findViewById(R.id.yourReview);
        yourReviewSection = view.findViewById(R.id.yourReviewSection);
        leaveReviewBtn = view.findViewById(R.id.leaveReviewBtn);
        editReviewBtn = view.findViewById(R.id.editYourReviewBtn);

        setYourReviewSectionVisibility();


        firestore = FirebaseFirestore.getInstance();

        Log.i("Restaurant ID: ", restaurant.getId());

        reviewsRef = firestore.collection("Reviews");

        Log.i("Collection Reference: ", reviewsRef.getPath());



        userCommentRecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        yourReviewRecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        reviews = new ArrayList<>();
        userCommentRecViewAdapter = new UserCommentRecViewAdapter(getContext(), reviews);
        userCommentRecView.setAdapter(userCommentRecViewAdapter);

        loadData();

        Log.i("Adapter: ", String.valueOf(userCommentRecViewAdapter.getItemCount()));
        Log.i("Review list: ", String.valueOf(reviews.size()));


        leaveReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSubmitFeedbackDialog();
                loadData();
            }
        });

        editReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditReviewDialog();
                userCommentRecViewAdapter.notifyDataSetChanged();
            }
        });


        return view;
    }



    private void setYourReviewSectionVisibility() {
        if (isReviewed) {
            yourReviewSection.setVisibility(View.VISIBLE);
            leaveReviewBtn.setClickable(false);
            leaveReviewBtn.setVisibility(View.GONE);
        }
        else {
            yourReviewSection.setVisibility(View.GONE);
            leaveReviewBtn.setClickable(true);
            leaveReviewBtn.setVisibility(View.VISIBLE);
        }
    }

    private void loadData() {
        Log.i("Method called: ", "LoadData()");
        reviews.clear();

        Log.i("restaurant Id: " , restaurant.getId());
        query = reviewsRef.whereEqualTo("restaurantId", restaurant.getId());

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
                    Review review = documentSnapshot.toObject(Review.class);

                    if (review.getUserId().equals(currentUser.getUserID())) {
                        isReviewed = true;
                        yourReviewId = documentSnapshot.getId();
                        setYourReviewSectionVisibility();
                        List<Review> userReview = new ArrayList<>();
                        userReview.add(review);
                        UserCommentRecViewAdapter yourReviewAdapter = new UserCommentRecViewAdapter(getContext(), userReview);
                        yourReviewRecView.setAdapter(yourReviewAdapter);
                        yourReviewAdapter.notifyDataSetChanged();
                    }else {
                        Log.i("Review id: ", review.getUserId());

                        switch (documentChange.getType()) {
                            case ADDED:
                                reviews.add(review);
                                Log.i("Added: ", review.getUsername());
                                Log.i("Review list:", String.valueOf(reviews.size()));
                                break;
                            case MODIFIED:
                                for (int i = 0; i < reviews.size(); i++) {
                                    if (reviews.get(i).getUserId().equals(review.getUserId())) {
                                        reviews.set(i, review);
                                        break;
                                    }
                                }
                                Log.i("Modified: ", review.getUserId());
                                Log.i("Review list:", String.valueOf(reviews.size()));
                            case REMOVED:
                                for (int i = 0; i < reviews.size(); i++) {
                                    if (reviews.get(i).getUserId().equals(review.getUserId())) {
                                        reviews.remove(i);
                                        break;
                                    }
                                }
                                Log.i("Removed: ", review.getUserId());
                                Log.i("Restaurant list:", String.valueOf(reviews.size()));
                        }

                        userCommentRecViewAdapter.notifyDataSetChanged();
                    }
                }
                Log.i("For loop ended le", "Thank you dajia");
            }
        });
    }



    private void openSubmitFeedbackDialog() {
        FeedbackDialog feedbackDialog = new FeedbackDialog(restaurant.getId(), currentUser);
        feedbackDialog.show(getActivity().getSupportFragmentManager(), "feedback dialog opened");
    }

    private void openEditReviewDialog() {
        Intent intent = new Intent(getContext(), EditReview.class);
        intent.putExtra("reviewId", yourReviewId);
        startActivity(intent);
    }
}