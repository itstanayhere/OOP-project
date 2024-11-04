import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Custom exception class demonstrating OOP principle of encapsulation
class TaskManagerException extends Exception {
    public TaskManagerException(String message) {
        super(message);
    }
}

// Abstract base class demonstrating OOP principle of abstraction
abstract class TaskManagerBase {
    Task[] tasks; // Array to hold tasks
    int taskCount; // Current count of tasks
    final int maxTasks = 100; // Maximum number of tasks

    public TaskManagerBase() {
        tasks = new Task[maxTasks]; // Initialize the tasks array
        taskCount = 0; // Initialize task count
    }

    // Abstract methods defining expected behavior of task managers (polymorphism)
    public abstract void addTask(String taskName, int priority) throws TaskManagerException;
    public abstract void removeTask(int index) throws TaskManagerException;
    public abstract void markTaskCompleted(int index) throws TaskManagerException;

    public Task[] getTasks() {
        return tasks; // Getter for tasks
    }

    public int getTaskCount() {
        return taskCount; // Getter for task count
    }
}

// Task class demonstrating OOP principles of encapsulation and composition
class Task {
    String taskName; // Name of the task
    int priority; // Priority of the task
    boolean completed; // Completion status of the task

    public Task(String taskName, int priority) {
        this.taskName = taskName; // Initialize task name
        this.priority = priority; // Initialize task priority
        this.completed = false; // Initialize task as not completed
    }

    // Getters for task properties
    public String getTaskName() {
        return taskName;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true; // Mark task as completed
    }

    @Override
    public String toString() {
        return taskName + " (Priority: " + priority + ")" + (completed ? " (Completed)" : "");
    }
}

// Concrete class implementing the abstract TaskManagerBase (OOP principle of inheritance)
class TaskManager extends TaskManagerBase {
    @Override
    public void addTask(String taskName, int priority) throws TaskManagerException {
        if (taskCount >= maxTasks) {
            throw new TaskManagerException("Task limit reached."); // Error handling
        }

        // Insert the new task at the correct position based on priority
        Task newTask = new Task(taskName, priority);
        int i = taskCount - 1;

        // Shift tasks with strictly higher priority, maintaining order for same priority
        while (i >= 0 && tasks[i].getPriority() > priority) {
            tasks[i + 1] = tasks[i];
            i--;
        }
        tasks[i + 1] = newTask; // Add the new task
        taskCount++; // Increment task count
    }

    @Override
    public void removeTask(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) {
            throw new TaskManagerException("Invalid task index."); // Error handling
        }
        // Shift tasks to remove the selected task
        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        taskCount--; // Decrement task count
    }

    @Override
    public void markTaskCompleted(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) {
            throw new TaskManagerException("Invalid task index."); // Error handling
        }
        tasks[index].markCompleted(); // Mark the specified task as completed
    }
}

// Main class for the JavaFX application
public class Main extends Application {

    TaskManager taskManager; // TaskManager instance

    @Override
    public void start(Stage primaryStage) {
        taskManager = new TaskManager(); // Initialize TaskManager

        // UI components
        Label titleLabel = new Label("To-Do List");
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter a new task");

        // Priority input field
        TextField priorityInputField = new TextField();
        priorityInputField.setPromptText("Enter priority (e.g., 1, 2, 3)");

        Button addTaskButton = new Button("Add Task");

        ListView<String> taskListView = new ListView<>();
        taskListView.setPrefHeight(200);

        // Adding task action with error handling
        addTaskButton.setOnAction(e -> {
            String taskName = taskInputField.getText();
            String priorityText = priorityInputField.getText();

            if (taskName.isEmpty()) {
                showAlert("Please enter a task name.");
            } else if (priorityText.isEmpty()) {
                showAlert("Please enter a priority.");
            } else {
                try {
                    int priority = Integer.parseInt(priorityText);
                    taskManager.addTask(taskName, priority); // OOP usage
                    updateTaskList(taskListView); // Update UI
                    taskInputField.clear();
                    priorityInputField.clear();
                } catch (NumberFormatException ex) {
                    showAlert("Priority must be a number.");
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        // Remove task action with error handling
        Button removeTaskButton = new Button("Remove Task");
        removeTaskButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                showAlert("Please select a task to remove.");
            } else {
                try {
                    taskManager.removeTask(selectedIndex);
                    updateTaskList(taskListView); // Update UI
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        // Complete task action with error handling
        Button completeTaskButton = new Button("Mark as Completed");
        completeTaskButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                showAlert("Please select a task to mark as completed.");
            } else {
                try {
                    taskManager.markTaskCompleted(selectedIndex);
                    updateTaskList(taskListView); // Update UI
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        // Layout setup
        VBox layout = new VBox(10, titleLabel, taskInputField, priorityInputField, addTaskButton, taskListView, completeTaskButton, removeTaskButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("To-Do List");
        primaryStage.show();

        // Starting a new thread to refresh the task list periodically (demonstrating multithreading)
        new Thread(new TaskListRefresher(taskListView)).start();
    }

    // Inner class demonstrating multithreading for UI update
    class TaskListRefresher implements Runnable {
        ListView<String> taskListView;

        public TaskListRefresher(ListView<String> taskListView) {
            this.taskListView = taskListView;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000); // Sleep for 5 seconds
                    javafx.application.Platform.runLater(() -> updateTaskList(taskListView)); // Update UI on the JavaFX Application Thread
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    // Method to update the task list in the UI
    void updateTaskList(ListView<String> taskListView) {
        taskListView.getItems().clear();
        for (int i = 0; i < taskManager.getTaskCount(); i++) {
            taskListView.getItems().add(taskManager.getTasks()[i].toString());
        }
    }

    // Alert for information messages
    void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    // Alert for error messages
    void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
