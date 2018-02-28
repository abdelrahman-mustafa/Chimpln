package com.indeves.chmplinapp.Models;

/**
 * Created by khalid on 26/02/18.
 */

public class EventModel {
    private String bookerUserId, photographerId, eventDate, eventType, bookerUserName, photographerName, eventTime, shareOption, noteToPro;
    private long eventLocationLong, eventLocationLat;
    //eventStatus is one of these values "pending, accepted, rejected, finished"
    private String eventStatus;

    public EventModel() {
    }

    public EventModel(String bookerUserId, String photographerId, String eventDate, String eventType, String bookerUserName, String photographerName, String eventTime, String shareOption, String noteToPro, long eventLocationLong, long eventLocationLat, String eventStatus) {
        this.bookerUserId = bookerUserId;
        this.photographerId = photographerId;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.bookerUserName = bookerUserName;
        this.photographerName = photographerName;
        this.eventTime = eventTime;
        this.shareOption = shareOption;
        this.noteToPro = noteToPro;
        this.eventLocationLong = eventLocationLong;
        this.eventLocationLat = eventLocationLat;
        this.eventStatus = eventStatus;
    }

    public EventModel(String bookerUserId, String photographerId, String eventDate, String eventType, String bookerUserName, String photographerName, String eventTime, String shareOption, long eventLocationLong, long eventLocationLat, String eventStatus) {
        this.bookerUserId = bookerUserId;
        this.photographerId = photographerId;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.bookerUserName = bookerUserName;
        this.photographerName = photographerName;
        this.eventTime = eventTime;
        this.shareOption = shareOption;
        this.eventLocationLong = eventLocationLong;
        this.eventLocationLat = eventLocationLat;
        this.eventStatus = eventStatus;
    }

    public String getBookerUserId() {
        return bookerUserId;
    }

    public void setBookerUserId(String bookerUserId) {
        this.bookerUserId = bookerUserId;
    }

    public String getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(String photographerId) {
        this.photographerId = photographerId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getBookerUserName() {
        return bookerUserName;
    }

    public void setBookerUserName(String bookerUserName) {
        this.bookerUserName = bookerUserName;
    }

    public String getPhotographerName() {
        return photographerName;
    }

    public void setPhotographerName(String photographerName) {
        this.photographerName = photographerName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getShareOption() {
        return shareOption;
    }

    public void setShareOption(String shareOption) {
        this.shareOption = shareOption;
    }

    public long getEventLocationLong() {
        return eventLocationLong;
    }

    public void setEventLocationLong(long eventLocationLong) {
        this.eventLocationLong = eventLocationLong;
    }

    public long getEventLocationLat() {
        return eventLocationLat;
    }

    public void setEventLocationLat(long eventLocationLat) {
        this.eventLocationLat = eventLocationLat;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getNoteToPro() {
        return noteToPro;
    }

    public void setNoteToPro(String noteToPro) {
        this.noteToPro = noteToPro;
    }
}
