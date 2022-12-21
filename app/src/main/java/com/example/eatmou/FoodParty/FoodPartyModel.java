package com.example.eatmou.FoodParty;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FoodPartyModel implements Serializable {
    String id;
    String title;
    String organiserId;
    String location;
    Date date;
    Date startTime;
    Date endTime;
    int maxParticipant;
    ArrayList<JoinedPersonModel> joinedPersons;

    public FoodPartyModel(String id, String title, String organiserId, String location, Date date, Date startTime, Date endTime, int maxParticipant, ArrayList<JoinedPersonModel> joinedPersons) {
        this.id = id;
        this.title = title;
        this.organiserId = organiserId;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxParticipant = maxParticipant;
        this.joinedPersons = joinedPersons;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("organiserId", organiserId);
        map.put("location", location);
        map.put("date", date);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("maxParticipant", maxParticipant);
        map.put("joinedPersons", joinedPersons);

        return map;
    }

    public static FoodPartyModel toObject(Map<String, Object> map) {
        FoodPartyModel fpm = new FoodPartyModel(
                (String) map.get("id"),
                (String) map.get("title"),
                (String) map.get("organiserId"),
                (String) map.get("location"),
                ((Timestamp) map.get("date")).toDate(),
                ((Timestamp) map.get("startTime")).toDate(),
                ((Timestamp) map.get("endTime")).toDate(),
                (int) ((long) map.get("maxParticipant")),
                (ArrayList<JoinedPersonModel>) map.get("joinedPersons"));
        return fpm;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getMaxParticipant() {
        return maxParticipant;
    }

    public ArrayList<JoinedPersonModel> getJoinedPersons() {
        return joinedPersons;
    }
}
