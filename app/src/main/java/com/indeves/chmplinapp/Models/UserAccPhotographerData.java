package com.indeves.chmplinapp.Models;

public class UserAccPhotographerData {
    private String name;
    private String location;
    private String currency;
    private String price;
    private String gender;

    public UserAccPhotographerData(String name, String location, String currency, String price, String gender) {
        this.name = name;
        this.location = location;
        this.currency = currency;
        this.price = price;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
