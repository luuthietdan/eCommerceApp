package com.example.ecommerceapp.Model;

public class Cart {
    private String date,image,description,id,name,price,quantity,time,status;

    public Cart() {
    }
    public Cart(String date, String description, String id, String name, String price, String quantity, String time) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        status = "accepting";
    }
    public Cart(String date, String description, String id, String name, String price, String quantity, String time,String image) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.image = image;
        status = "accepting";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
