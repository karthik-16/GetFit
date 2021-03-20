package com.example.myfitnessapp.Classes;

public class FriendRequestsModel {

    String  name,profileimg,user_AuthId,username;

    public FriendRequestsModel() {
    }

    public FriendRequestsModel(String name, String profileimg, String user_AuthId, String username) {
        this.name = name;
        this.profileimg = profileimg;
        this.user_AuthId = user_AuthId;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getUser_AuthId() {
        return user_AuthId;
    }

    public void setUser_AuthId(String user_AuthId) {
        this.user_AuthId = user_AuthId;
    }
}
