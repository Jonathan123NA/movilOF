package com.example.navbotdialog;

public class APIUtils {

    // Cambiar únicamente la dirección IP y el puerto según corresponda
<<<<<<< HEAD
    private static final String BASE_URL = "http://192.168.0.19:3000";
=======
    private static final String BASE_URL = "http://192.168.0.107:3000";
>>>>>>> 885e83252e81e7ac71166578e22bd50d4244cf8d

    public static String getFullUrl(String route) {
        return BASE_URL + route;
    }
}