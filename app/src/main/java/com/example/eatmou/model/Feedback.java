package com.example.eatmou.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Feedback {
    private String userID;
    private int satisfaction;
    private String improvedFactor;
    private Date date;

    public Feedback() {
    }

    public Feedback(String userID, int satisfaction, String improvedFactor, Date date) {
        this.userID = userID;
        this.satisfaction = satisfaction;
        this.improvedFactor = improvedFactor;
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getImprovedFactor() {
        return improvedFactor;
    }

    public void setImprovedFactor(String improvedFactor) {
        this.improvedFactor = improvedFactor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userID", getUserID());
        map.put("satisfaction", getSatisfaction());
        map.put("improvedFactor", getImprovedFactor());
        map.put("Date", getDate());
        return map;
    }
}
