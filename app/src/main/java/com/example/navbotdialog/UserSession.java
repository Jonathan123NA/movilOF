package com.example.navbotdialog;

public class UserSession {
    private static UserSession instance;
    private int userId;

    private UserSession() {
        // Constructor privado para evitar la creación de instancias directas
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

