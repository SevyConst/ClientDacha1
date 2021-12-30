package com.company;

import java.sql.*;
import java.time.Instant;

public class Sqlite {
    private final String url;

    Sqlite(String url) {
        this.url = url;
    }

    private void connectForRead(String selectSql) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSql)) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String SQL_INSERT_START = "INSERT INTO events (name_event, time_event) VALUES('start', ?)";
    void insertStart() {
        try (Connection connection = DriverManager
                .getConnection(url)) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_START)) {
                statement.setLong(1, Instant.now().toEpochMilli());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


