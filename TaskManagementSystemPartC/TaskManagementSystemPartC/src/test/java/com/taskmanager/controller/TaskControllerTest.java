package com.taskmanager.controller;

import com.taskmanager.model.Request;
import com.taskmanager.model.Response;
import com.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskController = new TaskController();
    }

    @Test
    void testSaveTask() {
        Map<String, String> headers = new HashMap<>();
        headers.put("action", "saveTask");
        Task task = new Task("1", "Test Task", "Description", java.time.LocalDate.now(), Task.Priority.MEDIUM, Task.Status.TODO);
        Request request = new Request(headers, task);

        Response response = taskController.handleRequest(request);

        assertEquals("Task saved successfully", response.getMessage());
    }

    @Test
    void testGetTask() {
        // First, save a task
        Map<String, String> saveHeaders = new HashMap<>();
        saveHeaders.put("action", "saveTask");
        Task task = new Task("1", "Test Task", "Description", java.time.LocalDate.now(), Task.Priority.MEDIUM, Task.Status.TODO);
        Request saveRequest = new Request(saveHeaders, task);
        taskController.handleRequest(saveRequest);

        // Now, try to get the task
        Map<String, String> getHeaders = new HashMap<>();
        getHeaders.put("action", "getTask");
        Request getRequest = new Request(getHeaders, "1");

        Response response = taskController.handleRequest(getRequest);

        assertEquals("Task retrieved", response.getMessage());
        assertNotNull(response.getData());
        assertTrue(response.getData() instanceof Task);
        assertEquals("Test Task", ((Task) response.getData()).getTitle());
    }
}