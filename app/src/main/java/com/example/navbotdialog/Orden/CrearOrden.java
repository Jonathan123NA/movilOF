package com.example.navbotdialog.Orden;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
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
import java.util.List;

public class CrearOrden extends AppCompatActivity {

    EditText no_orden, fechaI, fechaF;
    Button btn_agregarA, btn_crearOrden;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private List<String> listaPublicaciones;
    Spinner spinner;

    List<String> empleadoList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actuvity_crear_orden);

        obtenerListaEmpleados();

        Toolbar toolbar = findViewById(R.id.CO_toolbar);
        setSupportActionBar(toolbar);

        no_orden = findViewById(R.id.co_orden);
        fechaI = findViewById(R.id.co_fecha_Inicio);
        fechaF = findViewById(R.id.co_fecha_Fin);
        btn_agregarA = findViewById(R.id.co_btn_agregararticulo);
        btn_crearOrden = findViewById(R.id.co_btn_crearOrden);
        spinner = findViewById(R.id.co_Spinner);

        RecyclerView recyclerView = findViewById(R.id.lista_articulosAgregados);
        listaPublicaciones = new ArrayList<>();


        btn_agregarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CrearOrden.this, AgregarArticuloOrden.class);
                startActivity(intent);
            }
        });

        //Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_orden, parent, false);

                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                String data = listaPublicaciones.get(position);
                String[] splitData = data.split(";");
                TextView idPuestoTextView = holder.itemView.findViewById(R.id.listaO_id);
                TextView articulo = holder.itemView.findViewById(R.id.listaO_Articulo);
                TextView cantidad = holder.itemView.findViewById(R.id.listaO_Cantidad);

                //idPuestoTextView.setText(splitData[0]);
                articulo.setText(splitData[1]);
                cantidad.setText(splitData[2]);
                idPuestoTextView.setText(splitData[3]);

                String idAr = idPuestoTextView.getText().toString();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Llama al método para mostrar el menú emergente
                        showPopupMenu(v, idAr);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return listaPublicaciones.size();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        makeGetRequest();


    }

    private void makeGetRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = APIUtils.getFullUrl("/api/temporalLA/");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Crear una nueva lista temporal
                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String idtemporal_lista_articulos = jsonObject.getString("idtemporal_lista_articulos");
                                String id_articulo = jsonObject.getString("id_articulo");
                                String articulo = jsonObject.getString("articulo");
                                String cantidad = jsonObject.getString("cantidad");
                                String data = id_articulo + ";" + articulo + ";" + cantidad + ";" + idtemporal_lista_articulos;

                                tempList.add(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Actualizar la lista principal en el hilo principal de la interfaz de usuario
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Limpiar la lista principal
                                listaPublicaciones.clear();
                                // Agregar todos los elementos de la lista temporal
                                listaPublicaciones.addAll(tempList);
                                // Notificar al adaptador del cambio en los datos
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja el error de la petición
                        Toast.makeText(CrearOrden.this, "Error al obtener los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void obtenerListaEmpleados() {
        Toast.makeText(this, "Entro a lista", Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = APIUtils.getFullUrl("/api/usuariosRol/");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String idE = jsonObject.getString("id");
                                String nombre = jsonObject.getString("nombres");
                                String apellidos = jsonObject.getString("apellidos");

                                String data =  idE + "" + nombre + " " + apellidos;

                                empleadoList.add(data);

                                //Toast.makeText(CrearOrden.this, "emplados"+empleadoList, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CrearOrden.this,
                                android.R.layout.simple_spinner_item, empleadoList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CrearOrden.this, "Error al obtener artículos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

    private void showPopupMenu(View view, String idAr) {
        PopupMenu popupMenu = new PopupMenu(CrearOrden.this, view);
        popupMenu.inflate(R.menu.popup_menu_articulo);

        // Agrega un listener para manejar los clics en los elementos del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.PMA_editar:
                        Toast.makeText(CrearOrden.this, idAr +" Seleccionado", Toast.LENGTH_SHORT).show();
                        // Crear un Intent y agregar el offerId como extra
                        //Intent intent = new Intent(getActivity(), Editar_Post_Fragment.class);

                        //intent.putExtra("offerId", Integer.parseInt(idAr));
                        //startActivity(intent);
                        return true;
                    case R.id.PMA_eliminar:
                        deleteArticuloList(Integer.parseInt(idAr));
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Muestra el menú emergente
        popupMenu.show();
    }

    private void deleteArticuloList(int idAr) {
        //Toast.makeText(CrearOrden.this, "Vas a eliminar a "+idAr, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearOrden.this);
        builder.setTitle("Eliminar Articulo")
                .setMessage("¿Estás seguro de que quieres eliminar este articulo?")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        performDeleteRequest(idAr);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void performDeleteRequest(int idAr) {
        RequestQueue requestQueue = Volley.newRequestQueue(CrearOrden.this);
        String url = APIUtils.getFullUrl("temporalLA/" + idAr);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // La publicación se ha eliminado exitosamente
                        Toast.makeText(CrearOrden.this, "Articulo eliminado", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error al eliminar la publicación
                        Toast.makeText(CrearOrden.this, "Error al eliminar articulo: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.load("Error al eliminar: "+error.getMessage());
                    }
                });

        requestQueue.add(stringRequest);
    }

}