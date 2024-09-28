package com.taskmanager.controller;

import com.taskmanager.model.Request;
import com.taskmanager.model.Response;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import com.taskmanager.dao.MyDMFileImpl;

public class TaskController {
    private TaskService taskService;

    public TaskController() {
        MyDMFileImpl dao = new MyDMFileImpl("tasks.txt");
        this.taskService = new TaskService(dao);
    }

    public Response handleRequest(Request request) {
        switch (request.getHeaders().get("action")) {
            case "saveTask":
                Task task = (Task) request.getBody();
                taskService.addTask(task);
                return new Response("Task saved successfully", null);
            case "getTask":
                String taskId = (String) request.getBody();
                Task retrievedTask = taskService.getTask(taskId);
                return new Response("Task retrieved", retrievedTask);
            // Add more cases for other actions
            default:
                return new Response("Unknown action", null);
        }
    }
}