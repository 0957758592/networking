package com.ozzot.networking.jdbc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static com.ozzot.networking.constants.Constants.*;
import static org.junit.Assert.*;

public class JdbcServiceTest {

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

        assertEquals(JSON_ARRAY.toString(), JdbcService.getDataFromDatabase(jsonRequest));
    }

    @Test
    public void getObjectDataFromDatabase() throws SQLException {
        jsonRequest = new JSONObject("{\"id\":2,\"type\":\"getById\"}");

        assertEquals(JSON_OBJECT.toString(), JdbcService.getDataFromDatabase(jsonRequest));
    }

    @Test
    public void JsonTypeError() throws SQLException {
        jsonRequest = new JSONObject("{\"id\":2,\"typ\":\"getById\"}");

        String jsonTypeError = "{\"JSON has no key type => Error => \":{\"typ\":\"getById\",\"id\":2}}";

        assertEquals(jsonTypeError, JdbcService.getDataFromDatabase(jsonRequest));
    }

    @Test
    public void JsonIdError() throws SQLException {
        jsonRequest = new JSONObject("{\"ids\":2,\"type\":\"getById\"}");

        String jsonTypeError = "{\" key 'ID' not found => Error => \":{\"ids\":2,\"type\":\"getById\"}}";

        assertEquals(jsonTypeError, JdbcService.getDataFromDatabase(jsonRequest));
    }

    @Test(expected = JSONException.class)
    public void JsonError() throws SQLException {
        jsonRequest = new JSONObject("{\"id\":2,\"type\":\"getById\"");

    }
}