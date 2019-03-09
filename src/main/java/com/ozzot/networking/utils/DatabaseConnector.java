package com.ozzot.networking.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.ozzot.networking.constants.Constants.QUERY;


public class DatabaseConnector {


    public static Object getDataFromDatabase(Connection connection, Object json) throws SQLException {

        JSONObject jsonRequest = new JSONObject(json.toString());
        Iterator<String> keys = jsonRequest.keys();

        JSONObject jsonResponse = new JSONObject();

        PreparedStatement preparedStatement;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.equals("type")) {

                if (jsonRequest.getString(key).equals("getAll")) {

                    preparedStatement = connection.prepareStatement(QUERY);
                    getTableData(preparedStatement, jsonResponse, true);

                } else if (jsonRequest.getString(key).equals("getById")) {

                    preparedStatement = connection.prepareStatement(QUERY.replace(";", " ") + "WHERE ID = ?");
                    preparedStatement.setInt(1, jsonRequest.getInt("id"));
                    getTableData(preparedStatement, jsonResponse, false);

                } else {

                    return jsonResponse.put("", "");
                }
            }
        }

        return jsonResponse;
    }

    private static void getTableData(PreparedStatement preparedStatement, JSONObject jsonResponse, boolean array) throws SQLException {

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        Map<String, Object> tableData = new HashMap<>();
        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {

                tableData.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
            }

            if (array) {
                jsonArray.put(tableData);
            }
        }

        Object jsonObject = array ? jsonArray : tableData;

        jsonResponse.put(resultSetMetaData.getTableName(1), jsonObject);
    }

}
