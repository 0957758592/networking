package com.ozzot.networking.jdbc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

import static com.ozzot.networking.constants.Constants.*;

public class JdbcService {

    public static String getDataFromDatabase(JSONObject jsonRequest) throws SQLException {

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            if (jsonRequest.has(TYPE)) {

                String keyParam = jsonRequest.getString(TYPE);

                if (keyParam.equals(GET_ALL)) {

                    return getAll(connection);

                } else if (keyParam.equals(GET_BY_ID)) {

                    return getById(connection, jsonRequest);

                } else {
                    throw new JSONException("JSON value has Error");
                }
            }
        }

        throw new JSONException("JSON key '" + TYPE + "' has Error");
    }

    private static String getById(Connection connection, JSONObject jsonRequest) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(QUERY_BY_ID);

        if (!jsonRequest.has(ID)) {
            throw new JSONException("ID not found");
        }

        preparedStatement.setInt(1, jsonRequest.getInt(ID));
        ResultSet resultSet = preparedStatement.executeQuery();
        JSONObject object = new JSONObject();

        while (resultSet.next()) {
            object = getJsonObject(resultSet, new JSONObject());
        }

        return object.toString();


    }

    private static String getAll(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QUERY);

        ResultSet resultSet = preparedStatement.executeQuery();


        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {
            jsonArray.put(getJsonObject(resultSet, new JSONObject()));
        }

        return jsonArray.toString();
    }


    private static JSONObject getJsonObject(ResultSet resultSet, JSONObject jsonObject) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnLength = resultSetMetaData.getColumnCount();

        for (int i = 1; i <= columnLength; i++) {
            jsonObject.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
        }

        return jsonObject;
    }

}
