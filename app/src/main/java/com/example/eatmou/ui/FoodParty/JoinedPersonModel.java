package com.example.eatmou.ui.FoodParty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JoinedPersonModel implements Serializable {
    String userId;
    String name;
    int profilePic;

    public JoinedPersonModel(String userId, String name, int profilePic) {
        this.userId = userId;
        this.name = name;
        this.profilePic = profilePic;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("profilePic", profilePic);
        return map;
    }

    public static JoinedPersonModel toObject(Map<String, Object> map) {
        JoinedPersonModel model = new JoinedPersonModel((String) map.get("userId"), (String) map.get("name"), (int) ((long) map.get("profilePic")));
        return model;
    }
    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }

    public int getProfilePic() {
        return profilePic;
    }
}
