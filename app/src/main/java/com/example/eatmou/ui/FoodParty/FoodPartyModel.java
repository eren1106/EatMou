package com.example.eatmou.ui.FoodParty;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FoodPartyModel implements Serializable{
    String id;
    String title;
    String organiserId;
    String location;
    Date date;
    Date startTime;
    Date endTime;
    int maxParticipant;
    ArrayList<String> joinedPersons;

    public FoodPartyModel(String id, String title, String organiserId, String location, Date date, Date startTime, Date endTime, int maxParticipant, ArrayList<String> joinedPersons) {
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

    public FoodPartyModel(FoodPartyModel fpm){
        this.id = fpm.getId();
        this.title = fpm.getTitle();
        this.organiserId = fpm.getOrganiserId();
        this.location = fpm.getLocation();
        this.date = fpm.getDate();
        this.startTime = fpm.getStartTime();
        this.endTime = fpm.getEndTime();
        this.maxParticipant = fpm.getMaxParticipant();
        this.joinedPersons = fpm.getJoinedPersons();
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
                (ArrayList<String>) map.get("joinedPersons")
                );
        return fpm;
    }

    public String getDateText() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(this.date);
    }

    public String getStartTimeText() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        return formatter.format(this.startTime);
    }

    public String getEndTimeText() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        return formatter.format(this.endTime);
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

    public ArrayList<String> getJoinedPersons() {
        return joinedPersons;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setMaxParticipant(int maxParticipant) {
        this.maxParticipant = maxParticipant;
    }

    public void setJoinedPersons(ArrayList<String> joinedPersons) {
        this.joinedPersons = joinedPersons;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        calendar.setTime(this.date);
        startTime.setTime(this.startTime);
        calendar.set(Calendar.HOUR, startTime.get(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
        return calendar;
    }
}

