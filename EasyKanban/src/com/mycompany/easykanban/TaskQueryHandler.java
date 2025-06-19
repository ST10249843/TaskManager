package com.mycompany.easykanban; 

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskQueryHandler {
    private final ArrayList<JSONObject> allTasks = new ArrayList<>();
    private final ArrayList<JSONObject> toDoTasks = new ArrayList<>();
    private final ArrayList<JSONObject> inProgressTasks = new ArrayList<>();
    private final ArrayList<JSONObject> doneTasks = new ArrayList<>();
    private final ArrayList<String> taskCodes = new ArrayList<>();
    private final ArrayList<Integer> taskIDs = new ArrayList<>();

    public TaskQueryHandler() {
        loadTasksFromJson();
    }

    private void loadTasksFromJson() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(new FileReader("tasks.json"));

            for (Object obj : array) {
                JSONObject task = (JSONObject) obj;
                allTasks.add(task);

                // Normalize status
                String status = ((String) task.get("Status")).trim().toLowerCase();
                switch (status) {
                    case "to do" -> toDoTasks.add(task);
                    case "in progress" -> inProgressTasks.add(task);
                    case "done" -> doneTasks.add(task);
                }

                String code = ((String) task.get("TaskCode"));
                if (code != null) taskCodes.add(code.trim().toLowerCase());

                Long id = (Long) task.get("TaskID");
                if (id != null) taskIDs.add(Math.toIntExact(id));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading tasks.json:\n" + e.getMessage());
        }
    }

    public void showTaskQueryMenu() {
        String[] options = {
                "1. Show Senders & Recipients",
                "2. Longest Task Description",
                "3. Search by Task ID",
                "4. Search by Recipient",
                "5. Report: Done Tasks"
        };

        while (true) {
            int choice = JOptionPane.showOptionDialog(null,
                    "Select an option:", "Message Queries",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice == -1) break;

            switch (choice) {
                case 0 -> showSendersAndRecipients();
                case 1 -> showLongestDescription();
                case 2 -> searchByTaskID();
                case 3 -> searchByRecipient();
                case 4 -> showDoneTaskReport();
                default -> JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }

    private void showSendersAndRecipients() {
        if (allTasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks found.");
            return;
        }

        StringBuilder msg = new StringBuilder("Sender: System, Recipient List:\n\n");
        for (JSONObject task : allTasks) {
            msg.append("Recipient: ").append(task.get("Recipient")).append("\n");
        }

        JTextArea textArea = new JTextArea(msg.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "All Senders & Recipients", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showLongestDescription() {
        String longest = "";
        JSONObject longestTask = null;

        for (JSONObject task : allTasks) {
            String desc = (String) task.get("Description");
            if (desc != null && desc.length() > longest.length()) {
                longest = desc;
                longestTask = task;
            }
        }

        if (longestTask != null) {
            JOptionPane.showMessageDialog(null, "Longest Task Description:\n" +
                    longestTask.get("Description"));
        } else {
            JOptionPane.showMessageDialog(null, "No tasks available.");
        }
    }

    private void searchByTaskID() {
        String input = JOptionPane.showInputDialog("Enter Task ID:");
        if (input == null) return;

        try {
            int id = Integer.parseInt(input.trim());
            for (JSONObject task : allTasks) {
                Long storedId = (Long) task.get("TaskID");
                if (storedId != null && Math.toIntExact(storedId) == id) {
                    JOptionPane.showMessageDialog(null,
                            "Recipient: " + task.get("Recipient") +
                                    "\nDescription: " + task.get("Description"));
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Task ID not found.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number.");
        }
    }

    private void searchByRecipient() {
        String input = JOptionPane.showInputDialog("Enter recipient number:");
        if (input == null) return;

        String normalizedInput = input.trim();
        StringBuilder msg = new StringBuilder();

        for (JSONObject task : allTasks) {
            String recipient = (String) task.get("Recipient");
            if (recipient != null && recipient.trim().equalsIgnoreCase(normalizedInput)) {
                msg.append("Task ID: ").append(task.get("TaskID")).append("\n")
                        .append("Description: ").append(task.get("Description")).append("\n\n");
            }
        }

        if (msg.length() == 0) {
            msg.append("No tasks for that recipient.");
        }

        JTextArea textArea = new JTextArea(msg.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Tasks for Recipient", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showDoneTaskReport() {
        if (doneTasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No completed tasks.");
            return;
        }

        StringBuilder msg = new StringBuilder("Completed Tasks:\n\n");
        for (JSONObject task : doneTasks) {
            msg.append("ID: ").append(task.get("TaskID")).append("\n")
                    .append("Recipient: ").append(task.get("Recipient")).append("\n")
                    .append("Description: ").append(task.get("Description")).append("\n")
                    .append("Code: ").append(task.get("TaskCode")).append("\n\n");
        }

        JTextArea textArea = new JTextArea(msg.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Done Tasks Report", JOptionPane.INFORMATION_MESSAGE);
    }
}