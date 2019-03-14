package com.ozzot.networking.utils;

import com.ozzot.networking.jdbc.JDBC;
import org.json.JSONObject;

import java.sql.*;
import java.util.Iterator;

import static com.ozzot.networking.constants.Constants.*;

public class DatabaseConnector {

    public static JSONObject getDataFromDatabase(Connection connection, JSONObject jsonRequest) throws SQLException {

        Iterator<String> keys = jsonRequest.keys();

        JSONObject jsonResponse = new JSONObject();

        PreparedStatement preparedStatement;

        while (keys.hasNext()) {
            String key = keys.next();

            System.out.println("KEY" + key);

            if (key.equals("type")) {

                String keyParam = jsonRequest.getString(key);

                if (keyParam.equals("getAll")) {

                    preparedStatement = connection.prepareStatement(QUERY);
                    JDBC.getTableData(preparedStatement, jsonResponse, true);

                } else if (keyParam.equals("getById")) {

                    preparedStatement = connection.prepareStatement(QUERY_BY_ID);
                    preparedStatement.setInt(1, jsonRequest.getInt("id"));
                    JDBC.getTableData(preparedStatement, jsonResponse, false);

                } else {

                    return jsonResponse.put("", "");
                }
            }
        }

        return jsonResponse;
    }


}
