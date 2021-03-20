package com.example.myfitnessapp.Classes;

public class videoModel {
    public String challenge_name,profileimg,username,videourl,id;
    public long ads_watched,count,likes,points,rank;

    public videoModel() {
        //this is required by firestore to query data
    }

    public videoModel(String challenge_name, String profileimg, String username, String videourl, String id, long ads_watched, long count, long likes, long points, long rank) {
        this.challenge_name = challenge_name;
        this.profileimg = profileimg;
        this.username = username;
        this.videourl = videourl;
        this.id = id;
        this.ads_watched = ads_watched;
        this.count = count;
        this.likes = likes;
        this.points = points;
        this.rank = rank;
    }

    public String getChallenge_name() {
        return challenge_name;
    }

    public void setChallenge_name(String challenge_name) {
        this.challenge_name = challenge_name;
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

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public long getAds_watched() {
        return ads_watched;
    }

    public void setAds_watched(long ads_watched) {
        this.ads_watched = ads_watched;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
