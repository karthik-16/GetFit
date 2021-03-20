package com.example.myfitnessapp.Classes;

import android.util.Log;

public class SearchPeopleModel {
    String name,profileimg,user_AuthId,username;

    public SearchPeopleModel() {
    }

    public SearchPeopleModel(String name, String profileimg, String user_AuthId, String username) {
        this.name = name;
        this.profileimg = profileimg;
        this.user_AuthId = user_AuthId;
        this.username = username;
    }

    public String getUser_AuthId() {
        Log.d("TAGEW", "getUser_AuthId: " + user_AuthId);
        return user_AuthId;
    }

    public void setUser_AuthId(String user_AuthId) {
        this.user_AuthId = user_AuthId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
