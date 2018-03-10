package com.indeves.chmplinapp.Models;

/**
 * Created by khalid on 10/03/18.
 */

public class LookUpModel {
    private int id;
    private String englishName;

    public LookUpModel() {
    }

    public LookUpModel(int id, String englishName) {
        this.id = id;
        this.englishName = englishName;
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
        return this.englishName;
    }
}
