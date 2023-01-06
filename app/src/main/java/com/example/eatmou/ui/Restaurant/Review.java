package com.example.eatmou.ui.Restaurant;

import com.google.firebase.firestore.ServerTimestamp;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Review {
    private String restaurantId;
    private String userId;
    private String username;
    private double userRating;
    private Date reviewDate;
    private String comment;

    public Review() {
        // empty no-arg constructor
    }

    public Review(String restaurantId, String userId, String username, double userRating, Date reviewDate, String comment) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.username = username;
        this.userRating = userRating;
        this.reviewDate = reviewDate;
        this.comment = comment;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ServerTimestamp
    public Date getReviewDate() {
        return reviewDate;
    }

    public String displayReviewDate() {
        PrettyTime p = new PrettyTime();
        return p.format(getReviewDate());
    }


}
