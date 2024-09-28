package com.taskmanager.client.controller;

import com.taskmanager.client.model.Task;
import com.taskmanager.client.model.TaskModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import com.calendarfx.view.CalendarView;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TaskController {

    @FXML private TableView<Task> taskTableView;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descriptionColumn;
    @FXML private TableColumn<Task, LocalDateTime> startDateTimeColumn;
    @FXML private TableColumn<Task, LocalDateTime> endDateTimeColumn;
    @FXML private TableColumn<Task, String> priorityColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private TextField searchField;
    @FXML private TextField taskTextField;
    @FXML private TextField descriptionTextField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private ComboBox<String> priorityComboBox;
    @FXML private ProgressBar overallProgress;
    @FXML private PieChart statusPieChart;
    @FXML private VBox remindersBox;
    @FXML private BorderPane calendarPane;

    private TaskModel model;
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private static final String DEFAULT_TASKS_FILE = "tasks.txt";
    private CalendarView calendarView;
    private Calendar taskCalendar;

    public void initialize() {
        model = new TaskModel();
        initializeTableColumns();
        initializePriorityComboBox();
        initializeCalendarView();

        try {
            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + File.separator + DEFAULT_TASKS_FILE;
            System.out.println("Loading tasks from: " + fullPath);
            tasks.setAll(model.loadTasks(fullPath));
        } catch (IOException e) {
            System.err.println("Failed to load tasks: " + e.getMessage());
        }

        taskTableView.setItems(tasks);
        updateOverallProgress();
        updateStatusPieChart();
        checkReminders();
        updateCalendarView();
    }

    private void initializeTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        startDateTimeColumn.setCellFactory(column -> new TableCell<Task, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });
        endDateTimeColumn.setCellFactory(column -> new TableCell<Task, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });
    }

    private void initializePriorityComboBox() {
        priorityComboBox.getItems().addAll("Low", "Medium", "High");
    }

    private void initializeCalendarView() {
        calendarView = new CalendarView();
        taskCalendar = new Calendar("Tasks");
        taskCalendar.setStyle(Calendar.Style.STYLE1);

        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(taskCalendar);

        calendarView.getCalendarSources().addAll(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        calendarPane.setCenter(calendarView);
    }

    private void updateCalendarView() {
        taskCalendar.clear();
        for (Task task : tasks) {
            Entry<String> entry = new Entry<>(task.getTitle());
            entry.setInterval(task.getStartDateTime(), task.getEndDateTime());
            taskCalendar.addEntry(entry);
        }
    }

    @FXML
    private void searchTasks() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<Task> filteredTasks = tasks.filtered(task ->
                task.getTitle().toLowerCase().contains(searchTerm) ||
                        task.getDescription().toLowerCase().contains(searchTerm)
        );
        taskTableView.setItems(filteredTasks);
    }

    @FXML
    private void addTask() {
        String title = taskTextField.getText();
        String description = descriptionTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTime = startTimeField.getText();
        String endTime = endTimeField.getText();
        String priority = priorityComboBox.getValue();

        if (title.isEmpty() || startDate == null || endDate == null || startTime.isEmpty() || endTime.isEmpty() || priority == null) {
            showAlert("Error", "Please fill all required fields.");
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTime));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime));

        if (endDateTime.isBefore(startDateTime)) {
            showAlert("Error", "End date/time must be after start date/time.");
            return;
        }

        Task newTask = new Task(title, description, startDateTime, endDateTime, priority, "Not Started");
        tasks.add(newTask);
        clearInputFields();
        updateOverallProgress();
        updateStatusPieChart();
        updateCalendarView();
        saveTasksAutomatically();
    }

    @FXML
    private void editSelectedTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("Error", "Please select a task to edit.");
            return;
        }

        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Edit Task");
        dialog.setHeaderText("Edit the task details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField title = new TextField(selectedTask.getTitle());
        TextArea description = new TextArea(selectedTask.getDescription());
        DatePicker startDate = new DatePicker(selectedTask.getStartDateTime().toLocalDate());
        DatePicker endDate = new DatePicker(selectedTask.getEndDateTime().toLocalDate());
        TextField startTime = new TextField(selectedTask.getStartDateTime().toLocalTime().toString());
        TextField endTime = new TextField(selectedTask.getEndDateTime().toLocalTime().toString());
        ComboBox<String> priority = new ComboBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        priority.setValue(selectedTask.getPriority());
        ComboBox<String> status = new ComboBox<>(FXCollections.observableArrayList("Not Started", "In Progress", "Completed"));
        status.setValue(selectedTask.getStatus());

        grid.add(new Label("Title:"), 0, 0);
        grid.add(title, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(description, 1, 1);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDate, 1, 2);
        grid.add(new Label("Start Time:"), 0, 3);
        grid.add(startTime, 1, 3);
        grid.add(new Label("End Date:"), 0, 4);
        grid.add(endDate, 1, 4);
        grid.add(new Label("End Time:"), 0, 5);
        grid.add(endTime, 1, 5);
        grid.add(new Label("Priority:"), 0, 6);
        grid.add(priority, 1, 6);
        grid.add(new Label("Status:"), 0, 7);
        grid.add(status, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                LocalDateTime startDateTime = LocalDateTime.of(startDate.getValue(), LocalTime.parse(startTime.getText()));
                LocalDateTime endDateTime = LocalDateTime.of(endDate.getValue(), LocalTime.parse(endTime.getText()));

                if (endDateTime.isBefore(startDateTime)) {
                    showAlert("Error", "End date/time must be after start date/time.");
                    return null;
                }

                selectedTask.setTitle(title.getText());
                selectedTask.setDescription(description.getText());
                selectedTask.setStartDateTime(startDateTime);
                selectedTask.setEndDateTime(endDateTime);
                selectedTask.setPriority(priority.getValue());
                selectedTask.setStatus(status.getValue());
                return selectedTask;
            }
            return null;
        });

        Optional<Task> result = dialog.showAndWait();
        result.ifPresent(task -> {
            taskTableView.refresh();
            updateOverallProgress();
            updateStatusPieChart();
            updateCalendarView();
            saveTasksAutomatically();
        });
    }

    @FXML
    private void deleteSelectedTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("Error", "Please select a task to delete.");
            return;
        }
        tasks.remove(selectedTask);
        updateOverallProgress();
        updateStatusPieChart();
        updateCalendarView();
        saveTasksAutomatically();
    }

    @FXML
    private void markTaskAsComplete() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showAlert("Error", "Please select a task to mark as complete.");
            return;
        }
        selectedTask.setStatus("Completed");
        taskTableView.refresh();
        updateOverallProgress();
        updateStatusPieChart();
        updateCalendarView();
        saveTasksAutomatically();
    }

    @FXML
    private void saveTaskList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Task List");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        fileChooser.setInitialFileName(DEFAULT_TASKS_FILE);

        String currentDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(currentDir));

        File file = fileChooser.showSaveDialog(taskTableView.getScene().getWindow());
        if (file != null) {
            try {
                model.saveTasks(tasks, file.getAbsolutePath());
                showAlert("Success", "Tasks saved successfully.");
            } catch (IOException e) {
                showAlert("Error", "Failed to save tasks: " + e.getMessage());
            }
        }
    }

    @FXML
    private void loadTaskList() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Task List");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));

        String currentDir = System.getProperty("user.dir");
        fileChooser.setInitialDirectory(new File(currentDir));

        File file = fileChooser.showOpenDialog(taskTableView.getScene().getWindow());
        if (file != null) {
            try {
                tasks.setAll(model.loadTasks(file.getAbsolutePath()));
                updateOverallProgress();
                updateStatusPieChart();
                updateCalendarView();
                showAlert("Success", "Tasks loaded successfully.");
            } catch (IOException e) {
                showAlert("Error", "Failed to load tasks: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exportToCalendar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to Calendar (iCal)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("iCal files (*.ics)", "*.ics"));
        File file = fileChooser.showSaveDialog(taskTableView.getScene().getWindow());
        if (file != null) {
            try {
                model.exportToICalendar(tasks, file.getAbsolutePath());
                showAlert("Success", "Tasks exported to calendar successfully.");
            } catch (IOException e) {
                showAlert("Error", "Failed to export tasks: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exitApplication() {
        System.exit(0);
    }

    @FXML
    private void toggleCompletedTasks() {
        ObservableList<Task> filteredTasks = tasks.filtered(task -> !task.getStatus().equals("Completed"));
        taskTableView.setItems(filteredTasks);
    }



    private void updateOverallProgress() {
        long completedTasks = tasks.stream().filter(task -> task.getStatus().equals("Completed")).count();
        overallProgress.setProgress(tasks.isEmpty() ? 0 : (double) completedTasks / tasks.size());
    }

    private void updateStatusPieChart() {
        if (statusPieChart == null) {
            System.err.println("Status PieChart is not initialized.");
            return;
        }

        Map<String, Long> statusCounts = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                statusCounts.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        );

        statusPieChart.setData(pieChartData);
    }

    private void checkReminders() {
        remindersBox.getChildren().clear();
        LocalDate today = LocalDate.now();
        tasks.stream()
                .filter(task -> !task.getStatus().equals("Completed"))
                .filter(task -> ChronoUnit.DAYS.between(today, task.getStartDateTime().toLocalDate()) <= 3)
                .forEach(task -> {
                    Label reminder = new Label("Reminder: " + task.getTitle() + " starts on " + task.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    remindersBox.getChildren().add(reminder);
                });
    }

    private void clearInputFields() {
        taskTextField.clear();
        descriptionTextField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        startTimeField.clear();
        endTimeField.clear();
        priorityComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveTasksAutomatically() {
        try {
            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + File.separator + DEFAULT_TASKS_FILE;
            System.out.println("Saving tasks to: " + fullPath); // Debug log
            model.saveTasks(tasks, fullPath);
        } catch (IOException e) {
            showAlert("Error", "Failed to save tasks: " + e.getMessage());
        }
    }
}