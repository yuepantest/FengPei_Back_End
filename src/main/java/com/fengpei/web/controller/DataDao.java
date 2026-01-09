package com.fengpei.web.controller;

import com.fengpei.web.entiry.Client;
import com.fengpei.web.tool.BusinessTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DataDao {


    public String TABLE_NAME = "fengpei.clients";
    public BusinessTool businessTool = new BusinessTool();

    public long addClientData(Client client, DataSource dataSource) {
        long id = 0;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            String sqlCheck = "SELECT 1 FROM " + TABLE_NAME + " WHERE identityCard = '" + client.identityCard + "' LIMIT 1";
            ResultSet resultSet = statement.executeQuery(sqlCheck);
            if (resultSet.next()) {
                return HelloController.REPETITION;
            }
            String sql = businessTool.getString(client, TABLE_NAME);
            int code = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (code == 1) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    if (generatedId > 0) {
                        id = generatedId;
                    }
                }
            }
            closeResource(statement, connection);
            return id;
        } catch (SQLException e) {
            closeResource(statement, connection);
            return id;
        }
    }

    public void closeResource(Statement statement, Connection connection) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ignored) {

        }
    }
}
