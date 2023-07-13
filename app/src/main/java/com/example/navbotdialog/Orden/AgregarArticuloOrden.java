package com.example.navbotdialog.Orden;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.navbotdialog.APIUtils;
import com.example.navbotdialog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgregarArticuloOrden extends AppCompatActivity {

    ImageView imagen_salvar;
    Spinner spinner;
    TextView quantityTextView;
    EditText aao_cantidad;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_articulo_orden);

        Toolbar toolbar = findViewById(R.id.AAO_toolbar);
        setSupportActionBar(toolbar);

        intent = new Intent(AgregarArticuloOrden.this, CrearOrden.class);

        imagen_salvar = findViewById(R.id.aao_imagen_salvar);
        spinner = findViewById(R.id.aao_Spinner);
        quantityTextView = findViewById(R.id.aao_cantidad_disponible);
        aao_cantidad= findViewById(R.id.aao_cantidad);
        imagen_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedArticle = spinner.getSelectedItem().toString();
                String quantityString = quantityTextView.getText().toString();
                String cantidadEditText = aao_cantidad.getText().toString();

                if (selectedArticle.isEmpty()) {
                    Toast.makeText(AgregarArticuloOrden.this, "Selecciona un artículo", Toast.LENGTH_SHORT).show();
                } else if (quantityString.isEmpty()) {
                    Toast.makeText(AgregarArticuloOrden.this, "Cantidad no disponible", Toast.LENGTH_SHORT).show();
                } else if (cantidadEditText.isEmpty()) {
                    Toast.makeText(AgregarArticuloOrden.this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
                } else {
                    int quantity = Integer.parseInt(quantityString);
                    int enteredQuantity = Integer.parseInt(cantidadEditText);

                    if (enteredQuantity <= 0) {
                        Toast.makeText(AgregarArticuloOrden.this, "La cantidad debe ser mayor que cero", Toast.LENGTH_SHORT).show();
                    } else if (enteredQuantity > quantity) {
                        Toast.makeText(AgregarArticuloOrden.this, "La cantidad excede la cantidad disponible", Toast.LENGTH_SHORT).show();
                    } else {
                        // La cantidad ingresada es válida, puedes proceder con la lógica de guardar la orden
                        // Aquí puedes agregar tu código para guardar la orden
                        startActivity(intent);
                    }
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Regresar a la pantalla anterior
            }
        });

        obtenerListaArticulos();
    }

    private void obtenerListaArticulos() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String route = "/api/articulos/";
        String url = APIUtils.getFullUrl(route);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> articleList = new ArrayList<>();
                        final List<Integer> quantityList = new ArrayList<>(); // Lista para almacenar las cantidades
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject article = response.getJSONObject(i);
                                String articleName = article.getString("nombre");
                                int quantity = article.getInt("cantidad");
                                articleList.add(articleName);
                                quantityList.add(quantity); // Agregar la cantidad a la lista
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarArticuloOrden.this,
                                android.R.layout.simple_spinner_item, articleList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                int selectedQuantity = quantityList.get(position); // Obtener la cantidad seleccionada
                                quantityTextView.setText(String.valueOf(selectedQuantity)); // Mostrar la cantidad correspondiente
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                // No hacer nada
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AgregarArticuloOrden.this, "Error al obtener artículos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

}