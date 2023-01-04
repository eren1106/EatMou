package com.example.eatmou.Restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatmou.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Date;

public class ReviewFragment extends Fragment {

    private MaterialButton leaveReviewBtn;
    private RecyclerView userCommentRecView;
    Restaurant restaurant;

    ArrayList<Comment> comments = new ArrayList<>();

    public ReviewFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        leaveReviewBtn = view.findViewById(R.id.leaveReviewBtn);
        userCommentRecView = view.findViewById(R.id.userCommentRecView);

        leaveReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        comments.add(new Comment("Yua Mikami", 5.0, new Date(2022-12-28), "Fuck off my life."));
        comments.add(new Comment("Yua Mikame", 3.0, new Date(2022-12-27), "Fuck off my life."));
        comments.add(new Comment("Yua Mikami", 5.0, new Date(2022-12-26), "Fuck off my life."));
        comments.add(new Comment("Yua Mikamo", 5.0, new Date(2022-12-25), "Fuck off my life."));


        UserCommentRecViewAdapter userCommentRecViewAdapter = new UserCommentRecViewAdapter(this.getContext(), comments);
        userCommentRecView.setAdapter(userCommentRecViewAdapter);
        userCommentRecView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));


        return view;
    }

    private void openDialog() {
        FeedbackDialog feedbackDialog = new FeedbackDialog();
        feedbackDialog.show(getActivity().getSupportFragmentManager(), "feedback dialog opened");
    }
}