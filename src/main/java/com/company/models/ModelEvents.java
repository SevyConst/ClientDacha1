package com.company.models;

import java.util.ArrayList;
import java.util.List;

public class ModelEvents {
    private List<ModelEvent> modelEvents = new ArrayList<>();
    private int deviceId;

    public List<ModelEvent> getEvents() {
        return modelEvents;
    }

    public void setEvents(List<ModelEvent> modelEvents) {
        this.modelEvents = modelEvents;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}