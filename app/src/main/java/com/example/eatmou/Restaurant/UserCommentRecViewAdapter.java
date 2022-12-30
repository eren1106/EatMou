package com.example.eatmou.Restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatmou.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCommentRecViewAdapter extends RecyclerView.Adapter<UserCommentRecViewAdapter.ViewHolder>{

    private final ArrayList<Comment> comments;

    private Context context;

    public UserCommentRecViewAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comment_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(comments.get(position).getUsername());
        holder.userRating.setText(String.valueOf(comments.get(position).getUserRating()));
        holder.commentDate.setText("yesterday");
        holder.commentContent.setText(comments.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
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
