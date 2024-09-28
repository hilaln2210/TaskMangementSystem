package com.taskmanager.server;


import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                // Handle client request in a new thread or method
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
