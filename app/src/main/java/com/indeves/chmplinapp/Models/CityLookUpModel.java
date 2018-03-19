package com.indeves.chmplinapp.Models;

/**
 * Created by khalid on 19/03/18.
 */

public class CityLookUpModel {
    private int countryId, id;
    private String englishName;

    public CityLookUpModel() {
    }

    public CityLookUpModel(int countryId, int id, String englishName) {
        this.countryId = countryId;
        this.id = id;
        this.englishName = englishName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    @Override
    public String toString() {
        return englishName;
    }
}