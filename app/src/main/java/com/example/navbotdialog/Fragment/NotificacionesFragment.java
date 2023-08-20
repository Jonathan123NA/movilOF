package com.example.navbotdialog.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.navbotdialog.APIUtils;
import com.example.navbotdialog.R;
import com.example.navbotdialog.Scanner.ApiService;
import com.example.navbotdialog.Scanner.OrderArticle;
import com.example.navbotdialog.Scanner.OrderArticleAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificacionesFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderArticleAdapter orderArticleAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        recyclerView = view.findViewById(R.id.rv_ordenesarticulos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Iniciar la solicitud a la API para obtener las órdenes de artículos
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUtils.getFullUrl("")) // Aquí ya estás proporcionando la URL completa
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<OrderArticle>> call = apiService.getAllOrderArticles();
        call.enqueue(new Callback<List<OrderArticle>>() {
            @Override
            public void onResponse(Call<List<OrderArticle>> call, Response<List<OrderArticle>> response) {
                if (response.isSuccessful()) {
                    List<OrderArticle> orderArticleList = response.body();
                    // Configura el adaptador y asigna la lista al RecyclerView
                    for (OrderArticle orderArticle : orderArticleList) {
                        Log.d("AdapterDebug", "ID Orden: " + orderArticle.getId_orden() + ", ID Artículo: " + orderArticle.getId_articulo());
                    }
                    orderArticleAdapter = new OrderArticleAdapter(orderArticleList);
                    recyclerView.setAdapter(orderArticleAdapter);
                } else {
                    // Manejo de errores
                    Toast.makeText(getContext(), "Error en la respuesta de la API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderArticle>> call, Throwable t) {
                // Manejo de errores
                Toast.makeText(getContext(), "Error en la solicitud a la API", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}

