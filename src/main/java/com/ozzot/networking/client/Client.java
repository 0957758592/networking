package com.ozzot.networking.client;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

import static com.ozzot.networking.constants.Constants.*;

public class Client {

    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            JSONObject jsonRequest = new JSONObject();

//            System.out.println(jsonRequest.put("type", "getAll").toString());
            System.out.println(jsonRequest.put("type", "getById").put("id", 2).toString());

            while (true) {
                writer.write(jsonRequest.toString() + "\n");
                writer.flush();

                JSONObject jsonServerResponse = new JSONObject(reader.readLine());
                System.out.println("Server's respond: " + jsonServerResponse);

                if (scanner.readLine().equals("@")) {
                    break;
                }
            }
        }
    }

}
