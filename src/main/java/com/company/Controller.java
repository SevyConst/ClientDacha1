package com.company;

import com.company.dao.DaoEvent;
import com.company.dao.EventsResponse;
import com.company.models.ModelEvent;
import com.company.models.ModelEvents;
import org.slf4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
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
        ModelEvents modelEvents = daoEvents2ModelEvents(daoEvents);
        modelEvents.setDeviceId(deviceId);

        EventsResponse eventsResponse = httpClient.sendEvents(modelEvents);
        db.updateSentBool(daoEvents);
        if (null != eventsResponse && null != eventsResponse.getEventsIdsDelivered()) {
            db.updateSentApprovedBool(eventsResponse.getEventsIdsDelivered());

            if(null != eventsResponse.getPeriodSent()) {
                period = eventsResponse.getPeriodSent();
            }
        }
    }

    private ModelEvents daoEvents2ModelEvents(List<DaoEvent> daoEvents) {
        ModelEvents result = new ModelEvents();
        for (DaoEvent daoEvent: daoEvents) {
            ModelEvent modelEvent = new ModelEvent();

            modelEvent.setId(daoEvent.getId());
            modelEvent.setNameEvent(daoEvent.getNameEvent());

            modelEvent.setTimeEvent(
                    dateFromMillis2String(
                            daoEvent.getTimeEvent()));

            modelEvent.setTemperature(daoEvent.getTemperature());
            modelEvent.setProcessor(daoEvent.getProcessor());
            modelEvent.setUsedMemory(daoEvent.getUsedMemory());
            modelEvent.setFreeMemory(daoEvent.getFreeMemory());
            modelEvent.setSent(daoEvent.isSent());

            modelEvent.setSentTime(
                    dateFromMillis2String(
                            daoEvent.getSentTime()));

            modelEvent.setAdditInfo(daoEvent.getAdditInfo());

            result.getEvents().add(modelEvent);
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
