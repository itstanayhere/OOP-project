import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class TaskManagerException extends Exception {
    public TaskManagerException(String message) {
        super(message);
    }
}

abstract class TaskManagerBase {
    protected Task[] tasks;
    protected int taskCount;
    protected final int maxTasks = 100;

    public TaskManagerBase() {
        tasks = new Task[maxTasks];
        taskCount = 0;
    }

    public abstract void addTask(String taskName, int priority) throws TaskManagerException;
    public abstract void removeTask(int index) throws TaskManagerException;
    public abstract void markTaskCompleted(int index) throws TaskManagerException;

    public Task[] getTasks() {
        return tasks;
    }

    public int getTaskCount() {
        return taskCount;
    }
}

class Task {
    private String taskName;
    private int priority;
    private boolean completed;

    public Task(String taskName, int priority) {
        this.taskName = taskName;
        this.priority = priority;
        this.completed = false;
    }

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
        this.completed = true;
    }

    @Override
    public String toString() {
        return taskName + " (Priority: " + priority + ")" + (completed ? " (Completed)" : "");
    }
}

class TaskManager extends TaskManagerBase {
    @Override
public void addTask(String taskName, int priority) throws TaskManagerException {
    if (taskCount >= maxTasks) {
        throw new TaskManagerException("Task limit reached.");
    }

    // Insert the new task at the correct position based on priority
    Task newTask = new Task(taskName, priority);
    int i = taskCount - 1;

    // Shift tasks with strictly higher priority, maintaining order for same priority
    while (i >= 0 && tasks[i].getPriority() > priority) {
        tasks[i + 1] = tasks[i];
        i--;
    }
    tasks[i + 1] = newTask;
    taskCount++;
}

    @Override
    public void removeTask(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) {
            throw new TaskManagerException("Invalid task index.");
        }
        for (int i = index; i < taskCount - 1; i++) {
            tasks[i] = tasks[i + 1];
        }
        taskCount--;
    }

    @Override
    public void markTaskCompleted(int index) throws TaskManagerException {
        if (index < 0 || index >= taskCount) {
            throw new TaskManagerException("Invalid task index.");
        }
        tasks[index].markCompleted();
    }
}

public class Main extends Application {

    private TaskManager taskManager;

    @Override
    public void start(Stage primaryStage) {
        taskManager = new TaskManager();

        Label titleLabel = new Label("To-Do List");
        TextField taskInputField = new TextField();
        taskInputField.setPromptText("Enter a new task");

        // Priority input field
        TextField priorityInputField = new TextField();
        priorityInputField.setPromptText("Enter priority (e.g., 1, 2, 3)");

        Button addTaskButton = new Button("Add Task");

        ListView<String> taskListView = new ListView<>();
        taskListView.setPrefHeight(200);

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
                    taskManager.addTask(taskName, priority);
                    updateTaskList(taskListView);
                    taskInputField.clear();
                    priorityInputField.clear();
                } catch (NumberFormatException ex) {
                    showAlert("Priority must be a number.");
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        Button removeTaskButton = new Button("Remove Task");
        removeTaskButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                showAlert("Please select a task to remove.");
            } else {
                try {
                    taskManager.removeTask(selectedIndex);
                    updateTaskList(taskListView);
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        Button completeTaskButton = new Button("Mark as Completed");
        completeTaskButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                showAlert("Please select a task to mark as completed.");
            } else {
                try {
                    taskManager.markTaskCompleted(selectedIndex);
                    updateTaskList(taskListView);
                } catch (TaskManagerException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        VBox layout = new VBox(10, titleLabel, taskInputField, priorityInputField, addTaskButton, taskListView, completeTaskButton, removeTaskButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("To-Do List");
        primaryStage.show();

        new Thread(new TaskListRefresher(taskListView)).start();
    }

    class TaskListRefresher implements Runnable {
        private ListView<String> taskListView;

        public TaskListRefresher(ListView<String> taskListView) {
            this.taskListView = taskListView;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                    javafx.application.Platform.runLater(() -> updateTaskList(taskListView));
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private void updateTaskList(ListView<String> taskListView) {
        taskListView.getItems().clear();
        for (int i = 0; i < taskManager.getTaskCount(); i++) {
            taskListView.getItems().add(taskManager.getTasks()[i].toString());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
