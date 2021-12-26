package com.company;

import java.sql.*;
import java.util.Collections;

public class Sqlite {

    String url = "jdbc:sqlite:/Users/k.lopatko/My Projects/Sqlite/ibnDoom.db";

    private void connectForRead(String selectSql) {
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSql)) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


