package com.company;

import com.company.models.Event;
import com.company.models.Events;
import com.company.models.EventsResponse;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Controller {

    private final Sqlite sqlite;
    private final Client client;

    public Controller(Sqlite sqlite, Client client) {
        this.sqlite = sqlite;
        this.client = client;
    }

    private int periodSentSec = 10;

    void work() {

        sqlite.insertStart(fillEvent());
        updateAndSend();
        while (true) {

            try {
                TimeUnit.SECONDS.sleep(periodSentSec);
            } catch (InterruptedException e) {
                break;
            }
            sqlite.insertPing(fillEvent());
            updateAndSend();

        }

    }

    void updateAndSend() {
        Events events = sqlite.selectNotApproved();
        EventsResponse eventsResponse = client.sendEvents(events);
        if (null != eventsResponse && null != eventsResponse.getEventsIdsDelivered()) {
            sqlite.updateSentBool(events);
            sqlite.updateSentApprovedBool(eventsResponse.getEventsIdsDelivered());

            if(null != eventsResponse.getPeriodSent()) {
                periodSentSec = eventsResponse.getPeriodSent();
            }
        }
    }

    private Event fillEvent() {
        Event event = new Event();
        event.setTimeEvent(Instant.now().toEpochMilli());
        return event;
    }
}
