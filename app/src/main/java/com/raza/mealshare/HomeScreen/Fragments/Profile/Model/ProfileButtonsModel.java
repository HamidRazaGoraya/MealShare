package com.raza.mealshare.HomeScreen.Fragments.Profile.Model;

public class ProfileButtonsModel {
    private String title;
    private String id;


    public ProfileButtonsModel(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
