package com.example.eatmou.Inbox;

import java.sql.Time;
import java.util.Date;

public class Invitation {
    private String username;
    private String location;
    private Date date;
    private Time time;

    private boolean expandable;
    private boolean accepted;
    private boolean declined;

    public Invitation(String username, String location, Date date, Time time) {
        this.username = username;
        this.location = location;
        this.date = date;
        this.time = time;
        this.expandable = false;
        this.accepted = false;
        this.declined = false;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined) {
        this.declined = declined;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
