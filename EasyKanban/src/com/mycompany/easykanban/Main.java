package com.mycompany.easykanban;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        Login login = new Login();
        TaskManager manager = new TaskManager();

        // Register
        JOptionPane.showMessageDialog(null, "=== Register ===");

        String username = JOptionPane.showInputDialog("Username (max 4 chars):");
        String password = JOptionPane.showInputDialog("Password (min 5 chars, 1 uppercase, 1 digit, 1 special char):");
        String cell = JOptionPane.showInputDialog("Cell number (10 digits):");

        String regMsg = login.registerUser(username, password, cell);
        JOptionPane.showMessageDialog(null, regMsg);

        // Login
        JOptionPane.showMessageDialog(null, "=== Login ===");

        String loginUsername = JOptionPane.showInputDialog("Username:");
        String loginPassword = JOptionPane.showInputDialog("Password:");

        if (!login.loginUser(loginUsername, loginPassword)) {
            JOptionPane.showMessageDialog(null, "Login failed.");
            return;
        }

        JOptionPane.showMessageDialog(null, "Login successful!");

        boolean running = true;
        while (running) {
            String[] options = {"Add Task", "Show Recently Sent Messages", "Quit"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            switch (choice) {
                case 0: // Add Task
                    int count = 0;
                    while (true) {
                        String inputCount = JOptionPane.showInputDialog("How many tasks to add?");
                        try {
                            count = Integer.parseInt(inputCount);
                            if (count <= 0) {
                                JOptionPane.showMessageDialog(null, "Please enter a positive number.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid number. Try again.");
                        }
                    }

                    for (int i = 0; i < count; i++) {
                        JOptionPane.showMessageDialog(null, "Task #" + (i + 1));

                        String recipient;
                        while (true) {
                            recipient = JOptionPane.showInputDialog("Recipient phone (+CCXXXXXXXXXX):");
                            if (recipient == null) break; // user cancelled
                            if (manager.checkRecipientCell(recipient)) break;
                            JOptionPane.showMessageDialog(null, "Invalid phone number format.");
                        }
                        if (recipient == null) break;

                        String desc;
                        while (true) {
                            desc = JOptionPane.showInputDialog("Task description (max 300 chars):");
                            if (desc == null) break; // user cancelled
                            if (desc.length() <= 300) break;
                            JOptionPane.showMessageDialog(null, "Description too long.");
                        }
                        if (desc == null) break;

                        String status = submitTaskGui();

                        manager.createTask(recipient, desc, status);
                        JOptionPane.showMessageDialog(null, "Task added successfully.");
                    }

                    // After all tasks added, show report
                    JOptionPane.showMessageDialog(null, manager.getTasksReport());
                    break;

                case 1: // Show Recently Sent Messages
                    TaskQueryHandler handler = new TaskQueryHandler();
                    handler.showTaskQueryMenu();
                    break;

                case 2: // Quit
                case JOptionPane.CLOSED_OPTION:
                    manager.storeMessages();
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    running = false;
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }

    // Helper method to get task status from GUI options
    private static String submitTaskGui() {
        String[] statusOptions = {"To Do", "In Progress", "Done"};
        int statusChoice = JOptionPane.showOptionDialog(null, "Select task status:", "Task Status",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, statusOptions, statusOptions[0]);

        if (statusChoice < 0 || statusChoice >= statusOptions.length) {
            return "Unknown";
        }
        return statusOptions[statusChoice];
    }
}
