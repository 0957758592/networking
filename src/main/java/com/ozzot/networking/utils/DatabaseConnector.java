package com.ozzot.networking.utils;

import com.ozzot.networking.jdbc.JDBC;
import org.json.JSONObject;

import java.sql.*;

import static com.ozzot.networking.constants.Constants.*;

public class DatabaseConnector {

    public static Object getDataFromDatabase(Connection connection, JSONObject jsonRequest) throws SQLException {

        JSONObject jsonResponse = new JSONObject();

        PreparedStatement preparedStatement;

        if (jsonRequest.has("type")) {

            String keyParam = jsonRequest.getString("type");

            if (keyParam.equals("getAll")) {

                preparedStatement = connection.prepareStatement(QUERY);
                return JDBC.getDatabaseTableData(preparedStatement, true);

            } else if (keyParam.equals("getById")) {

                preparedStatement = connection.prepareStatement(QUERY_BY_ID);

                if (jsonRequest.toString().contains("id")) {
                    preparedStatement.setInt(1, jsonRequest.getInt("id"));
                    return JDBC.getDatabaseTableData(preparedStatement, false);
                }

            } else {

                return jsonResponse.put("", "");
            }
        } else {

            return jsonResponse.put("", "");
        }

        return jsonResponse;
    }

}
