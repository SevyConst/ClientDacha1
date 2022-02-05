package com.company;

import com.company.dao.DaoEvent;
import org.slf4j.Logger;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Db {
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

    Db(String url, Logger logger) {
        this.url = url;
        this.logger = logger;
    }

    private static final String SQL_INSERT_EVENT = "INSERT INTO events (name_event, time_event) VALUES(?, ?)";
    void insertEvent(String nameEvent, long time) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_EVENT)) {

            statement.setString(1, nameEvent);
            statement.setLong(2, time);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("can't insert start event to sqlite", e);
        }
    }

    private static final String SQL_SELECT_NOT_APPROVED =
            "SELECT * FROM events WHERE sent_approved <> 'Y' ORDER BY time_event";
    List<DaoEvent> selectNotApproved() {
        List<DaoEvent> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_NOT_APPROVED)) {

            while (resultSet.next()) {
                DaoEvent daoEvent = new DaoEvent();

                daoEvent.setId(resultSet.getLong(COLUMN_ID));
                daoEvent.setNameEvent(resultSet.getString(COLUMN_NAME_EVENT));
                daoEvent.setTimeEvent(resultSet.getLong(COLUMN_TIME_EVENT));
                daoEvent.setSent(trueStr.equals(resultSet.getString(COLUMN_SENT)));
                daoEvent.setTemperature(resultSet.getInt(COLUMN_TEMPERATURE));
                daoEvent.setProcessor(resultSet.getInt(COLUMN_PROCESSOR));
                daoEvent.setUsedMemory(resultSet.getInt(COLUMN_USED_MEMORY));
                daoEvent.setFreeMemory(resultSet.getInt(COLUMN_FREE_MEMORY));
                daoEvent.setSentTime(resultSet.getLong(COLUMN_SENT_TIME));
                daoEvent.setSentApproved(trueStr.equals(resultSet.getString(COLUMN_SENT_APPROVED)));
                daoEvent.setSentApprovedTime(resultSet.getLong(COLUMN_SEN_APPROVED_TIME));
                daoEvent.setAdditInfo(resultSet.getString(COLUMN_ADDIT_INFO));

                result.add(daoEvent);
            }

            return result;

        } catch (SQLException e) {
            logger.error("can't select from sqlite", e);
            return null;
        }
    }

    void updateSentBool(List<DaoEvent> events) {

        StringBuilder sqlUpdate = new StringBuilder("UPDATE events SET sent = 'Y', sent_time = ? WHERE id in (");
        sqlUpdate.append(events.stream().
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
}