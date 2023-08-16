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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.navbotdialog.APIUtils;
import com.example.navbotdialog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarArticuloOrden extends AppCompatActivity {

    ImageView imagen_salvar;
    Spinner spinner;
    TextView quantityTextView;
    EditText aao_cantidad;
    private Intent intent;
    List<String> articleList = new ArrayList<>();
    final List<Integer> quantityList = new ArrayList<>();
    final List<Integer> idList = new ArrayList<>(); // Lista para almacenar los IDs
    final List<String> descripcionList = new ArrayList<>();
    final List<String> tipoList = new ArrayList<>();
    final List<String> estadoList = new ArrayList<>();

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
                        //Guardar los datos en la lista temporal
                        guardarDatosEnTemporal(selectedArticle, enteredQuantity);

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

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject article = response.getJSONObject(i);

                                int id = article.getInt("id");
                                String articleName = article.getString("nombre");
                                String descripcion = article.getString("descripcion");
                                int quantity = article.getInt("cantidad");
                                String tipo = article.getString("tipo");
                                String estado = article.getString("estado");

                                idList.add(id);
                                articleList.add(articleName);
                                descripcionList.add(descripcion);
                                quantityList.add(quantity);
                                tipoList.add(tipo);
                                estadoList.add(estado);

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
    private void guardarDatosEnTemporal(String selectedArticle, int enteredQuantity) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String route = "/api/temporalLA/";
        String url = APIUtils.getFullUrl(route);

        JSONObject requestBody = new JSONObject();
        try {
            // Obtener el id del artículo seleccionado según el nombre
            int selectedArticleId = idList.get(articleList.indexOf(selectedArticle));

            requestBody.put("id_articulo", selectedArticleId);
            requestBody.put("articulo", selectedArticle);
            requestBody.put("cantidad", enteredQuantity);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor en caso de éxito
                        Toast.makeText(AgregarArticuloOrden.this, "Datos guardados en la lista temporal", Toast.LENGTH_SHORT).show();
                        // Puedes realizar otras acciones o redireccionar a otra actividad aquí
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error en caso de que falle la solicitud
                        Toast.makeText(AgregarArticuloOrden.this, "Error al guardar datos en la lista temporal", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }




}