package com.taskmanager.service;

import com.taskmanager.dao.IDao;
import com.taskmanager.model.Task;

import java.util.List;

public class TaskService {
    private final IDao<Task> taskDao;

    public TaskService(IDao<Task> taskDao) {
        this.taskDao = taskDao;
    }

    public void addTask(Task task) {
        taskDao.save(task);
    }

    public void updateTask(Task task) {
        taskDao.update(task);
    }

    public void deleteTask(String id) {
        taskDao.delete(id);
    }

    public Task getTask(String id) {
        return taskDao.get(id);
    }

    public List<Task> getAllTasks() {
        return taskDao.getAll();
    }

    // If you need sorting or searching, implement it here without external algorithms
    public List<Task> sortTasks(List<Task> tasks) {
        // Implement simple sorting logic here
        return tasks.stream().sorted((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate())).toList();
    }

    public List<Task> searchTasks(String keyword) {
        // Implement simple search logic here
        return getAllTasks().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}