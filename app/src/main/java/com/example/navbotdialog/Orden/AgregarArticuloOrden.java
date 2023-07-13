package com.example.navbotdialog.Orden;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import com.example.navbotdialog.R;

public class AgregarArticuloOrden extends AppCompatActivity {

    ImageView imagen_salvar;

    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_articulo_orden);

        Toolbar toolbar = findViewById(R.id.AAO_toolbar);
        setSupportActionBar(toolbar);

        intent = new Intent(AgregarArticuloOrden.this, CrearOrden.class);


        imagen_salvar = findViewById(R.id.aao_imagen_salvar);

        imagen_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);

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
}