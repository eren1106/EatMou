package com.example.eatmou.FoodParty;

import java.io.Serializable;

public class JoinedPersonModel implements Serializable {
    String uid;
    String name;
    int profilePic;

    public JoinedPersonModel(String uid, String name, int profilePic) {
        this.uid = uid;
        this.name = name;
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }

    public int getProfilePic() {
        return profilePic;
    }
}
