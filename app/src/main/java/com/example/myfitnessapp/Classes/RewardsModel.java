package com.example.myfitnessapp.Classes;

public class RewardsModel {
    String category,desc,image,title;
    int id;

    public RewardsModel() {
    }

    public RewardsModel(String category, String desc,int id, String image, String title) {
        this.category = category;
        this.desc = desc;
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
