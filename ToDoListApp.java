package OOP_Project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Exception class for handling task management errors
class TaskManagerException extends Exception {
    public TaskManagerException(String message) {
        super(message);
    }
}

// Abstract class defining general task management operations
abstract class TaskManagerBase {
    protected Task[] tasks;  // Array to store tasks
    protected int taskCount; // Keeps track of the number of tasks
    protected final int maxTasks = 100; // Maximum limit of tasks

    public TaskManagerBase() {
        tasks = new Task[maxTasks];  // Initialize the task array
        taskCount = 0;               // No tasks at the beginning
    }

    // Abstract methods to be implemented by concrete class
    public abstract void addTask(String taskName) throws TaskManagerException;
    public abstract void removeTask(int index) throws TaskManagerException;
    public abstract void markTaskCompleted(int index) throws TaskManagerException;

    // Returns the task array
    public Task[] getTasks() {
        return tasks;
    }

    // Returns the number of tasks
    public int getTaskCount() {
        return taskCount;
    }
}

// Task class representing a to-do task (Encapsulation demonstrated)
class Task {
    private String taskName;   // Name of the task
    private boolean completed; // Completion status

    public Task(String taskName) {
        this.taskName = taskName;
        this.completed = false;  // Task is not completed initially
    }

    // Getter for task name
    public String getTaskName() {
        return taskName;
    }

    // Getter for task completion status
    public boolean isCompleted() {
        return completed;
    }

    // Mark the task as completed
    public void markCompleted() {
        this.completed = true;
    }

    // Override toString to display task status in the list
    @Override
    public String toString() {
        return taskName + (completed ? " (Completed)" : "");
    }
}

// TaskManager class that extends the abstract TaskManagerBase class (Inheritance demonstrated)
class TaskManager extends TaskManagerBase {

    // Adds a task to the task list
    @Override
    public void addTask(String taskName) throws TaskManagerException {
        if (taskCount >= maxTasks) {
            throw new TaskManagerException("Task limit reached.");  // Exception handling
        }
        tasks[taskCount++] = new Task(taskName); // Add task and increment task count
    }

    // Removes a task from the list by index
    @Override
    public void removeTask(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) {
            throw new TaskManagerException("Invalid task index.");  // Exception handling
        }
        // Shift tasks to fill the gap left by the removed task
        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        taskCount--; // Decrement task count
    }

    // Marks a task as completed
    @Override
    public void markTaskCompleted(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) {
            throw new TaskManagerException("Invalid task index.");  // Exception handling
        }
        tasks[index].markCompleted(); // Mark the task as completed
    }
}

// Main application class integrating JavaFX for UI and using the task manager
public class ToDoListApp extends Application {

    private TaskManager taskManager; // TaskManager instance to handle tasks

    @Override
    public void start(Stage primaryStage) {
        taskManager = new TaskManager(); // Initialize TaskManager

        // JavaFX UI components
        Label titleLabel = new Label("To-Do List"); // Title label
        TextField taskInputField = new TextField(); // Input field for new tasks
        taskInputField.setPromptText("Enter a new task"); // Placeholder text
        Button addTaskButton = new Button("Add Task"); // Button to add task

        // ListView to display tasks
        ListView<String> taskListView = new ListView<>();
        taskListView.setPrefHeight(200); // Set height for the task list

        // Event handler for adding a task
        addTaskButton.setOnAction(e -> {
            String taskName = taskInputField.getText(); // Get input from text field
            if (!taskName.isEmpty()) {
                try {
                    taskManager.addTask(taskName); // Add task to TaskManager
                    updateTaskList(taskListView);  // Update the displayed task list
                    taskInputField.clear();        // Clear the input field
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());    // Show error if task cannot be added
                }
            }
        });

        // Button to remove selected task
        Button removeTaskButton = new Button("Remove Task");
        removeTaskButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex(); // Get selected task
            if (selectedIndex != -1) {
                try {
                    taskManager.removeTask(selectedIndex); // Remove the selected task
                    updateTaskList(taskListView);          // Update task list
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());            // Show error if task cannot be removed
                }
            }
        });

        // Button to mark a task as completed
        Button completeTaskButton = new Button("Mark as Completed");
        completeTaskButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex(); // Get selected task
            if (selectedIndex != -1) {
                try {
                    taskManager.markTaskCompleted(selectedIndex); // Mark task as completed
                    updateTaskList(taskListView);                 // Update task list
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());                   // Show error if task cannot be marked
                }
            }
        });

        // Layout setup for the UI
        VBox layout = new VBox(10, titleLabel, taskInputField, addTaskButton, taskListView, completeTaskButton, removeTaskButton);
        layout.setPadding(new Insets(20)); // Set padding

        // Set up the scene and display the window
        Scene scene = new Scene(layout, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("To-Do List");
        primaryStage.show();

        // Start the background thread to auto-refresh the task list every 2 seconds (Multithreading)
        new Thread(new TaskListRefresher(taskListView)).start();
    }

    // Runnable class for auto-refreshing the task list in a separate thread
    class TaskListRefresher implements Runnable {
        private ListView<String> taskListView;

        public TaskListRefresher(ListView<String> taskListView) {
            this.taskListView = taskListView; // Reference to the task list UI element
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000);  // Sleep for 2 seconds before refreshing
                    // Use Platform.runLater to update the UI from a background thread
                    javafx.application.Platform.runLater(() -> updateTaskList(taskListView));
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    // Method to update the task list in the UI
    private void updateTaskList(ListView<String> taskListView) {
        taskListView.getItems().clear();  // Clear the current list
        // Loop through tasks and add each task to the ListView
        for (int i = 0; i < taskManager.getTaskCount(); i++) {
            taskListView.getItems().add(taskManager.getTasks()[i].toString());
        }
    }

    // Display an error message in case of exceptions
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Alert dialog for errors
        alert.setContentText(message);                  // Set error message
        alert.show();                                   // Show the alert
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);  // Start the JavaFX application
    }
}
