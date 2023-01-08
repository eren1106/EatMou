package com.example.eatmou.ui.Restaurant;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.type.DateTime;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Restaurant {

    private String id;
    private String name;
//    private String imageURL;
    private double rating;
    private String category;
    private String location;
    private String description;
    private ArrayList<Integer> openingHours;        // {hours, minutes}
    private ArrayList<Integer> closingHours;        // {hours, minutes}

    public Restaurant() {
        // empty no-arg constructor
    }

    // Constructor
    public Restaurant(String name, double rating, String category, String location, String description,
                      ArrayList<Integer> openingHours, ArrayList<Integer> closingHours) {
        this.name = name;
//        this.imageURL = imageURL;
        this.rating = rating;
        this.category = category;
        this.location = location;
        this.description = description;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setImageURL(String imageURL) {
//        this.imageURL = imageURL;
//    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOpeningHours(ArrayList<Integer> openingHours) {
        this.openingHours = openingHours;
    }

    public void setClosingHours(ArrayList<Integer> closingHours) {
        this.closingHours = closingHours;
    }

// Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public String getImageURL() {
//        return imageURL;
//    }

    public double getRating() {
        return rating;
    }

    public String displayRating() {
        return String.format("%.1f", getRating());
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Integer> getOpeningHours() {
        return openingHours;
    }

    public ArrayList<Integer> getClosingHours() {
        return closingHours;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStatus() {
        if (isOpen(LocalTime.now())) return "OPEN";
        else return "CLOSED";
    }

    // cannot perform validity check and no action for invalid time (if open >= close --> X )
    public boolean isOpen(LocalTime now) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime open = LocalTime.of(openingHours.get(0), openingHours.get(1));
            LocalTime close = LocalTime.of(closingHours.get(0), closingHours.get(1));
            return (now.isAfter(open) && now.isBefore(close));
        }
        return false;
    }
}

