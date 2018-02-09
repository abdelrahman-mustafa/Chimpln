package com.indeves.chmplinapp.Controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PhotographerData {
    private String name;
    private String eventType;
    private String time;
    private String dateDay;
    private String dateMonth;
    private String shareOption;

    public PhotographerData(String name, String eventType, String time, String dateDay, String dateMonth, String shareOption) {
        this.name = name;
        this.eventType = eventType;
        this.time = time;
        this.dateDay = dateDay;
        this.dateMonth = dateMonth;
        this.shareOption = shareOption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
        this.dateDay = dateDay;
    }

    public String getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(String dateMonth) {
        this.dateMonth = dateMonth;
    }

    public String getShareOption() {
        return shareOption;
    }

    public void setShareOption(String shareOption) {
        this.shareOption = shareOption;
    }
}
