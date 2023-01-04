package com.example.eatmou.model;

import java.util.HashMap;
import java.util.Map;

public class AppLock {
    private String userID;
    private String password;
    private boolean existPass;

    public AppLock() {
    }

    public AppLock(String userID, String password, boolean existPass) {
        this.userID = userID;
        this.password = password;
        this.existPass = existPass;
    }

    public void setExistPass(boolean existPass) {
        this.existPass = existPass;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public boolean isExistPass() {
        return existPass;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userID", getUserID());
        map.put("password", getPassword());
        map.put("existPass", isExistPass());
        return map;
    }
}
