package com.taskmanager.dao;

import com.taskmanager.model.Task;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyDMFileImpl implements IDao<Task> {
    private final String filePath;

    public MyDMFileImpl(String filePath) {
        this.filePath = filePath;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Task task) {
        List<Task> tasks = getAll();
        tasks.add(task);
        writeToFile(tasks);
    }

    @Override
    public void update(Task task) {
        List<Task> tasks = getAll();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(task.getId())) {
                tasks.set(i, task);
                break;
            }
        }
        writeToFile(tasks);
    }

    @Override
    public void delete(String id) {
        List<Task> tasks = getAll();
        tasks.removeIf(task -> task.getId().equals(id));
        writeToFile(tasks);
    }

    @Override
    public Task get(String id) {
        List<Task> tasks = getAll();
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Task task = new Task(
                            parts[0],
                            parts[1],
                            parts[2],
                            LocalDate.parse(parts[3]),
                            Task.Priority.valueOf(parts[4]),
                            Task.Status.valueOf(parts[5])
                    );
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void writeToFile(List<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.getPriority(),
                        task.getStatus()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}