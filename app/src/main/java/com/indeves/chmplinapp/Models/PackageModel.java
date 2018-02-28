package com.indeves.chmplinapp.Models;

/**
 * Created by khalid on 27/02/18.
 */

public class PackageModel {
    private String packageTitle, packageDescription, packagePrice, eventType, eventTime;

    public PackageModel() {
    }

    public PackageModel(String packageTitle, String packageDescription, String packagePrice, String eventType, String eventTime) {
        this.packageTitle = packageTitle;
        this.packageDescription = packageDescription;
        this.packagePrice = packagePrice;
        this.eventType = eventType;
        this.eventTime = eventTime;
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

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
