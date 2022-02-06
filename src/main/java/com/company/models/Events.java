package com.company.models;

import java.util.ArrayList;
import java.util.List;

public class Events {
    private List<Event> events = new ArrayList<>();
    private int deviceId;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}