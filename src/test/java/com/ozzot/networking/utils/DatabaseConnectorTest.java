package com.ozzot.networking.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static com.ozzot.networking.constants.Constants.*;
import static org.junit.Assert.*;

public class DatabaseConnectorTest {

    private static final JSONObject JSON_OBJECT = new JSONObject("{\"address\":\"что-то там адресс\",\"name\":\"имя\",\"driverLicense\":false,\"dateOfBirth\":\"1970-01-01\",\"id\":2,\"salary\":30000.55}");

    private static final JSONArray JSON_ARRAY = new JSONArray("[{\"address\":\"address\",\"name\":\"name\",\"driverLicense\":true,\"dateOfBirth\":\"1970-01-08\",\"id\":1,\"salary\":20000.555}," +
            "{\"address\":\"что-то там адресс\",\"name\":\"имя\",\"driverLicense\":false,\"dateOfBirth\":\"1970-01-01\",\"id\":2,\"salary\":30000.55}]");

    private Connection connection;

    private JSONObject jsonRequest;

    @Before
    public void beforeClass() throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @After
    public void after() throws SQLException {
        connection.close();
    }

    @Test
    public void isConnection() throws SQLException {
        assertTrue(!connection.isClosed());
    }

    @Test
    public void getArrayDataFromDatabase() throws SQLException {
        jsonRequest = new JSONObject("{\"type\":\"getAll\"}");

//        assertEquals(JSON_ARRAY.toString(), DatabaseConnector.getDataFromDatabase(jsonRequest).toString());
    }

    @Test
    public void getObjectDataFromDatabase() throws SQLException {
        jsonRequest = new JSONObject("{\"id\":2,\"type\":\"getById\"}");

//        assertEquals(JSON_OBJECT.toString(), DatabaseConnector.getDataFromDatabase(jsonRequest).toString());
    }
}