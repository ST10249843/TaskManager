package com.mycompany.easykanban;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class TaskManager {
    private static int taskCount = 0;
    private static final JSONArray tasks = new JSONArray();

    private final Random random = new Random();

    // Check if Task ID is valid (<= 5 digits)
    public boolean checkTaskID(int id) {
        return String.valueOf(id).length() <= 5;
    }

    // Check recipient phone number format (e.g., +271234567890)
    public boolean checkRecipientCell(String cell) {
        return cell.matches("^\\+\\d{2}\\d{10}$");
    }

    // Create task code: ID-FIRSTWORD-LASTWORD (all in caps)
    public String createCode(int id, String description) {
        String[] words = description.trim().split("\\s+");
        if (words.length < 2) return id + "-INVALID-DESCRIPTION";
        return (id + "-" + words[0] + "-" + words[words.length - 1]).toUpperCase();
    }

    // Create a task, generate ID/code, add to list, and return the task
    public JSONObject createTask(String recipient, String description, String status) {
        taskCount++;

        int id;
        do {
            id = 10000 + random.nextInt(90000); // random 5-digit ID
        } while (!checkTaskID(id));

        String code = createCode(id, description);

        JSONObject task = new JSONObject();
        task.put("TaskID", id);
        task.put("TaskCode", code);
        task.put("Recipient", recipient);
        task.put("Description", description);
        task.put("Status", status);

        tasks.add(task);
        return task;
    }

    // Return a formatted string report of all tasks
    public String getTasksReport() {
        if (tasks.isEmpty()) {
            return "No tasks to show.";
        }

        StringBuilder sb = new StringBuilder();
        for (Object obj : tasks) {
            if (obj instanceof JSONObject) {
                JSONObject task = (JSONObject) obj;
                sb.append("----- Task -----\n");
                sb.append("ID: ").append(task.get("TaskID")).append("\n");
                sb.append("Code: ").append(task.get("TaskCode")).append("\n");
                sb.append("Recipient: ").append(task.get("Recipient")).append("\n");
                sb.append("Description: ").append(task.get("Description")).append("\n");
                sb.append("Status: ").append(task.get("Status")).append("\n\n");
            }
        }
        sb.append("Total messages sent: ").append(taskCount);
        return sb.toString();
    }

    // Save all tasks to a JSON file
    public void storeMessages() {
        try (FileWriter file = new FileWriter("tasks.json")) {
            file.write(tasks.toJSONString());
            file.flush();
            System.out.println("Tasks saved to tasks.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
