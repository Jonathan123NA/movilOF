package com.example.navbotdialog.Orden;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.navbotdialog.R;

public class CrearOrden extends AppCompatActivity {

    EditText no_orden, fechaI, fechaF;
    Button btn_agregarA, btn_crearOrden;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actuvity_crear_orden);

        Toolbar toolbar = findViewById(R.id.CO_toolbar);
        setSupportActionBar(toolbar);

        no_orden = findViewById(R.id.co_orden);
        fechaI = findViewById(R.id.co_fecha_Inicio);
        fechaF = findViewById(R.id.co_fecha_Fin);
        btn_agregarA = findViewById(R.id.co_btn_agregararticulo);
        btn_crearOrden = findViewById(R.id.co_btn_crearOrden);

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


    }

}