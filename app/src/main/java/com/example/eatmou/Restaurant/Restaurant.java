package com.example.eatmou.Restaurant;

public class Restaurant {

    private int id;
    private String name;
//    private String imageURL;
    private double rating;
    private String category;
    private String location;
    private String description;
    private boolean isOpen;

    // Constructor
    public Restaurant(int id, String name, double rating, String category, String location, String description, boolean isOpen) {
        this.id = id;
        this.name = name;
//        this.imageURL = imageURL;
        this.rating = rating;
        this.category = category;
        this.location = location;
        this.description = description;
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

    public String isOpen() {
        if (isOpen) return "OPEN";
        return "CLOSED";
    }
}
