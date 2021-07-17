package com.raza.mealshare.Models;

import com.raza.mealshare.Database.AllProductsFills.MyProduct;

import java.util.ArrayList;

public class SeperatedItems {
    private ArrayList<MyProduct> postedItems;
    private Category category;

    public SeperatedItems(MyProduct postedItem) {
        postedItems=new ArrayList<>();
        postedItems.add(postedItem);
        this.category = postedItem.getCategory();
    }

    public void AddItem(MyProduct postedItem){
        postedItems.add(postedItem);
    }

    public ArrayList<MyProduct> getPostedItems() {
        return postedItems;
    }

    public void setPostedItems(ArrayList<MyProduct> postedItems) {
        this.postedItems = postedItems;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
