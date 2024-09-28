package com.taskmanager.service;

import com.taskmanager.dao.IDao;
import com.taskmanager.dao.MyDMFileImpl;
import com.taskmanager.model.Task;
import algorithm.IAlgoTaskManager;
import algorithm.SortingAlgorithm;
import algorithm.SearchAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestTaskService {
    private TaskService taskService;
    private IDao<Task> taskDao;
    private IAlgoTaskManager<Task> sortAlgorithm;
    private IAlgoTaskManager<Task> searchAlgorithm;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        String filePath = tempDir.resolve("tasks.txt").toString();
        taskDao = new MyDMFileImpl(filePath);
        sortAlgorithm = new SortingAlgorithm<>();
        searchAlgorithm = new SearchAlgorithm<>();
        taskService = new TaskService(taskDao, sortAlgorithm, searchAlgorithm);
    }

    @Test
    void testAddAndGetTask() {
        Task task = new Task("1", "Test Task", "Description", LocalDate.now(), Task.Priority.MEDIUM, Task.Status.TODO);
        taskService.addTask(task);

        Task retrievedTask = taskService.getTask("1");
        assertNotNull(retrievedTask);
        assertEquals("Test Task", retrievedTask.getTitle());
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("1", "Test Task", "Description", LocalDate.now(), Task.Priority.MEDIUM, Task.Status.TODO);
        taskService.addTask(task);

        task.setTitle("Updated Task");
        taskService.updateTask(task);

        Task updatedTask = taskService.getTask("1");
        assertEquals("Updated Task", updatedTask.getTitle());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task("1", "Test Task", "Description", LocalDate.now(), Task.Priority.MEDIUM, Task.Status.TODO);
        taskService.addTask(task);

        taskService.deleteTask("1");

        assertNull(taskService.getTask("1"));
    }

    @Test
    void testSortTasks() {
        Task task1 = new Task("1", "Task 1", "Description", LocalDate.now().plusDays(2), Task.Priority.MEDIUM, Task.Status.TODO);
        Task task2 = new Task("2", "Task 2", "Description", LocalDate.now(), Task.Priority.HIGH, Task.Status.TODO);
        Task task3 = new Task("3", "Task 3", "Description", LocalDate.now().plusDays(1), Task.Priority.LOW, Task.Status.TODO);

        taskService.addTask(task1);
        taskService.addTask(task2);
        taskService.addTask(task3);

        List<Task> sortedTasks = taskService.sortTasks(taskService.getAllTasks());
        assertEquals("2", sortedTasks.get(0).getId());
        assertEquals("3", sortedTasks.get(1).getId());
        assertEquals("1", sortedTasks.get(2).getId());
    }

    @Test
    void testSearchTasks() {
        Task task1 = new Task("1", "Urgent Task", "Important", LocalDate.now(), Task.Priority.HIGH, Task.Status.TODO);
        Task task2 = new Task("2", "Regular Task", "Not urgent", LocalDate.now(), Task.Priority.MEDIUM, Task.Status.TODO);

        taskService.addTask(task1);
        taskService.addTask(task2);

        List<Task> searchResults = taskService.searchTasks("Urgent");
        assertEquals(1, searchResults.size());
        assertEquals("1", searchResults.get(0).getId());
    }
}