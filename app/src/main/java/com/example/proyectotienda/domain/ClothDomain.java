package com.example.proyectotienda.domain;


import java.io.Serializable;
import java.util.Map;

import com.google.firebase.database.IgnoreExtraProperties;

import org.jetbrains.annotations.NotNull;

@IgnoreExtraProperties
public class ClothDomain implements Serializable {
    private String title;
    private String pic="";
    private String description="";
    private String brand="";
    private String size="";
    private String star="";
    private double price=0.0;
    private int numberInCart=0;

    private String category; // Agregar la propiedad category

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public ClothDomain(String title, String pic, String description, String star,String brand, String size, double price, String category) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.star = star;
        this.brand = brand;
        this.size = size;
        this.price = price;
        this.category = category;
    }

    public ClothDomain(String title, String pic, String description, String star,String brand, String size, double price) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.star = star;
        this.brand = brand;
        this.size = size;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public class ClothListResponse {
        private Map<String, ClothDomain> clothList;

        public ClothListResponse(Map<String, ClothDomain> clothList) {
            this.clothList = clothList;
        }

        public Map<String, ClothDomain> getClothList() {
            return clothList;
        }
    }
}

