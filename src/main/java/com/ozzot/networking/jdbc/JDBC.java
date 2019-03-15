package com.ozzot.networking.jdbc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JDBC {

    public static Object getDatabaseTableData(PreparedStatement preparedStatement, boolean array) throws SQLException {

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        JSONObject tableData = new JSONObject();

        JSONArray jsonArray = new JSONArray();

        while (resultSet.next()) {

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {

                tableData.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
            }

            if (array) {
                jsonArray.put(tableData);
            }
        }

        return array ? jsonArray : tableData;

    }

}
