package com.example.ecommerceapp.Model;

public class Cart {
    private String date,description,id,name,price,quantity,time;

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
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
