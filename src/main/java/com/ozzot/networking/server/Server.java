package com.ozzot.networking.server;

import static com.ozzot.networking.constants.Constants.*;

import com.ozzot.networking.utils.DatabaseConnector;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {

    public static void main(String[] args) throws IOException, SQLException {

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket socket = serverSocket.accept();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                while (true) {

                    String jsonObject = getJsonFromInputStream(reader);

                    if (jsonObject != null && isJsonValid(jsonObject)) {

                        JSONObject jsonClientRequest = new JSONObject(jsonObject);

                        Object jsonServerResponse = DatabaseConnector.getDataFromDatabase(connection, jsonClientRequest);
                        writer.write(jsonServerResponse.toString() + "\n");
                        writer.flush();

                    } else {
                        System.out.println("Client disconnected");
                        break;
                    }
                }
            }
        }
    }

    private static String getJsonFromInputStream(BufferedReader reader) throws IOException {

        String line;
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            if (line.contains("}")) {
                return stringBuilder.append(line).toString();
            }
            stringBuilder.append(line);
        }

        return null;
    }

    private static boolean isJsonValid(String line) {
        try {
            new JSONObject(line);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }
}