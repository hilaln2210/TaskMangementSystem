package com.taskmanager.service;

import com.taskmanager.dao.IDao;
import com.taskmanager.model.Task;
import algorithm.IAlgoTaskManager;

import java.util.List;

public class TaskService {
    private final IDao<Task> taskDao;
    private final IAlgoTaskManager<Task> sortAlgorithm;
    private final IAlgoTaskManager<Task> searchAlgorithm;

    public TaskService(IDao<Task> taskDao, IAlgoTaskManager<Task> sortAlgorithm, IAlgoTaskManager<Task> searchAlgorithm) {
        this.taskDao = taskDao;
        this.sortAlgorithm = sortAlgorithm;
        this.searchAlgorithm = searchAlgorithm;
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

    public List<Task> sortTasks(List<Task> tasks) {
        return sortAlgorithm.sort(tasks, (t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));
    }

    public List<Task> searchTasks(String keyword) {
        List<Task> allTasks = taskDao.getAll();
        return searchAlgorithm.search(allTasks, task ->
                task.getTitle().toLowerCase().contains(keyword.toLowerCase()));
    }
}