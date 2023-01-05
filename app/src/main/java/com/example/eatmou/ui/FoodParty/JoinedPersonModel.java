package com.example.eatmou.ui.FoodParty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JoinedPersonModel implements Serializable {
    String userId;
    String name;
    String profilePicUrl;

    public JoinedPersonModel(String userId, String name, String profilePicUrl) {
        this.userId = userId;
        this.name = name;
        this.profilePicUrl = profilePicUrl;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("profilePicUrl", profilePicUrl);
        return map;
    }

    public static JoinedPersonModel toObject(Map<String, Object> map) {
        JoinedPersonModel model = new JoinedPersonModel((String) map.get("userId"), (String) map.get("name"), (String) map.get("profilePicUrl"));
        return model;
    }
    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }
}
