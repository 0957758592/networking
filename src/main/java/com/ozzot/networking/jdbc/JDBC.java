package com.ozzot.networking.jdbc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JDBC {
    public static void getTableData(PreparedStatement preparedStatement, JSONObject jsonResponse, boolean array) throws SQLException {

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
