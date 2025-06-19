package com.mycompany.easykanban;

public class Login {
    private String storedUsername;
    private String storedPassword;
    private String storedCellNumber;

    private String inputUsername;
    private String inputPassword;

    public boolean checkUsername(String username) {
        return username.length() <= 4;
    }

    public boolean checkPasswordComplexity(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{5,}$";
        return password.matches(regex);
    }

    public boolean checkCellphoneNumber(String phoneNumber) {
        String regex = "^[0-9]{10,}$";
        return phoneNumber.matches(regex);
    }

    public String registerUser(String username, String password, String cellNumber) {
        if (!checkUsername(username)) {
            return "Username is too long.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password not complex enough.";
        }
        if (!checkCellphoneNumber(cellNumber)) {
            return "Cellphone number must be 10 digits.";
        }

        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellNumber = cellNumber;
        return "User registered successfully!";
    }

    public boolean loginUser(String username, String password) {
        this.inputUsername = username;
        this.inputPassword = password;
        return storedUsername.equals(username) && storedPassword.equals(password);
    }

    public String returnLoginStatus() {
        if (storedUsername == null || storedPassword == null) return "No user registered.";
        if (loginUser(inputUsername, inputPassword)) return "Login successful!";
        return "Login failed.";
    }

    public String getStoredUsername() {
        return storedUsername;
    }
}
