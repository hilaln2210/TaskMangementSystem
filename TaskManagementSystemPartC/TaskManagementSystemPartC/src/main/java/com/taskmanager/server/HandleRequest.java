package com.taskmanager.server;

import com.google.gson.Gson;
import com.taskmanager.controller.TaskController;
import com.taskmanager.model.Request;
import com.taskmanager.model.Response;

import java.io.*;
import java.net.Socket;

public class HandleRequest implements Runnable {
    private Socket socket;
    private TaskController taskController;
    private Gson gson;

    public HandleRequest(Socket socket) {
        this.socket = socket;
        this.taskController = new TaskController();
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String jsonRequest = reader.readLine();
            Request request = gson.fromJson(jsonRequest, Request.class);

            Response response = taskController.handleRequest(request);

            String jsonResponse = gson.toJson(response);
            writer.println(jsonResponse);

        } catch (IOException e) {
            System.out.println("HandleRequest exception: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}