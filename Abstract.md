# To-Do List Application

## Abstract

This project implements a fully functioning To-Do List application using JavaFX for the graphical user interface (GUI). It integrates fundamental object-oriented programming (OOP) concepts, such as encapsulation, inheritance, and abstraction, along with exception handling and multithreading. The application allows users to add, remove, and mark tasks as completed, with a user-friendly interface. This project mimics a real world task-management system.

We have attempted to break down and give an outline for each function of the code we want to upload, should our abstract be accepted.

## Classes and Functions Overview

---

### 1. **TaskManagerException (Custom Exception)**
   - A custom-built error handler that manages issues related to task management.
   - It identifies and alerts when certain task-related actions cannot be performed, such as adding too many tasks or trying to change tasks that aren't valid.

### 2. **TaskManagerBase (Abstract Class)**
   - The main structure for handling the tasks.
   - It stores all tasks, keeps track of how many tasks exist, and provides the essential functions to add, remove, or mark tasks as completed.
   - Sets a limit to how many tasks can be handled.

### 3. **Task (Class)**
   - Represents a single task and keeps track of the task's name and whether it has been completed.
   - Allows marking tasks as completed and offers a way to display each task with its current status.

### 4. **TaskManager (Concrete Class)**
   - This class puts the task management functions into action.
   - It adds new tasks, removes tasks, and marks tasks as done.
   - Makes sure the number of tasks stays within limits and that each task exists before trying to update or remove it.

### 5. **ToDoListApp (Main Application Class)**
   - This is the main program that displays the to-do list and handles user input.
   - Users can add tasks, remove tasks, and mark them as completed using a simple interface.
   - The task list updates regularly to show the current tasks.

### 6. **TaskListRefresher (Runnable Class)**
   - Runs in the background and automatically refreshes the task list every few seconds, ensuring the user interface stays up-to-date.

### 7. **Exception Handling**
   - Catches and deals with common errors, such as trying to add more tasks than allowed or removing tasks that don’t exist, ensuring smooth user experience.

### 8. **Multithreading**
   - Uses a background process to keep the task list refreshed without interfering with other parts of the program, enhancing the overall usability.

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

