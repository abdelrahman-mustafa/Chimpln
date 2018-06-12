package com.indeves.chmplinapp.Models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khalid on 26/02/18.
 */

public class EventModel implements Serializable {
    private String bookerUserId, photographerId, eventDate, bookerUserName, photographerName, startTime, endTime, noteToPro, eventCity, eventId, rejectionReason;
    private long eventLocationLong, eventLocationLat;
    //eventStatus is one of these values "pending, accepted, rejected, finished, cancelled, booked-cash, booked-credit"
    private String eventStatus;
    private int typeId, timeId, sharingOptionId;
    private PackageModel selectedPackage;
    private List<String> eventImagesUrls;
    private double paidAmount;
    private boolean isRated;

    public EventModel() {
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    //this constructor contains the main data that user booking must have
    public EventModel(String bookerUserId, String photographerId, String eventDate, String bookerUserName, String photographerName, String startTime, String endTime, String noteToPro, String eventCity, long eventLocationLong, long eventLocationLat, String eventStatus, int typeId, int timeId, int sharingOptionId, PackageModel selectedPackage, Boolean isRated) {
        this.bookerUserId = bookerUserId;
        this.photographerId = photographerId;
        this.eventDate = eventDate;
        this.bookerUserName = bookerUserName;
        this.photographerName = photographerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noteToPro = noteToPro;
        this.eventCity = eventCity;
        this.eventLocationLong = eventLocationLong;
        this.eventLocationLat = eventLocationLat;
        this.eventStatus = eventStatus;
        this.typeId = typeId;
        this.timeId = timeId;
        this.sharingOptionId = sharingOptionId;
        this.selectedPackage = selectedPackage;
        this.isRated = isRated;
    }

    //this constructor contains the main data that user booking must have
    public EventModel(String bookerUserId, String photographerId, String eventDate, String bookerUserName, String photographerName, String startTime, String endTime, String noteToPro, String eventCity, long eventLocationLong, long eventLocationLat, String eventStatus, int typeId, int timeId, int sharingOptionId) {
        this.bookerUserId = bookerUserId;
        this.photographerId = photographerId;
        this.eventDate = eventDate;
        this.bookerUserName = bookerUserName;
        this.photographerName = photographerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noteToPro = noteToPro;
        this.eventCity = eventCity;
        this.eventLocationLong = eventLocationLong;
        this.eventLocationLat = eventLocationLat;
        this.eventStatus = eventStatus;
        this.typeId = typeId;
        this.timeId = timeId;
        this.sharingOptionId = sharingOptionId;
    }


    //This constructor contains all data to be retrieved.
    public EventModel(String bookerUserId, String photographerId, String eventDate, String bookerUserName, String photographerName, String startTime, String endTime, String noteToPro, String eventCity, String eventId, String rejectionReason, long eventLocationLong, long eventLocationLat, String eventStatus, int typeId, int timeId, int sharingOptionId, PackageModel selectedPackage, List<String> eventImagesUrls, double paidAmount) {
        this.bookerUserId = bookerUserId;
        this.photographerId = photographerId;
        this.eventDate = eventDate;
        this.bookerUserName = bookerUserName;
        this.photographerName = photographerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noteToPro = noteToPro;
        this.eventCity = eventCity;
        this.eventId = eventId;
        this.rejectionReason = rejectionReason;
        this.eventLocationLong = eventLocationLong;
        this.eventLocationLat = eventLocationLat;
        this.eventStatus = eventStatus;
        this.typeId = typeId;
        this.timeId = timeId;
        this.sharingOptionId = sharingOptionId;
        this.selectedPackage = selectedPackage;
        this.eventImagesUrls = eventImagesUrls;
        this.paidAmount = paidAmount;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNoteToPro() {
        return noteToPro;
    }

    public void setNoteToPro(String noteToPro) {
        this.noteToPro = noteToPro;
    }

    public String getEventCity() {
        return eventCity;
    }

    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public int getSharingOptionId() {
        return sharingOptionId;
    }

    public void setSharingOptionId(int sharingOptionId) {
        this.sharingOptionId = sharingOptionId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public PackageModel getSelectedPackage() {
        return selectedPackage;
    }

    public void setSelectedPackage(PackageModel selectedPackage) {
        this.selectedPackage = selectedPackage;
    }

    public List<String> getEventImagesUrls() {
        return eventImagesUrls;
    }

    public void setEventImagesUrls(List<String> eventImagesUrls) {
        this.eventImagesUrls = eventImagesUrls;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if (eventStatus != null)
            result.put("eventStatus", eventStatus);
        if (eventImagesUrls != null)
            result.put("eventImagesUrls", eventImagesUrls);
        if (rejectionReason != null)
            result.put("rejectionReason", rejectionReason);
        if (paidAmount > 0) {
            result.put("paidAmount", paidAmount);
        }
        result.put("isRated", isRated);
        return result;
    }
}