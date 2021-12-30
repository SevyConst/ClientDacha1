package com.company.models;

import java.util.List;

public class EventsResponse {
    private List<Long> eventsIdsDelivered;

    public void setEventsIdsDelivered(List<Long> eventsIdsDelivered) {
        this.eventsIdsDelivered = eventsIdsDelivered;
    }

    private long periodSent;

    public List<Long> getEventsIdsDelivered() {
        return eventsIdsDelivered;
    }

    public void setEventsArrayDeliveryConfirmation(List<Long> eventsIdsDelivered) {
        this.eventsIdsDelivered = eventsIdsDelivered;
    }

    public long getPeriodSent() {
        return periodSent;
    }

    public void setPeriodSent(long periodSent) {
        this.periodSent = periodSent;
    }
}
