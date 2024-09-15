# To-Do List Application

## Abstract

This project implements a fully functioning To-Do List application using JavaFX for the graphical user interface (GUI). It integrates fundamental object-oriented programming (OOP) concepts, such as encapsulation, inheritance, and abstraction, along with exception handling and multithreading. The application allows users to add, remove, and mark tasks as completed, with a user-friendly interface. This project mimics a real world task-management system.

We have attempted to break down and give an outline for each function of the code we want to upload, should our abstract be accepted.

## Classes and Functions Overview

---

### 1. **TaskManagerException (Custom Exception)**
   - A custom exception class to handle errors related to task management.
   - Inherits from the `Exception` class.
   - Manages errors like exceeding the task limit or trying to modify tasks that don't exist.

### 2. **TaskManagerBase (Abstract Class)**
   - Acts as the base class for managing tasks and implements core functionality.
   - Stores tasks in an array(`Task[]`) and tracks the total number of tasks with an integer counter(`int`).
   - Contains abstract methods:
     - **`addTask(String)`**: Adds a task with a its name.
     - **`removeTask(int)`**: Removes a task by its index.
     - **`markTaskCompleted(int)`**: Marks a task as completed by its index.
   - Limits the number of tasks using a fixed integer (`final int`).

### 3. **Task (Class)**
   - Represents a single task with a name and completion status.
   - Attributes:
     - **`String taskName`**: Stores the name of the task.
     - **`boolean completed`**: Tracks whether the task is completed.
   - Methods:
     - **`markCompleted()`**: Marks the task as completed.
     - **`toString()`**: Returns a formatted `String` with the task name and status.

### 4. **TaskManager (Concrete Class)**
   - Extends `TaskManagerBase` and implements the task management methods:
     - **`addTask(String)`**: Adds a task to the array.
     - **`removeTask(int)`**: Removes a task by its index.
     - **`markTaskCompleted(int)`**: Marks a task as completed by its index.
   - Validates task limits and indices, throwing `TaskManagerException` where appropriate.

### 5. **ToDoListApp (Main Application Class)**
   - Extends the JavaFX `Application` class to create the graphical user interface (GUI) for managing tasks.
   - Key Components:
     - **`Label`**, **`TextField`**, **`Button`**: JavaFX UI elements for user interaction.
     - **`ListView<String>`**: Displays the list of tasks.
   - Users can:
     - Add tasks via a text input (`String`).
     - Remove tasks by selecting their index (`int`).
     - Mark tasks as completed.
   - A background thread (`TaskListRefresher`) periodically updates the task list.

### 6. **TaskListRefresher (Runnable Class)**
   - A background thread that automatically refreshes the task list every 2 seconds.
   - Uses `Platform.runLater()` to update the UI from the background without blocking the main thread.

### 7. **Exception Handling**
   - Manages errors like exceeding the maximum number of tasks or accessing invalid task indices.
   - Uses JavaFX `Alert` dialogs to show error messages (`String`) to the user.

### 8. **Multithreading**
   - Demonstrates multithreading with the `TaskListRefresher` running in a separate thread (`Thread`) to continuously update the task list without disrupting the main application flow.
---

## Team Members
- **Backend and Functionality**: Tanay Aggarwal [230911278]
- **JavaFX and Frontend**: Aditya Mohan Jha [230911428]
- **Testing & Debugging**: Ishaan Kukreti [230911282]

---

## Conclusion
---

This To-Do List application demonstrates key software development principles like object-oriented programming (OOP), exception handling, and multithreading. With its easy-to-use interface and features, it attempts to address task management in the practical world.

We encourage you to explore the application as a reflection of our understanding and implementation of JavaFX and essential programming practices.

