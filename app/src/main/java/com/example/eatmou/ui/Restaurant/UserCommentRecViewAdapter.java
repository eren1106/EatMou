package com.example.eatmou.ui.Restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCommentRecViewAdapter extends RecyclerView.Adapter<UserCommentRecViewAdapter.ViewHolder>{

    private List<Review> reviews;
    private Context context;

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
        Review review = reviews.get(position);

        holder.username.setText(review.getUsername());
        holder.userRating.setText(String.valueOf(review.getUserRating()));
        holder.commentDate.setText(review.displayReviewDate());
        holder.commentContent.setText(reviews.get(position).getComment());
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
