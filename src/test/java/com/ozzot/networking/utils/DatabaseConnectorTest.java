package com.ozzot.networking.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static com.ozzot.networking.constants.Constants.*;
import static org.junit.Assert.*;

public class DatabaseConnectorTest {

    private static final String JSON_OBJECT = "{\"user\":{\"name\":\"имя\",\"driverLicense\":false," +
            "\"dateOfBirth\":\"1970-01-01\",\"address\":\"что-то там адресс\",\"id\":2,\"salary\":30000.55}}";

    private static final String JSON_ARRAY = "{\"user\":[{\"name\":\"name\",\"driverLicense\":true,\"dateOfBirth\":\"1970-01-08\"," +
            "\"address\":\"address\",\"id\":1,\"salary\":20000.555},{\"name\":\"имя\",\"driverLicense\":false," +
            "\"dateOfBirth\":\"1970-01-01\",\"address\":\"что-то там адресс\",\"id\":2,\"salary\":30000.55}]}";


    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private JSONObject jsonObject;

    private Connection connection;
    private PreparedStatement prepareStatement;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private Map<String, Object> tableData;

    @Before
    public void beforeClass() throws IOException, SQLException {
        serverSocket = new ServerSocket(PORT);
        clientSocket = new Socket(HOST, PORT);

        Socket socket = serverSocket.accept();
        printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        tableData = new HashMap<>();
        jsonObject = new JSONObject();

    }

    @After
    public void after() throws IOException, SQLException {
        bufferedReader.close();
        printWriter.close();
        clientSocket.close();
        serverSocket.close();
        connection.close();

    }

    @Test
    public void isConnection() throws SQLException {
        assertTrue(!connection.isClosed());
        assertTrue(serverSocket.isBound());
        assertTrue(clientSocket.isConnected());
    }

    @Test
    public void getArrayDataFromDatabase() throws SQLException {
        //prepare
        prepareStatement = connection.prepareStatement(QUERY);
        resultSet = prepareStatement.executeQuery();
        resultSetMetaData = resultSet.getMetaData();

        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                tableData.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
            }
            jsonArray.put(tableData);
        }
        jsonObject.put(resultSetMetaData.getTableName(1), jsonArray);

        printWriter.println(tableData);

        //then
        assertEquals(jsonObject.toString(), JSON_ARRAY);
    }

    @Test
    public void getObjectDataFromDatabase() throws SQLException {
        //prepare
        prepareStatement = connection.prepareStatement(QUERY.replace(";", " ") + "WHERE id = ?");
        prepareStatement.setInt(1, 2);
        resultSet = prepareStatement.executeQuery();
        resultSetMetaData = resultSet.getMetaData();

        while (resultSet.next()) {

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                tableData.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
            }
        }
        jsonObject.put(resultSetMetaData.getTableName(1), tableData);

        printWriter.println(tableData);

        //then
        assertEquals(jsonObject.toString(), JSON_OBJECT);
    }
}