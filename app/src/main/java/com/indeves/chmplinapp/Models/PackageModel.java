package com.indeves.chmplinapp.Models;

import java.io.Serializable;

/**
 * Created by khalid on 27/02/18.
 */

public class PackageModel implements Serializable {
    private String packageTitle, packageDescription;
    private int eventTypeId, eventTimeId, price;

    public PackageModel() {
    }

    public PackageModel(String packageTitle, String packageDescription, int eventTypeId, int eventTimeId, int price) {
        this.packageTitle = packageTitle;
        this.packageDescription = packageDescription;
        this.eventTypeId = eventTypeId;
        this.eventTimeId = eventTimeId;
        this.price = price;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public int getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public int getEventTimeId() {
        return eventTimeId;
    }

    public void setEventTimeId(int eventTimeId) {
        this.eventTimeId = eventTimeId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        if (this.price==0){return this.packageTitle ;}
        else{return this.packageTitle + ",  " + this.price;}
    }
}
