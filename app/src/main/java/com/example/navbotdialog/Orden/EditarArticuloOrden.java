package com.example.navbotdialog.Orden;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.navbotdialog.APIUtils;
import com.example.navbotdialog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditarArticuloOrden extends AppCompatActivity {

    ImageView imagen_salvar;
    TextView eao_nombre_articulo;
    TextView eao_cantidad_disponible;
    EditText eao_cantidad;
    private Intent intent;
    int id_art;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_articulo_orden);

        Toolbar toolbar = findViewById(R.id.EAO_toolbar);
        setSupportActionBar(toolbar);

        intent = new Intent(EditarArticuloOrden.this, CrearOrden.class);

        imagen_salvar = findViewById(R.id.aao_imagen_salvar);
        eao_nombre_articulo = findViewById(R.id.eao_nombre_articulo);
        eao_cantidad_disponible= findViewById(R.id.eao_cantidad_disponible);
        eao_cantidad= findViewById(R.id.eao_cantidad);

        // Obtener el offerId de alguna manera (puede ser a través de un Intent)
        int idAr = getIntent().getIntExtra("idAr", -1);


        //Toast.makeText(EditarArticuloOrden.this, "ID "+idAr, Toast.LENGTH_SHORT).show();

        String url = APIUtils.getFullUrl("/api/temporalLA/" + idAr);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String id_articulo = response.getString("id_articulo");
                            String articulo = response.getString("articulo");
                            String cantidad = response.getString("cantidad");

                            // Llenar tus campos de edición con los valores obtenidos
                            eao_nombre_articulo.setText(articulo);
                            eao_cantidad.setText(cantidad);
                            id_art = Integer.parseInt(id_articulo);

                            cantidadDisponible(id_art);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la petición
                        System.out.println("Error al obtener los datos: "+error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error al obtener los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);

        imagen_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityString = eao_cantidad_disponible.getText().toString();
                String cantidadEditText = eao_cantidad.getText().toString();

                if (quantityString.isEmpty()) {
                    Toast.makeText(EditarArticuloOrden.this, "Cantidad no disponible", Toast.LENGTH_SHORT).show();
                } else if (cantidadEditText.isEmpty()) {
                    Toast.makeText(EditarArticuloOrden.this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show();
                } else {
                    int quantity = Integer.parseInt(quantityString);
                    int enteredQuantity = Integer.parseInt(cantidadEditText);

                    if (enteredQuantity <= 0) {
                        Toast.makeText(EditarArticuloOrden.this, "La cantidad debe ser mayor que cero", Toast.LENGTH_SHORT).show();
                    } else if (enteredQuantity > quantity) {
                        Toast.makeText(EditarArticuloOrden.this, "La cantidad excede la cantidad disponible", Toast.LENGTH_SHORT).show();
                    } else {
                        //Guardar los datos en la lista temporal
                        // Construir el objeto JSON para la solicitud PUT
                        JSONObject updatedData = new JSONObject();
                        try {
                            updatedData.put("cantidad", cantidadEditText);

                            // Realizar la solicitud PUT usando Volley
                            String putUrl = APIUtils.getFullUrl("/api/temporalLA/" + idAr);
                            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, putUrl, updatedData,
                                    response -> {
                                        // Manejar la respuesta exitosa
                                        Toast.makeText(EditarArticuloOrden.this, "Editado exitosamente", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(EditarArticuloOrden.this, CrearOrden.class);
                                        startActivity(intent);
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // Error al eliminar la publicación
                                            System.load("Error al eliminar: "+error.getMessage());
                                        }
                                    });

                            // Agregar la solicitud a la cola de Volley
                            Volley.newRequestQueue(EditarArticuloOrden.this).add(putRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Envia el otro fragmento
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

    }


    private void cantidadDisponible(int id_art) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = APIUtils.getFullUrl("/api/articulos/" + id_art);
        JsonObjectRequest  request = new JsonObjectRequest (Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                            try {

                                int quantity = response.getInt("cantidad");

                                Toast.makeText(EditarArticuloOrden.this, "Cantidad del articulo"+quantity, Toast.LENGTH_SHORT).show();

                                eao_cantidad_disponible.setText(String.valueOf(quantity));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // System.load("Error al obtener articulos: "+error.getMessage());
                        Log.e("EditarArticuloOrden", "Error al obtener artículo: " + error.getMessage());
                        Toast.makeText(EditarArticuloOrden.this, "Error al obtener artículos" +error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

}
