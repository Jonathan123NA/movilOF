package com.example.navbotdialog.Scanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/api/orden_articulos")
    Call<List<OrderArticle>> getAllOrderArticles();
    // Agrega otros métodos si los necesitas
}

