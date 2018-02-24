package com.indeves.chmplinapp.Models;

public class UserData {


    public String name;
    public String email;
    public String phone;
    public String type;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UserData() {
    }

    public UserData(String name, String email,String phone,String type) {
        this.name = name;
        this.email = email;
        this.type=type;
        this.phone = phone;
    }

}
