package com.company;

import com.company.dao.DaoEvent;
import com.company.dao.EventsResponse;
import com.company.models.Event;
import com.company.models.Events;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Controller {

    private final Db db;
    private final httpClient httpClient;
    private final int deviceId;
    private final Logger logger;

    private int period;

    public static DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


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
        LocalDateTime dateTime = LocalDateTime.now();
        String nowStr = dateTime.format(DateFormatter);
        db.insertEvent("start", nowStr);
        updateAndSend();

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(period);
            } catch (InterruptedException e) {

                break;
            }

            dateTime = LocalDateTime.now();
            nowStr = dateTime.format(DateFormatter);
            db.insertEvent("ping", nowStr);
            updateAndSend();
        }
    }

    private void updateAndSend() {
        List<DaoEvent> daoEvents = db.selectNotApproved();
        Events events = daoEvents2ModelEvents(daoEvents);
        events.setDeviceId(deviceId);

        EventsResponse eventsResponse = httpClient.sendEvents(events);
        db.updateSentBool(daoEvents);
        if (null != eventsResponse && null != eventsResponse.getEventsIdsDelivered()) {
            db.updateSentApprovedBool(eventsResponse.getEventsIdsDelivered());

            if (null != eventsResponse.getPeriodSent()) {
                period = eventsResponse.getPeriodSent();
            }
        }
    }

    private Events daoEvents2ModelEvents(List<DaoEvent> daoEvents) {
        Events result = new Events();
        for (DaoEvent daoEvent: daoEvents) {
            Event event = new Event();

            event.setId(daoEvent.getId());
            event.setNameEvent(daoEvent.getNameEvent());
            event.setTimeEvent(daoEvent.getTimeEvent());
            event.setTemperature(daoEvent.getTemperature());
            event.setProcessor(daoEvent.getProcessor());
            event.setUsedMemory(daoEvent.getUsedMemory());
            event.setFreeMemory(daoEvent.getFreeMemory());
            event.setSent(daoEvent.isSent());
            event.setSentTime(daoEvent.getSentTime());
            event.setAdditInfo(daoEvent.getAdditInfo());
            result.getEvents().add(event);
        }

        return result;
    }
}
