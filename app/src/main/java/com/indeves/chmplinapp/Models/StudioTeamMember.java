package com.indeves.chmplinapp.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by khalid on 26/03/18.
 */

public class StudioTeamMember implements Serializable {
    private String imageUrl, name, city, gender, email, phone;
    private List<String> eventsIds;

    public StudioTeamMember() {
    }

    public StudioTeamMember(String imageUrl, String name, String city, String gender) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.city = city;
        this.gender = gender;
    }

    public StudioTeamMember(String imageUrl, String name, String city, String gender, String email, String phone, List<String> eventsIds) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.city = city;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.eventsIds = eventsIds;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getEventsIds() {
        return eventsIds;
    }

    public void setEventsIds(List<String> eventsIds) {
        this.eventsIds = eventsIds;
    }
}
