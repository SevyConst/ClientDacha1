package com.company;

import com.company.data.Events;
import com.company.data.EventsResponse;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Controller {

    private final Db db;
    private final httpClient httpClient;
    private final int deviceId;
    private final Logger logger;

    private int period;

    public Controller(Db db, httpClient httpClient,
                      int deviceId, int period,
                      Logger logger) {
        this.db = db;
        this.httpClient = httpClient;
        this.deviceId = deviceId;
        this.period = period;
        this.logger = logger;
    }

    void launch() {
        db.insertEvent("start", Instant.now().toEpochMilli());
        updateAndSend();

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(period);
            } catch (InterruptedException e) {

                break;
            }

            db.insertEvent("ping", Instant.now().toEpochMilli());
            updateAndSend();
        }
    }

    void updateAndSend() {
        Events events = db.selectNotApproved();
        events.setDeviceId(deviceId);
        EventsResponse eventsResponse = httpClient.sendEvents(events);
        if (null != eventsResponse && null != eventsResponse.getEventsIdsDelivered()) {
            db.updateSentBool(events);
            db.updateSentApprovedBool(eventsResponse.getEventsIdsDelivered());

            if(null != eventsResponse.getPeriodSent()) {
                period = eventsResponse.getPeriodSent();
            }
        }
    }
}
