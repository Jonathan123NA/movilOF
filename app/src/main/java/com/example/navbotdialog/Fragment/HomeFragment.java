package com.example.navbotdialog.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.navbotdialog.APIUtils;
import com.example.navbotdialog.Orden.CrearOrden;
import com.example.navbotdialog.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private List<String> listaPublicaciones;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview_ordenes);

        listaPublicaciones = new ArrayList<>();

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_ordenes, parent, false);

                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                String data = listaPublicaciones.get(position);
                String[] splitData = data.split(";");
                TextView id = holder.itemView.findViewById(R.id.listaOs_Numero);
                TextView fInicio = holder.itemView.findViewById(R.id.listaOs_FInicio);
                TextView descripcion = holder.itemView.findViewById(R.id.listaOs_Descripcion);

                id.setText(splitData[0]);
                fInicio.setText(splitData[1]);
                descripcion.setText(splitData[4]);
            }

            @Override
            public int getItemCount() {
                return listaPublicaciones.size();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        makeGetRequest();
        
        return rootView;

    }

    private void makeGetRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = APIUtils.getFullUrl("/api/consultasOrdenes/");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Crear una nueva lista temporal
                        List<String> tempList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String fecha_inicio = jsonObject.getString("fecha_inicio");
                                String fecha_fin = jsonObject.getString("fecha_fin");
                                String estado = jsonObject.getString("estado");
                                String descripcion = jsonObject.getString("descripcion");

                                String data = id + ";" + fecha_inicio + ";" + fecha_fin + ";" + estado + ";" + descripcion;

                                tempList.add(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Actualizar la lista principal en el hilo principal de la interfaz de usuario
                        getActivity().runOnUiThread(new Runnable() {
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
                        Toast.makeText(getContext(), "Error al obtener los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }
}