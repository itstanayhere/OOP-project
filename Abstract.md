# To-Do List Application

## Abstract

This project implements a fully functioning To-Do List application using JavaFX for the graphical user interface (GUI). It integrates fundamental object-oriented programming (OOP) concepts, such as encapsulation, inheritance, and abstraction, along with exception handling and multithreading. The application allows users to add, remove, and mark tasks as completed, with a user-friendly interface. This project mimics a real world task-management system.

We have attempted to break down and give an outline for each function of the code we want to upload, should our abstract be accepted.

## Classes and Functions Overview

### 1. **TaskManagerException (Custom Exception)**
   - A custom exception class designed to handle errors related to task management.
   - Inherits from the `Exception` class.
   - Provides meaningful error messages when the application encounters task-related issues, such as adding a task beyond the maximum limit or accessing invalid task indices.

### 2. **TaskManagerBase (Abstract Class)**
   - Serves as the base class for managing tasks, implementing the core task management functionality.
   - Contains an array of tasks (`Task[] tasks`), and tracks the number of tasks (`taskCount`).
   - Defines abstract methods `addTask()`, `removeTask()`, and `markTaskCompleted()` that concrete subclasses must implement.
   - Enforces the maximum limit of tasks (`maxTasks = 100`).

### 3. **Task (Class)**
   - Represents a to-do task, encapsulating task details and status.
   - Attributes:
     - `taskName`: Stores the task's name.
     - `completed`: Boolean flag indicating if the task is completed.
   - Methods:
     - `markCompleted()`: Marks the task as completed.
     - `toString()`: Returns a formatted string with the task's name and status for easy display in the list.

### 4. **TaskManager (Concrete Class)**
   - Extends `TaskManagerBase` and implements the abstract methods for managing tasks:
     - `addTask()`: Adds a new task to the list.
     - `removeTask()`: Removes a task by its index.
     - `markTaskCompleted()`: Marks a task as completed.
   - Ensures proper validation of task limits and indices using exception handling (`TaskManagerException`).

### 5. **ToDoListApp (Main Application Class)**
   - Implements the `Application` class from JavaFX to create a graphical user interface for the task manager.
   - Main Components:
     - `Label`, `TextField`, `Button`: Standard JavaFX UI components for user interaction.
     - `ListView<String>`: Displays the list of tasks.
   - Allows users to:
     - Add tasks using a text input field.
     - Remove selected tasks from the list.
     - Mark tasks as completed.
   - Utilizes a background thread (`TaskListRefresher`) to periodically refresh the task list, demonstrating multithreading.

### 6. **TaskListRefresher (Runnable Class)**
   - A background thread that automatically refreshes the task list every 2 seconds.
   - Updates the UI through `Platform.runLater()`, ensuring that the task list is always up-to-date.
   
### 7. **Exception Handling**
   - Handles all task-related exceptions, such as adding more tasks than allowed or removing tasks with invalid indices, ensuring robust error handling.
   - Displays errors using JavaFX's `Alert` dialog to inform the user of issues.

### 8. **Multithreading**
   - Demonstrates multithreading by running the task list refresher in a separate thread. This background process enhances user experience by keeping the task list synchronized without manual refresh.

---

## Team Members
- **Backend and Functionality**: Tanay Aggarwal [230911278]
- **JavaFX and Frontend**: Aditya Mohan Jha [230911428]
- **Testing & Debugging**: Ishaan Kukreti [230911282]

---

## Conclusion
---

This To-Do List application demonstrates key software development principles like object-oriented programming (OOP), exception handling, and multithreading. With its easy-to-use interface and functional features, it not only addresses task management in the practical world.

We encourage you to explore the application as a reflection of our understanding and implementation of JavaFX and essential programming practices. It implements everything we have been taught and more.

