package com.example.eatmou.Restaurant;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Review {
    private String username;
    private double userRating;
    private String content;
    private Date reviewDate;

    public Review(String username, double userRating, String content, Date reviewDate) {
        this.username = username;
        this.userRating = userRating;
        this.content = content;
        this.reviewDate = reviewDate;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ServerTimestamp
    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
