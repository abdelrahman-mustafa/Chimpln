package com.indeves.chmplinapp.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class UserData {


    public String name, email, phone, type, lastName, birthDate, gender, uid, profilePicUrl, location;
    private List<String> eventsIds;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UserData() {
    }

    public UserData(String email, String phone, String type, String uid) {
        this.email = email;
        this.type = type;
        this.phone = phone;
        this.uid = uid;
    }

    public UserData(String name, String email, String phone, String type, String lastName, String birthDate, String gender, String uid, String profilePicUrl, String location, List<String> eventsIds) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.uid = uid;
        this.profilePicUrl = profilePicUrl;
        this.location = location;
        this.eventsIds = eventsIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getEventsIds() {
        return eventsIds;
    }

    public void setEventsIds(List<String> eventsIds) {
        this.eventsIds = eventsIds;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "name: " + this.name + " & email: " + this.email + " & type: " + this.type + " & phone: " + this.phone;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if (uid != null)
            result.put("uid", uid);
        if (email != null)
            result.put("email", email);
        if (phone != null)
            result.put("phone", phone);
        if (lastName != null)
            result.put("lastName", lastName);
        if (birthDate != null)
            result.put("birthDate", birthDate);
        if (gender != null)
            result.put("gender", gender);
        if (name != null)
            result.put("name", name);
        if (profilePicUrl != null)
            result.put("profilePicUrl", profilePicUrl);
        if (eventsIds != null)
            result.put("eventsIds", eventsIds);
        if (location != null)
            result.put("location", location);
        return result;
    }

}
