package com.indeves.chmplinapp.Models;

import java.util.List;

/**
 * Created by khalid on 26/02/18.
 */

public class ProUserModel extends UserData {
    private long rate, locationLat, locationLong;
    private List<String> lastWorkPicsUrls;
    private List<PackageModel> packages;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public ProUserModel() {

    }

    public ProUserModel(String name, String email, String phone, String type, String lastName, String birthDate, String gender, String uid, List<String> eventsIds, String profilePicUrl, long rate, long locationLat, long locationLong, List<String> lastWorkPicsUrls, List<PackageModel> packages) {
        super(name, email, phone, type, lastName, birthDate, gender, uid, eventsIds, profilePicUrl);
        this.rate = rate;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.lastWorkPicsUrls = lastWorkPicsUrls;
        this.packages = packages;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public long getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(long locationLat) {
        this.locationLat = locationLat;
    }

    public long getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(long locationLong) {
        this.locationLong = locationLong;
    }

    public List<String> getLastWorkPicsUrls() {
        return lastWorkPicsUrls;
    }

    public void setLastWorkPicsUrls(List<String> lastWorkPicsUrls) {
        this.lastWorkPicsUrls = lastWorkPicsUrls;
    }

    public List<PackageModel> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageModel> packages) {
        this.packages = packages;
    }
}