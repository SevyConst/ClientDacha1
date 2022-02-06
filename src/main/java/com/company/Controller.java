package com.company;

import com.company.dao.DaoEvent;
import com.company.dao.EventsResponse;
import com.company.models.Event;
import com.company.models.Events;
import org.slf4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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

    private void updateAndSend() {
        List<DaoEvent> daoEvents = db.selectNotApproved();
        Events events = daoEvents2ModelEvents(daoEvents);
        events.setDeviceId(deviceId);

        EventsResponse eventsResponse = httpClient.sendEvents(events);
        db.updateSentBool(daoEvents);
        if (null != eventsResponse && null != eventsResponse.getEventsIdsDelivered()) {
            db.updateSentApprovedBool(eventsResponse.getEventsIdsDelivered());

            if(null != eventsResponse.getPeriodSent()) {
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

            event.setTimeEvent(
                    dateFromMillis2String(
                            daoEvent.getTimeEvent()));

            event.setTemperature(daoEvent.getTemperature());
            event.setProcessor(daoEvent.getProcessor());
            event.setUsedMemory(daoEvent.getUsedMemory());
            event.setFreeMemory(daoEvent.getFreeMemory());
            event.setSent(daoEvent.isSent());

            event.setSentTime(
                    dateFromMillis2String(
                            daoEvent.getSentTime()));

            event.setAdditInfo(daoEvent.getAdditInfo());

            result.getEvents().add(event);
        }

        return result;
    }

    private String dateFromMillis2String(Long epochTime) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS Z");
        format.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));
        Date date = new Date(epochTime);
        return format.format(date);
    }
}
