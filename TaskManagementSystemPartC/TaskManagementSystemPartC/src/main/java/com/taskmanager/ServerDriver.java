package com.taskmanager;

import com.taskmanager.server.Server;

public class ServerDriver {
    public static void main(String[] args) {
        Server server = new Server(34567);
        new Thread(server).start();  // Starts the server in a new thread
        System.out.println("Server started on port 34567");
    }
}
