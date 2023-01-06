package com.example.eatmou.ui.Restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatmou.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class EditReview extends AppCompatActivity {

    private Toolbar toolbarEditReview;
    private TextView ratingTitle;
    private RatingBar ratingBar;
    private EditText commentET;
    private MaterialButton deleteReviewBtn;
    private MaterialButton saveReviewBtn;
    private MaterialButton cancelBtn;

    String reviewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        toolbarEditReview = (Toolbar) findViewById(R.id.toolbar_edit_review);
        toolbarEditReview.setTitle("Edit Your Review");

        Intent intent = getIntent();
        reviewId = intent.getStringExtra("reviewId").toString();

        Log.i("review id: " , reviewId);

        setSupportActionBar(toolbarEditReview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarEditReview.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ratingTitle = findViewById(R.id.reviewRatingTitle);
        ratingBar = findViewById(R.id.reviewRatingBar);
        commentET = findViewById(R.id.reviewComment_ET);
        deleteReviewBtn = findViewById(R.id.deleteReviewBtn);
        saveReviewBtn = findViewById(R.id.saveReviewButton);
        cancelBtn = findViewById(R.id.cancelReviewBtn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingTitle.setText("You have rated: " + v + " / 5.0");
            }
        });

        saveReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rating = ratingBar.getRating();
                String comment = commentET.getText().toString();
                Date reviewDate = new Date();

                CollectionReference reviewRef = FirebaseFirestore.getInstance().collection("Reviews");
                reviewRef.document(reviewId)
                        .update("userRating", rating, "comment", comment, "reviewDate", reviewDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(EditReview.this, "Your review has been updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}