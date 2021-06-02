package com.raza.mealshare.ExtraFiles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class SettingsModel {
    public SettingsModel() {
    }

    @Expose
    @SerializedName("version")
    private int version;
    @Expose
    @SerializedName("condition")
    private List<String> condition;
    @Expose
    @SerializedName("category")
    private List<Category> category;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getCondition() {
        return condition;
    }

    public void setCondition(List<String> condition) {
        this.condition = condition;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
