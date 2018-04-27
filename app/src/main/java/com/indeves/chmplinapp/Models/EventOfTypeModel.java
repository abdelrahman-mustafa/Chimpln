package com.indeves.chmplinapp.Models;

import java.util.ArrayList;

/**
 * Created by khalid on 27/04/18.
 */

public class EventOfTypeModel {
    private String eventType;
    private ArrayList<EventModel> events;

    public EventOfTypeModel() {
    }

    public EventOfTypeModel(String eventType, ArrayList<EventModel> events) {
        this.eventType = eventType;
        this.events = events;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public ArrayList<EventModel> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventModel> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return this.eventType + ": " + this.events;
    }
}
