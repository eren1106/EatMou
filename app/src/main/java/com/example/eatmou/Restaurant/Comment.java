package com.example.eatmou.Restaurant;

import java.util.Date;

public class Comment {

    private String username;
    private double userRating;
    private Date commentDate;
    private String content;

    public Comment(String username, double userRating, Date commentDate, String content) {
        this.username = username;
        this.userRating = userRating;
        this.commentDate = commentDate;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
