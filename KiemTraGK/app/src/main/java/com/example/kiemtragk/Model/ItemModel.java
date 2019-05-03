package com.example.kiemtragk.Model;

import android.content.ClipData;

public class ItemModel {
    private int ID;
    private String Product_Name;
    private int Price;
    private String Description;
    private String Producer;

    public ItemModel(){

    }


    public ItemModel(int ID, String product_Name, int price, String description, String producer) {
        this.ID = ID;
        Product_Name = product_Name;
        Price = price;
        Description = description;
        Producer = producer;
    }



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProducer() {
        return Producer;
    }

    public void setProducer(String producer) {
        Producer = producer;
    }
}
