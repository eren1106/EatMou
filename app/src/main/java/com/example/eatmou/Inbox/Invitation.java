package com.example.eatmou.Inbox;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Invitation {
    private String InvitationID;
    private String OrganiserID;
    private String InvitedID;
    private String Location;
    private Date date;
    private Date StartTime;
    private Date EndTime;
    private String Status;

    private boolean expandable;
    private boolean accepted;
    private boolean declined;
    private boolean canceled;

    public Invitation(String InvitationID, String OrganiserID, String InvitedID, String Location, Date date, Date StartTime, Date EndTime, String Status) {
        this.InvitationID = InvitationID;
        this.OrganiserID = OrganiserID;
        this.InvitedID = InvitedID;
        this.Location = Location;
        this.date = date;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        switch (Status) {
            case "Cancel":
                this.canceled = true;
                break;
            case "Accepted":
                this.accepted = true;
                this.declined = false;
                this.canceled = false;
                break;
            case "Declined":
                this.accepted = false;
                this.declined = true;
                this.canceled = false;
                break;
            default:
                this.accepted = false;
                this.declined = false;
                this.canceled = false;
                break;
        }
        this.Status = Status;
        this.expandable = false;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("InvitationID", InvitationID);
        map.put("OrganiserID", OrganiserID);
        map.put("InvitedID", InvitedID);
        map.put("Location", Location);
        map.put("Date", date);
        map.put("StartTime", StartTime);
        map.put("EndTime", EndTime);
        map.put("Status", Status);
        return map;
    }

    public static Invitation toObject(Map<String, Object> map) {
        Invitation invitation = new Invitation(
                (String) map.get("InvitationID"),
                (String) map.get("OrganiserID"),
                (String) map.get("InvitedID"),
                (String) map.get("Location"),
                ((Timestamp) map.get("Date")).toDate(),
                ((Timestamp) map.get("StartTime")).toDate(),
                ((Timestamp) map.get("EndTime")).toDate(),
                (String) map.get("Status"));
        return invitation;
    }

    public String getDateText() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.date);
    }

    public String getStartTimeText() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        return formatter.format(this.StartTime);
    }

    public String getEndTimeText() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        return formatter.format(this.EndTime);
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

    public String getInvitationID() {
        return InvitationID;
    }

    public void setInvitationID(String invitationID) {
        InvitationID = invitationID;
    }

    public String getOrganiserID() {
        return OrganiserID;
    }

    public void setOrganiserID(String organiserID) {
        OrganiserID = organiserID;
    }

    public String getInvitedID() {
        return InvitedID;
    }

    public void setInvitedID(String invitedID) {
        InvitedID = invitedID;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
