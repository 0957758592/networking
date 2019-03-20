package com.ozzot.networking.client;

import java.io.*;
import java.net.Socket;

import static com.ozzot.networking.constants.Constants.*;

public class Client {

    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket(HOST, PORT);
             FileReader file = new FileReader("src/main/java/com/ozzot/networking/client/getAll.txt");
//             FileReader file = new FileReader("src/main/java/com/ozzot/networking/client/getById.txt");
             BufferedReader fileReader = new BufferedReader(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                writer.write(line + "\n");
                writer.flush();
            }
            writer.newLine();
            writer.flush();

            System.out.println("Server response: " + reader.readLine());

        }
    }

}
