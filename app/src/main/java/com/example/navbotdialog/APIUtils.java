package com.example.navbotdialog;

public class APIUtils {

    // Cambiar únicamente la dirección IP y el puerto según corresponda
    private static final String BASE_URL = "http://192.168.0.107:3000";

    public static String getFullUrl(String route) {
        return BASE_URL + route;
    }
}