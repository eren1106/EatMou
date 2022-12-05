package com.example.eatmou.FoodParty;

public class FoodPartyModel {
    String title;
    String organizer;
    String location;
    String date;
    String time;

    public FoodPartyModel(String title, String organizer, String location, String date, String time) {
        this.title = title;
        this.organizer = organizer;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
