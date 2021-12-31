package com.company;

import com.company.models.Events;
import com.company.models.EventsResponse;

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
        sqlite.insertStart();
        updateAndSend();
        while (true) {

            try {
                TimeUnit.SECONDS.sleep(periodSentSec);
            } catch (InterruptedException e) {
                break;
            }
            sqlite.insertPing();
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

}
