package com.company.models;

import java.util.List;

public class EventsResponse {
    private List<Long> eventsIdsDelivered;

    public void setEventsIdsDelivered(List<Long> eventsIdsDelivered) {
        this.eventsIdsDelivered = eventsIdsDelivered;
    }

    private Integer periodSent;

    public List<Long> getEventsIdsDelivered() {
        return eventsIdsDelivered;
    }

    public void setEventsArrayDeliveryConfirmation(List<Long> eventsIdsDelivered) {
        this.eventsIdsDelivered = eventsIdsDelivered;
    }

    public Integer getPeriodSent() {
        return periodSent;
    }

    public void setPeriodSent(Integer periodSent) {
        this.periodSent = periodSent;
    }
}
