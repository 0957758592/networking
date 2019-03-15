package com.ozzot.networking.client;

import java.io.*;
import java.net.Socket;

import static com.ozzot.networking.constants.Constants.*;

public class Client {

    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
             FileReader file = new FileReader("src/main/java/com/ozzot/networking/client/json.txt");
             BufferedReader fileReader = new BufferedReader(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            while (true) {
                System.out.println("+----------------------------------------+");
                System.out.println("| Press 'Enter' to read JSON for request |");
                System.out.println("+----------------------------------------+");

                if (scanner.readLine().equals("@")) {
                    break;
                }

                String json = fileReader.readLine();

                writer.write(json + "\n");
                writer.flush();

                if (json.contains("}")) {
                    String line = reader.readLine();
                    System.out.println("Server's respond: " + line);
                }

            }

        }
    }

}
