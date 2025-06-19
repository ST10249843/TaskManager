# TaskManager
PROG5121 POE


# EasyKanban Java Application

EasyKanban is a desktop-based task management system developed in Java using Swing. It replicates a simplified Kanban board where users can register, log in, create tasks, and interact with them through a GUI. The application supports task creation, categorization by status, search, and reporting — with all data persisted in a JSON file.

---

## Overview

EasyKanban allows users to:
- Add and track tasks categorized as "To Do", "In Progress", or "Done".
- View sender and recipient information for tasks.
- Search tasks by ID or recipient number.
- Delete tasks based on their unique task code.
- View a report of completed tasks.

All tasks are saved to and loaded from a `tasks.json` file using the `json-simple` library.

---

## Functional Requirements

### 1. User Registration
- Username must be **4 characters or fewer**.
- Password must be at least **5 characters long**, and include:
  - **1 uppercase letter**
  - **1 digit**
  - **1 special character**
- Cell number must be a **valid 10-digit** number.

### 2. User Login
- Validates username and password against registered credentials.

### 3. Task Creation
- Prompts the user for:
  - **Recipient number** (international format e.g. `+27831234567`)
  - **Description** (max 300 characters)
  - **Status**: "To Do", "In Progress", "Done"
- Assigns an auto-incremented Task ID and a Task Code.

### 4. Task Queries
- **Show Senders & Recipients**: Lists all tasks with sender ("System") and recipient.
- **Longest Task Description**: Displays the task with the longest description.
- **Search by Task ID**: Retrieves and shows task info by ID.
- **Search by Recipient Number**: Lists all tasks sent to a given recipient.
- **Delete Task by Code**: Removes a task by its code (in-memory only).
- **Report on Completed Tasks**: Lists all tasks with status "Done".

---

## 🔧 Setup Instructions

### 1. Requirements
- Java JDK 17+
- NetBeans (recommended) or any Java IDE
- `json-simple-1.1.1.jar` for JSON operations

### 2. Add json-simple Library
- Download `json-simple-1.1.1.jar`
- In NetBeans:  
  `Right-click project > Properties > Libraries > Add JAR/Folder`

### 3. Compile and Run (Command Line Example)
```bash
javac -cp .;json-simple-1.1.1.jar com/mycompany/easykanban/*.java
java -cp .;json-simple-1.1.1.jar com.mycompany.easykanban.Main



