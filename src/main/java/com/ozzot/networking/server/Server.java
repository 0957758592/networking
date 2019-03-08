package com.ozzot.networking.server;

import static com.ozzot.networking.constants.Constants.*;

import com.ozzot.networking.utils.DatabaseConnector;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {

    public static void main(String[] args) throws IOException, SQLException {

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {


            while (true) {

                String line = reader.readLine();

                if (line != null) {

                    JSONObject jsonClientRequest = new JSONObject(line);

                    JSONObject jsonServerResponse = (JSONObject) DatabaseConnector.getDataFromDatabase(connection, jsonClientRequest);
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
