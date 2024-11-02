import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Exception class to handle task-related errors
class TaskManagerException extends Exception {
    TaskManagerException(String message) {
        super(message);
    }
}

// Base class for task management
abstract class TaskManagerBase {
    Task[] tasks = new Task[100]; // Array to store tasks, encapsulating task data
    int taskCount = 0; // Initial task count

    // Abstract methods for task operations (abstraction and polymorphism)
    abstract void addTask(String taskName, int priority) throws TaskManagerException;
    abstract void removeTask(int index) throws TaskManagerException;
    abstract void markTaskCompleted(int index) throws TaskManagerException;

    // Returns the current task list (encapsulation)
    Task[] getTasks() { return tasks; }
}

// Task class represents a single to-do item, with encapsulated properties and methods
class Task implements Comparable<Task> {
    private String taskName;
    private boolean completed = false;
    private int priority;

    Task(String taskName, int priority) {
        this.taskName = taskName;
        this.priority = priority;
    }

    String getTaskName() { return taskName; }
    boolean isCompleted() { return completed; }
    int getPriority() { return priority; }

    // Encapsulated method to mark the task as completed
    void markCompleted() { completed = true; }

    // Compare tasks based on priority, for ordering tasks
    @Override
    public int compareTo(Task other) {
        return Integer.compare(priority, other.priority);
    }

    @Override
    public String toString() {
        return taskName + (completed ? " (Completed)" : "");
    }
}

// Main TaskManager class that extends the base class and defines task management behavior
class TaskManager extends TaskManagerBase {
    @Override
    public void addTask(String taskName, int priority) throws TaskManagerException {
        if (taskCount >= tasks.length) throw new TaskManagerException("Task limit reached.");
        tasks[taskCount++] = new Task(taskName, priority);
        sortTasks();
    }

    @Override
    public void removeTask(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) throw new TaskManagerException("Invalid task index.");
        System.arraycopy(tasks, index + 1, tasks, index, taskCount - index - 1);
        taskCount--;
        sortTasks();
    }

    @Override
    public void markTaskCompleted(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) throw new TaskManagerException("Invalid task index.");
        tasks[index].markCompleted();
        sortTasks();
    }

    private void sortTasks() {
        java.util.Arrays.sort(tasks, 0, taskCount);
    }
}

// Main application class, setting up the UI and handling actions
public class Main extends Application {
    private TaskManager taskManager = new TaskManager();

    @Override
    public void start(Stage primaryStage) {
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter task name");

        TextField priorityField = new TextField();
        priorityField.setPromptText("Priority (1-5)");

        ListView<String> taskListView = new ListView<>();

        // Add task button
        Button addTaskButton = new Button("Add Task");
        addTaskButton.setOnAction(e -> {
            try {
                int priority = Integer.parseInt(priorityField.getText());
                if (priority < 1 || priority > 5) throw new NumberFormatException();
                taskManager.addTask(taskInputField.getText(), priority);
                updateTaskList(taskListView);
                taskInputField.clear();
                priorityField.clear();
            } catch (TaskManagerException | NumberFormatException ex) {
                showAlert("Error", "Invalid input or priority.");
            }
        });

        // Remove task button
        Button removeTaskButton = new Button("Remove Task");
        removeTaskButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            try {
                taskManager.removeTask(selectedIndex);
                updateTaskList(taskListView);
            } catch (TaskManagerException ex) {
                showAlert("Error", "Invalid task selection.");
            }
        });

        VBox layout = new VBox(10, taskInputField, priorityField, addTaskButton, taskListView, removeTaskButton);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 300, 400));
        primaryStage.setTitle("To-Do List");
        primaryStage.show();
    }

    // Updates task list view with task names and statuses
    private void updateTaskList(ListView<String> taskListView) {
        taskListView.getItems().clear();
        for (int i = 0; i < taskManager.taskCount; i++) {
            taskListView.getItems().add(taskManager.tasks[i].toString());
        }
    }

    // Shows an error alert with a message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(title);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
