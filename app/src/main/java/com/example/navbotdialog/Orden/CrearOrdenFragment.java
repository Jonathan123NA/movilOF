package com.example.navbotdialog.Orden;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.navbotdialog.R;

public class CrearOrdenFragment extends Fragment {

    EditText no_orden, fechaI, fechaF;
    Button btn_agregarA, btn_crearOrden;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_orden, container, false);

        no_orden = rootView.findViewById(R.id.co_orden);
        fechaI = rootView.findViewById(R.id.co_fecha_Inicio);
        fechaF = rootView.findViewById(R.id.co_fecha_Fin);
        btn_agregarA = rootView.findViewById(R.id.co_btn_agregararticulo);
        btn_crearOrden = rootView.findViewById(R.id.co_btn_crearOrden);

        btn_agregarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AgregarArticuloOrden.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}