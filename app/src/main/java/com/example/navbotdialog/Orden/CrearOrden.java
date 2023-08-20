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

import android.util.Log;
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
import com.example.navbotdialog.MainActivity;
import com.example.navbotdialog.R;
import com.example.navbotdialog.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CrearOrden extends AppCompatActivity {

    EditText no_orden, fechaI, fechaF, co_descripcion;
    Button btn_agregarA, btn_crearOrden;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private List<String> listaPublicaciones;
    private List<Integer> idArticulosList = new ArrayList<>();
    private List<Integer> idEmpleadosList = new ArrayList<>();
    Spinner spinner;

    List<String> articleList = new ArrayList<>();
    List<String> empleadoList = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actuvity_crear_orden);

        obtenerListaEmpleados();

        UserSession userSession = UserSession.getInstance();
        int userId = userSession.getUserId();
        //Toast.makeText(this, "Entraste con: "+ userId, Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.CO_toolbar);
        setSupportActionBar(toolbar);

        no_orden = findViewById(R.id.co_orden);
        fechaI = findViewById(R.id.co_fecha_Inicio);
        fechaF = findViewById(R.id.co_fecha_Fin);
        co_descripcion = findViewById(R.id.co_descripcion);
        btn_agregarA = findViewById(R.id.co_btn_agregararticulo);
        btn_crearOrden = findViewById(R.id.co_btn_crearOrden);
        spinner = findViewById(R.id.co_Spinner);

        obtenerid();

        RecyclerView recyclerView = findViewById(R.id.lista_articulosAgregados);
        listaPublicaciones = new ArrayList<>();
        btn_agregarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CrearOrden.this, AgregarArticuloOrden.class);
                startActivity(intent);
            }
        });

        btn_crearOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obtenerid();
                guardarOrden(userId);
                guardarOrdenUsuario();
                guardarOrdenArticulo();
                // Llama a la función para eliminar todos los registros
                deleteAllRecords();

                Toast.makeText(CrearOrden.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CrearOrden.this, MainActivity.class);
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
                // Llama a la función para eliminar todos los registros
                deleteAllRecords();
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

    private void obtenerid() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = APIUtils.getFullUrl("/api/nextIDOrden/");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int nextOrderId = response.getInt("nextOrderId");
                            no_orden.setText(String.valueOf(nextOrderId));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CrearOrden.this, "Error al obtener el siguiente id", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        queue.add(request);
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
                                idArticulosList.add(Integer.valueOf(id_articulo));

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

                                idEmpleadosList.add(Integer.valueOf(idE));
                                articleList.add(nombre + " " + apellidos);
                                String data =  idE + " " + nombre + " " + apellidos;
                                String data2 =  nombre + " " + apellidos;

                                empleadoList.add(data2);

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
                        //Toast.makeText(CrearOrden.this, idAr +" Seleccionado", Toast.LENGTH_SHORT).show();
                        // Crear un Intent y agregar el offerId como extra
                        Intent intent = new Intent(CrearOrden.this, EditarArticuloOrden.class);
                        intent.putExtra("idAr", Integer.parseInt(idAr));
                        startActivity(intent);
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
        String url = APIUtils.getFullUrl("/api/temporalLA/" + idAr);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // La publicación se ha eliminado exitosamente
                        Toast.makeText(CrearOrden.this, "Articulo eliminado", Toast.LENGTH_SHORT).show();
                        //RECARGAR LISTA
                        makeGetRequest();
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

    private void guardarOrden(int userId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String route = "/api/creacionOrden/";
        String url = APIUtils.getFullUrl(route);

        // Obtén los valores reales de los campos de texto
        String fechaIS = fechaI.getText().toString(); // Reemplaza editTextFechaInicio con tu EditText correspondiente
        String fechaFS = fechaF.getText().toString(); // Reemplaza editTextFechaFin con tu EditText correspondiente
        String co_descripcionS = co_descripcion.getText().toString(); // Reemplaza editTextDescripcion con tu EditText correspondiente

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("fecha_inicio", fechaIS);
            requestBody.put("fecha_fin", fechaFS);
            requestBody.put("estado", 1);
            requestBody.put("descripcion", co_descripcionS);
            requestBody.put("id_creador", 1);

            Log.d("Request Body", requestBody.toString()); // Imprime el JSON en la consola

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CrearOrden.this, "Datos guardados", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error en caso de que falle la solicitud
                        Toast.makeText(CrearOrden.this, "Error al guardar datos"+error, Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }

    private void guardarOrdenUsuario() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String route = "/api/orden_usuarios/";
        String url = APIUtils.getFullUrl(route);
        int valor = Integer.parseInt(no_orden.getText().toString());

        JSONObject requestBody = new JSONObject();
        try {

            String selectedArticle = spinner.getSelectedItem().toString();
            // Obtener el id del artículo seleccionado según el nombre
            int selectedArticleId = idEmpleadosList.get(empleadoList.indexOf(selectedArticle));

            Toast.makeText(this, "Seleccione id del empleado:"+selectedArticleId, Toast.LENGTH_SHORT).show();

            //Toast.makeText(this, "El valor de ORDER ID : "+ valor, Toast.LENGTH_SHORT).show();
            requestBody.put("id_orden",valor);
            requestBody.put("id_usuario", selectedArticleId);
            Log.d("Request Body", requestBody.toString()); // Imprime el JSON en la consola

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error en caso de que falle la solicitud
                        Toast.makeText(CrearOrden.this, "Error al guardar datos en Orden Usuario", Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(request);
    }

    private void guardarOrdenArticulo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String route = "/api/orden_articulos/";
        String url = APIUtils.getFullUrl(route);
        int valor = Integer.parseInt(no_orden.getText().toString());
        //Toast.makeText(this, "Numero de la orden es "+valor, Toast.LENGTH_SHORT).show();

        JSONObject requestBody = new JSONObject();
        try {

            JSONArray idArticulosArray = new JSONArray(idArticulosList); // Convierte la lista de IDs a JSONArray

            for (int i = 0; i < idArticulosArray.length(); i++) {
                int idArticulo = idArticulosArray.getInt(i);

                JSONObject articleData = new JSONObject();
                articleData.put("id_orden", valor); // Mismo ID de orden para cada artículo
                articleData.put("id_articulo", idArticulo); // ID del artículo actual

                Log.d("Request Body", articleData.toString()); // Imprime el JSON en la consola
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor en caso de éxito
                        Toast.makeText(CrearOrden.this, "Datos guardados orden articulos", Toast.LENGTH_SHORT).show();
                        // Puedes realizar otras acciones o redireccionar a otra actividad aquí
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error en caso de que falle la solicitud
                        Toast.makeText(CrearOrden.this, "Error al guardar datos en guardarOrdenArticulo", Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(request);
    }

    private void deleteAllRecords() {
        String route = "/api/temporalLAAL/";
        String url = APIUtils.getFullUrl(route);

        // Crea una nueva solicitud de Volley
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // La respuesta del servidor, aquí puedes manejar la respuesta si es necesario
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error en la solicitud, aquí puedes manejar el error si es necesario
            }
        });

        // Agrega la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}