package com.indeves.chmplinapp.Models;

/**
 * Created by khalid on 18/02/18.
 */

public class ProEventHistoryItem {
    String imageUrl, title, location, description;

    public ProEventHistoryItem(String imageUrl, String title, String location, String description) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.location = location;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
