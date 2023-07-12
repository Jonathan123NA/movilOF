package com.example.navbotdialog.Orden;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.navbotdialog.R;
import com.example.navbotdialog.forgetPassword;
import com.example.navbotdialog.verifyCode;

public class AgregarArticuloOrden extends AppCompatActivity {

    ImageView imagen_salvar;

    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_articulo_orden);

        intent = new Intent(AgregarArticuloOrden.this, CrearOrdenFragment.class);


        imagen_salvar = findViewById(R.id.aao_imagen_salvar);

        imagen_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);

            }
        });
    }
}