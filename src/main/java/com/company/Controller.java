package com.company;

import com.company.models.Events;
import com.company.models.EventsResponse;

public class Controller {

    private final Sqlite sqlite;
    private final Client client;

    public Controller(Sqlite sqlite, Client client) {
        this.sqlite = sqlite;
        this.client = client;
    }

    void work() {
        sqlite.insertStart();

        Events events = sqlite.selectNotApproved();
        EventsResponse eventsResponse = client.sendEvents(events);
        if (null != eventsResponse) {
            sqlite.updateSentBool(events);
            sqlite.updateSentApproved(eventsResponse.getEventsIdsDelivered());
        }




        System.out.println("done!");
    }

}
