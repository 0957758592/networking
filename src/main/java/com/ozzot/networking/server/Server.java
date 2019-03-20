package com.ozzot.networking.server;

import static com.ozzot.networking.constants.Constants.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {

    public static void main(String[] args) throws IOException, SQLException {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                try (Socket socket = serverSocket.accept()) {

                    ServerHandler.handle(socket);

                }
            }
        }

    }

}