package com.example.eatmou.ui.Restaurant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eatmou.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCommentRecViewAdapter extends RecyclerView.Adapter<UserCommentRecViewAdapter.ViewHolder>{

    private List<Review> reviews;
    private Context context;
    private FirebaseFirestore db;

    public UserCommentRecViewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_comment_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        Review review = reviews.get(position);

        holder.username.setText(review.getUsername());
        holder.userRating.setText(String.valueOf((int) review.getUserRating()));
        holder.commentDate.setText(review.displayReviewDate());
        holder.commentContent.setText(reviews.get(position).getComment());

        DocumentReference docRef = db.collection("users").document(review.getUserId());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String imageURL = document.getString("profilePicUrl");
                    Glide.with(context).load(imageURL).into(holder.userProfileImage);
                } else Log.d("User document", "No such document");
            } else Log.d("User document", "get failed with ", task.getException());
        });

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username, userRating, commentDate, commentContent;
        private CircleImageView userProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            userRating = itemView.findViewById(R.id.userRating);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentContent = itemView.findViewById(R.id.commentContent);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
        }
    }


}
