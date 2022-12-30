package com.example.eatmou.ui.FoodParty;

import java.io.Serializable;

public class JoinedPersonModel implements Serializable {
    String name;
    int profilePic;

    public JoinedPersonModel(String name, int profilePic) {
        this.name = name;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public int getProfilePic() {
        return profilePic;
    }
}
