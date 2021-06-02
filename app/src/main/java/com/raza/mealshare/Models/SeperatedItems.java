package com.raza.mealshare.Models;

import java.util.ArrayList;

public class SeperatedItems {
    private ArrayList<PostedItems> postedItems;
    private Category category;

    public SeperatedItems(PostedItems postedItem) {
        postedItems=new ArrayList<>();
        postedItems.add(postedItem);
        this.category = postedItem.getCategory();
    }

    public void AddItem(PostedItems postedItem){
        postedItems.add(postedItem);
    }

    public ArrayList<PostedItems> getPostedItems() {
        return postedItems;
    }

    public void setPostedItems(ArrayList<PostedItems> postedItems) {
        this.postedItems = postedItems;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
