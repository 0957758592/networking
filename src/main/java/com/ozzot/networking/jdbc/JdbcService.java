package com.ozzot.networking.jdbc;

import org.json.JSONArray;
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
                    return new JSONObject().put("JSON should has type 'getAll' or 'getById' => Error => ", jsonRequest).toString();
                }
            }
            return new JSONObject().put("JSON has no key " + TYPE +" => Error => ", jsonRequest).toString();
        }

    }

    private static String getById(Connection connection, JSONObject jsonRequest) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(QUERY_BY_ID);

        if (!jsonRequest.has(ID)) {
            return new JSONObject().put(" key 'ID' not found => Error => ", jsonRequest).toString();
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
