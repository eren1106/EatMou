package com.example.eatmou.Restaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

public class FeedbackDialog extends AppCompatDialogFragment {

    private RatingBar ratingBar;
    private EditText commentET;
    private Button submitFeedbackBtn;
    private Button cancelFeedbackBtn;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.feedback_dialog, null, false);

        ratingBar = view.findViewById(R.id.ratingBar);
        commentET = view.findViewById(R.id.comment_ET);
        submitFeedbackBtn = view.findViewById(R.id.submitFeedbackBtn);
        cancelFeedbackBtn = view.findViewById(R.id.cancelFeedbackBtn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                feedbackDialogListener.submitFeedback();
                Toast.makeText(getContext(), "Your feedback has been submitted successfully", Toast.LENGTH_SHORT).show();
                dismiss();
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


}
