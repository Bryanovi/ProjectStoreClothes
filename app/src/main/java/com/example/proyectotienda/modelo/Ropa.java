package com.example.proyectotienda.modelo;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tblRopa")
public class Ropa implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "pic")
    private String pic;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "brand")
    private String brand;

    @ColumnInfo(name = "size")
    private String size;

    @ColumnInfo(name = "star")
    private String star;

    @ColumnInfo(name = "price")
    private double price;


    public Ropa(int id, String title, String pic,String description, String brand, String size, String star, double price) {
        this.id = id;
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.brand = brand;
        this.size = size;
        this.star = star;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
