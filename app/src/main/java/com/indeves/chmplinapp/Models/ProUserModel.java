package com.indeves.chmplinapp.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khalid on 26/02/18.
 */

public class ProUserModel extends UserData implements Serializable {
    //pro could be a single photographer or a studio
    private long  locationLat, locationLong;
    private double rate;
    private List<String> lastWorkPicsUrls;
    private List<Integer> rates;
    private List<PackageModel> packages;
    private String country, city, area, workDayStart, workDayEnd, experience, idFrontImageUrl, idBackImageUrl;
    private ArrayList<StudioTeamMember> studioTeamMembers;
    private ArrayList<String> eventAvailablity;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public ProUserModel() {

    }

    public List<Integer> getRates() {
        return rates;
    }

    public void setRates(List<Integer> rates) {
        this.rates = rates;
    }

    public ArrayList<String> getEventAvailablity() {
        return eventAvailablity;
    }

    public void setEventAvailablity(ArrayList<String> eventAvailablity) {
        this.eventAvailablity = eventAvailablity;
    }

    public ProUserModel(String email, String phone, String type, String uid) {
        super(email, phone, type, uid);
    }

    public ProUserModel(String name, String email, String phone, String type, String lastName, String birthDate, String gender, String uid, String profilePicUrl, String location, List<String> eventsIds, double rate, long locationLat, long locationLong, List<String> lastWorkPicsUrls, List<PackageModel> packages, String country, String city, String area, String workDayStart, String workDayEnd, String experience) {
        super(name, email, phone, type, lastName, birthDate, gender, uid, profilePicUrl, location, eventsIds);
        this.rate = rate;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.lastWorkPicsUrls = lastWorkPicsUrls;
        this.packages = packages;
        this.country = country;
        this.city = city;
        this.area = area;
        this.workDayStart = workDayStart;
        this.workDayEnd = workDayEnd;
        this.experience = experience;
    }

    public ProUserModel(String name, String email, String phone, String type, String lastName, String birthDate, String gender, String uid, String profilePicUrl, String location, List<String> eventsIds, double rate, long locationLat, long locationLong, List<String> lastWorkPicsUrls, List<PackageModel> packages, String country, String city, String area, String workDayStart, String workDayEnd, String experience, ArrayList<StudioTeamMember> studioTeamMembers) {
        super(name, email, phone, type, lastName, birthDate, gender, uid, profilePicUrl, location, eventsIds);
        this.rate = rate;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.lastWorkPicsUrls = lastWorkPicsUrls;
        this.packages = packages;
        this.country = country;
        this.city = city;
        this.area = area;
        this.workDayStart = workDayStart;
        this.workDayEnd = workDayEnd;
        this.experience = experience;
        this.studioTeamMembers = studioTeamMembers;
    }

    public ProUserModel(String name, String email, String phone, String type, String lastName, String birthDate, String gender, String uid, String profilePicUrl, String location, List<String> eventsIds, double rate, long locationLat, long locationLong, List<String> lastWorkPicsUrls, List<PackageModel> packages, String country, String city, String area, String workDayStart, String workDayEnd, String experience, String idFrontImageUrl, String idBackImageUrl, ArrayList<StudioTeamMember> studioTeamMembers, ArrayList<String> eventAvailablity) {
        super(name, email, phone, type, lastName, birthDate, gender, uid, profilePicUrl, location, eventsIds);
        this.rate = rate;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.lastWorkPicsUrls = lastWorkPicsUrls;
        this.packages = packages;
        this.country = country;
        this.city = city;
        this.area = area;
        this.workDayStart = workDayStart;
        this.workDayEnd = workDayEnd;
        this.experience = experience;
        this.idFrontImageUrl = idFrontImageUrl;
        this.idBackImageUrl = idBackImageUrl;
        this.studioTeamMembers = studioTeamMembers;
        this.eventAvailablity = eventAvailablity;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWorkDayStart() {
        return workDayStart;
    }

    public void setWorkDayStart(String workDayStart) {
        this.workDayStart = workDayStart;
    }

    public String getWorkDayEnd() {
        return workDayEnd;
    }

    public void setWorkDayEnd(String workDayEnd) {
        this.workDayEnd = workDayEnd;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public ArrayList<StudioTeamMember> getStudioTeamMembers() {
        return studioTeamMembers;
    }

    public void setStudioTeamMembers(ArrayList<StudioTeamMember> studioTeamMembers) {
        this.studioTeamMembers = studioTeamMembers;
    }

    public String getIdFrontImageUrl() {
        return idFrontImageUrl;
    }

    public void setIdFrontImageUrl(String idFrontImageUrl) {
        this.idFrontImageUrl = idFrontImageUrl;
    }

    public String getIdBackImageUrl() {
        return idBackImageUrl;
    }

    public void setIdBackImageUrl(String idBackImageUrl) {
        this.idBackImageUrl = idBackImageUrl;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> result = super.toMap();
        if (rate != 0)
            result.put("rate", rate);
        if (locationLat != 0)
            result.put("locationLat", locationLat);
        if (locationLong != 0)
            result.put("locationLong", locationLong);
        if (lastWorkPicsUrls != null)
            result.put("lastWorkPicsUrls", lastWorkPicsUrls);
        if (packages != null)
            result.put("packages", packages);
        if (country != null)
            result.put("country", country);
        if (city != null)
            result.put("city", city);
        if (area != null)
            result.put("area", area);
        if (workDayStart != null)
            result.put("workDayStart", workDayStart);
        if (workDayEnd != null)
            result.put("workDayEnd", workDayEnd);
        if (experience != null)
            result.put("experience", experience);
        if (studioTeamMembers != null)
            result.put("studioTeamMembers", studioTeamMembers);
        if (rates != null)
            result.put("rates", rates);
        if (idFrontImageUrl != null) {
            result.put("idFrontImageUrl", idFrontImageUrl);
        }
        if (idBackImageUrl != null) {
            result.put("idBackImageUrl", idBackImageUrl);
        }
        return result;
    }
}