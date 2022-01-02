package com.company;

import com.company.models.Event;
import com.company.models.Events;
import org.slf4j.Logger;

import java.sql.*;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class Sqlite {
    private final String url;
    private final Logger logger;

    // Columns of the Events table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME_EVENT = "name_event";
    private static final String COLUMN_TIME_EVENT = "time_event";
    private static final String COLUMN_SENT = "sent";
    private static final String COLUMN_TEMPERATURE = "temperature";
    private static final String COLUMN_PROCESSOR = "processor";
    private static final String COLUMN_USED_MEMORY = "used_memory";
    private static final String COLUMN_FREE_MEMORY = "free_memory";
    private static final String COLUMN_SENT_TIME = "sent_time";
    private static final String COLUMN_SENT_APPROVED = "sent_approved";
    private static final String COLUMN_SEN_APPROVED_TIME = "sent_approved_time";
    private static final String COLUMN_ADDIT_INFO = "addit_info";

    private static final String trueStr = "Y";
    private static final String falseStr = "N";

    Sqlite(String url, Logger logger) {
        this.url = url;
        this.logger = logger;
    }

    private static final String SQL_INSERT_START = "INSERT INTO events (name_event, time_event) VALUES('start', ?)";
    void insertStart(Event event) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_START)) {

            statement.setLong(1, event.getTimeEvent());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("can't insert start event to sqlite", e);
        }
    }

    private static final String SQL_SELECT_NOT_APPROVED = "SELECT * FROM events WHERE sent_approved <> 'Y'";
    Events selectNotApproved() {
        Events events = new Events();
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_NOT_APPROVED)) {

            while (resultSet.next()) {
                Event event = new Event();

                event.setId(resultSet.getLong(COLUMN_ID));
                event.setNameEvent(resultSet.getString(COLUMN_NAME_EVENT));
                event.setTimeEvent(resultSet.getLong(COLUMN_TIME_EVENT));
                event.setSent(trueStr.equals(resultSet.getString(COLUMN_SENT)));
                event.setTemperature(resultSet.getInt(COLUMN_TEMPERATURE));
                event.setProcessor(resultSet.getInt(COLUMN_PROCESSOR));
                event.setUsedMemory(resultSet.getInt(COLUMN_USED_MEMORY));
                event.setFreeMemory(resultSet.getInt(COLUMN_FREE_MEMORY));
                event.setSentTime(resultSet.getLong(COLUMN_SENT_TIME));
                event.setSentApproved(trueStr.equals(resultSet.getString(COLUMN_SENT_APPROVED)));
                event.setSentApprovedTime(resultSet.getLong(COLUMN_SEN_APPROVED_TIME));
                event.setAdditInfo(resultSet.getString(COLUMN_ADDIT_INFO));

                events.getEvents().add(event);
            }

            return events;

        } catch (SQLException e) {
            logger.error("can't select from sqlite", e);
            return null;
        }
    }

    void updateSentBool(Events events) {

        StringBuilder sqlUpdate = new StringBuilder("UPDATE events SET sent = 'Y', sent_time = ? WHERE id in (");
        sqlUpdate.append(events.getEvents().stream().
                map(event -> String.valueOf(event.getId())).
                collect(Collectors.joining(", ")));
        sqlUpdate.append(");");

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(sqlUpdate.toString())) {
            statement.setLong(1, Instant.now().toEpochMilli());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("can't update info about sending (sqlite)", e);
        }
    }

    void updateSentApprovedBool(List<Long> ids) {

        StringBuilder sqlUpdate =
                new StringBuilder("UPDATE events SET sent_approved = 'Y', sent_approved_time = ? WHERE id in (");

        sqlUpdate.append(ids.stream().
                map(id -> String.valueOf(id)).
                collect(Collectors.joining(", ")));

        sqlUpdate.append(");");

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(sqlUpdate.toString())) {
            statement.setLong(1, Instant.now().toEpochMilli());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("can't update info about approval (sqlite)", e);

        }
    }

    private static final String SQL_INSERT_PING = "INSERT INTO events (name_event, time_event) VALUES('event', ?)";
    void insertPing(Event event) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PING)) {

            statement.setLong(1, event.getTimeEvent());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("can't insert info about ping (sqlite)", e);
        }
    }
}


