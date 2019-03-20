package com.ozzot.networking.server;

import com.ozzot.networking.jdbc.JdbcService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ServerHandler {

    public static void handle(Socket socket) throws SQLException, IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            while (true) {

                String jsonObject = getJsonFromInputStream(reader);

                if (jsonObject != null && isJsonValid(jsonObject)) {

                    JSONObject jsonClientRequest = new JSONObject(jsonObject);

                    Object jsonServerResponse = JdbcService.getDataFromDatabase(jsonClientRequest);
                    writer.write(jsonServerResponse.toString() + "\n");
                    writer.flush();

                } else {
                    writer.write("JSON Object not valid: "+jsonObject + "\n");
                    writer.flush();
                    break;
                }
            }
        }
    }

    private static String getJsonFromInputStream(BufferedReader reader) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            stringBuilder.append(line.trim());
        }

        return stringBuilder.toString();
    }

    static boolean isJsonValid(String line) {
        try {
            new JSONObject(line);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }
}
