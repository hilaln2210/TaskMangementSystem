package com.taskmanager.client.model;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class TaskModel {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void saveTasks(List<Task> tasks, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s%n",
                        task.getTitle(),
                        task.getDescription(),
                        task.getStartDateTime().format(DATE_TIME_FORMATTER),
                        task.getEndDateTime().format(DATE_TIME_FORMATTER),
                        task.getPriority(),
                        task.getStatus(),
                        String.join(";", task.getTags())));
            }
        }
    }

    public List<Task> loadTasks(String filePath) throws IOException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks; // Return empty list if file doesn't exist
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String title = parts[0];
                    String description = parts[1];
                    LocalDateTime startDateTime = LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER);
                    LocalDateTime endDateTime = LocalDateTime.parse(parts[3], DATE_TIME_FORMATTER);
                    String priority = parts[4];
                    String status = parts[5];
                    List<String> tags = Arrays.asList(parts[6].split(";"));
                    Task task = new Task(title, description, startDateTime, endDateTime, priority, status);
                    task.setTags(tags);
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    public void exportToICalendar(List<Task> tasks, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("BEGIN:VCALENDAR\n");
            writer.write("VERSION:2.0\n");
            writer.write("PRODID:-//TaskManager//EN\n");

            for (Task task : tasks) {
                writer.write("BEGIN:VEVENT\n");
                writer.write("SUMMARY:" + task.getTitle() + "\n");
                writer.write("DESCRIPTION:" + task.getDescription() + "\n");
                writer.write("DTSTART:" + task.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n");
                writer.write("DTEND:" + task.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n");
                writer.write("CATEGORIES:" + String.join(",", task.getTags()) + "\n");
                writer.write("END:VEVENT\n");
            }

            writer.write("END:VCALENDAR\n");
        }
    }
}