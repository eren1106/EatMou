package com.example.eatmou.ui.FoodParty;

import java.io.Serializable;
import java.util.ArrayList;

public class FoodPartyModel implements Serializable {
    String title;
    String organizer;
    String location;
    String date;
    String time;
    ArrayList<JoinedPersonModel> joinedPersonModels;

    public FoodPartyModel(String title, String organizer, String location, String date, String time, ArrayList<JoinedPersonModel> joinedPersonModels) {
        this.title = title;
        this.organizer = organizer;
        this.location = location;
        this.date = date;
        this.time = time;
        this.joinedPersonModels = joinedPersonModels;
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

    public ArrayList<JoinedPersonModel> getJoinedPersonModels() {
        return joinedPersonModels;
    }
}
