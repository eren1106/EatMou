package com.example.eatmou.Restaurant;

import android.os.Build;

import com.google.type.DateTime;

import java.time.LocalTime;
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
    private boolean isOpen;
    private List<Integer> openingHours;        // {hours, minutes}
    private List<Integer> closingHours;        // {hours, minutes}

    public Restaurant() {
        // empty no-arg constructor
    }

    // Constructor
    public Restaurant(String id, String name, double rating, String category, String location, String description,
                      List<Integer> openingHours, List<Integer> closingHours, boolean isOpen) {
        this.id = id;
        this.name = name;
//        this.imageURL = imageURL;
        this.rating = rating;
        this.category = category;
        this.location = location;
        this.description = description;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
        this.isOpen = isOpen;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setOpeningHours(List<Integer> openingHours) {
        this.openingHours = openingHours;
    }

    public void setClosingHours(List<Integer> closingHours) {
        this.closingHours = closingHours;
    }

// Getters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
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

    public List<Integer> getOpeningHours() {
        return openingHours;
    }

    public List<Integer> getClosingHours() {
        return closingHours;
    }

    public String isOpen() {
        if (isOpen) return "OPEN";
        else return "CLOSED";
    }

    // cannot perform validity check and no action for invalid time (if open >= close --> X )
//    public boolean isOpen(LocalTime now) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            LocalTime open = LocalTime.of(openingHours[0], openingHours[1]);
//            LocalTime close = LocalTime.of(closingHours[0], closingHours[1]);
//            return (now.isAfter(open) && now.isBefore(close));
//        }
//        return false;
//    }
}

