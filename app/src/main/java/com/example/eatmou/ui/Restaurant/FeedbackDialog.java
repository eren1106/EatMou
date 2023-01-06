package com.example.eatmou.ui.Restaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.eatmou.R;
import com.example.eatmou.UserModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class FeedbackDialog extends AppCompatDialogFragment {


    private RatingBar ratingBar;
    private EditText commentET;
    private Button submitFeedbackBtn;
    private MaterialButton cancelFeedbackBtn;


    String restaurantId;
    UserModel user;

    public FeedbackDialog(String restaurantId, UserModel user) {
        this.restaurantId = restaurantId;
        this.user = user;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getUserId() {
        return user.getUserID();
    }

    public String getUsername() {
        return user.getUsername();
    }

    FeedbackDialogListener feedbackDialogListener;


    public interface FeedbackDialogListener{
        public void submitFeedback();
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            feedbackDialogListener = (FeedbackDialogListener) context;
//        } catch (ClassCastException e){
//            throw new ClassCastException(context.toString());
//        }
//    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.feedback_dialog, null, false);

        ratingBar = view.findViewById(R.id.ratingBar);
        commentET = view.findViewById(R.id.comment_ET);
        submitFeedbackBtn = view.findViewById(R.id.submitFeedbackBtn);
        cancelFeedbackBtn = view.findViewById(R.id.cancelFeedbackBtn);

        Review review = new Review();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            }
        });

        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });

        cancelFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(view);

        return builder.create();
    }

    private void submitFeedback() {
        double rating = ratingBar.getRating();
        String comment = commentET.getText().toString().trim();

        if (rating == 0.0) {
            Toast.makeText(getContext(), "You must rate it before you can submit your feedback.", Toast.LENGTH_LONG).show();
            return;
        }

        CollectionReference reviewRef = FirebaseFirestore.getInstance().collection("Reviews");
        reviewRef.add(new Review(getRestaurantId(), getUserId(), getUsername(), rating, new Date(), comment));
        Toast.makeText(getContext(), "Your feedback has been submitted successfully", Toast.LENGTH_SHORT).show();
        dismiss();

    }


}
