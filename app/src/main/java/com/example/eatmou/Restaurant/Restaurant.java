package com.example.eatmou.Restaurant;

import android.os.Build;

import com.google.type.DateTime;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Restaurant {

    private int id;
    private String name;
//    private String imageURL;
    private double rating;
    private String category;
    private String location;
    private String description;
    private boolean isOpen;
    private final int[] openingHours;        // {hours, minutes}
    private final int[] closingHours;        // {hours, minutes}

    // Constructor
    public Restaurant(int id, String name, double rating, String category, String location, String description,
                      int[] openingHours, int[] closingHours, boolean isOpen) {
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

    // Getters

    public int getId() {
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

    public int[] getOpeningHours() {
        return openingHours;
    }

    public int[] getClosingHours() {
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

