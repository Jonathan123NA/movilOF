package com.example.navbotdialog.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.navbotdialog.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdenesFragment extends Fragment {

    private List<String> listaOrdenes;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ordenes, container, false);

        UserSession userSession = UserSession.getInstance();
        int userId = userSession.getUserId();

        System.out.println("ID: " + userId);


        RecyclerView recyclerView = rootView.findViewById(R.id.recycleViewOrdenes);

        listaOrdenes = new ArrayList<>();

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
                String data = listaOrdenes.get(position);
                String[] splitData = data.split(";");
                TextView no_orden = holder.itemView.findViewById(R.id.listaOs_Numero);
                TextView fInicio = holder.itemView.findViewById(R.id.listaOs_FInicio);
                TextView descripcion = holder.itemView.findViewById(R.id.listaOs_Descripcion);
                no_orden.setText(splitData[0]);

                String originalDate = splitData[1];
                String formattedDate = formatDate(originalDate);
                fInicio.setText(formattedDate);

                descripcion.setText(splitData[2]);

            }

            @Override
            public int getItemCount() {
                return listaOrdenes.size();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        makeGetRequest(userId);




        return rootView;
    }



    //Tabla ordennes usuario
    private void makeGetRequest(int userId) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = APIUtils.getFullUrl("/______" + userId);

        System.out.println("URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Crear una nueva lista temporal
                        List<String> tempList = new ArrayList<>();


                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);


                                String idPublicacion = jsonObject.getString("id_jobOffer");
                                String titulo = jsonObject.getString("jobTitle");
                                String vacancy = jsonObject.getString("vacancy");

                                String data = idPublicacion + ";" + titulo + ";" + vacancy;

                                System.out.println( "Titulo:  " +titulo);

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
                                listaOrdenes.clear();
                                // Agregar todos los elementos de la lista temporal
                                listaOrdenes.addAll(tempList);
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
                        Toast.makeText(getActivity(), "Error al obtener los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private String formatDate(String originalDate) {
        try {
            // Formato de fecha original
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

            // Formato de fecha deseado
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Parsear la fecha original
            Date date = inputFormat.parse(originalDate);

            // Formatear la fecha al nuevo formato
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return originalDate; // En caso de error, retorna la fecha original
        }
    }

}