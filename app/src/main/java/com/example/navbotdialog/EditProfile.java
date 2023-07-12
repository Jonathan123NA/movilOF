package com.example.navbotdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.navbotdialog.Fragment.PerfilFragment;

public class EditProfile extends AppCompatActivity {
private Button guardarPerfil;
private String nombre,apellidoPaterno,apellidoMaterno,password,confirmPassword;
private EditText nombreET, apellidoPET, apellidoMET, passwordET, confirmPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        guardarPerfil=findViewById(R.id.savePerfil);

        nombreET = findViewById(R.id.nombreET);
        apellidoPET = findViewById(R.id.apellidoPET);
        apellidoMET = findViewById(R.id.apellidoMET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);


        guardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editprofile();
            }
        });

    }

    private void editprofile(){
        nombre = nombreET.getText().toString().trim();
        apellidoPaterno = apellidoPET.getText().toString().trim();
        apellidoMaterno = apellidoMET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        confirmPassword = confirmPasswordET.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            nombreET.setError("Ingrese su nombre");
            nombreET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(apellidoPaterno)) {
            apellidoPET.setError("Ingrese su apellido paterno");
            apellidoPET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(apellidoMaterno)) {
            apellidoMET.setError("Ingrese su apellido materno");
            apellidoMET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Ingrese su contraseña");
            passwordET.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordET.setError("Confirme su contraseña");
            confirmPasswordET.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordET.setError("La contraseña debe tener al menos 6 caracteres");
            passwordET.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordET.setError("Las contraseñas no coinciden");
            confirmPasswordET.requestFocus();
            return;
        }else {
            Intent cmLogin = new Intent(getApplicationContext(), PerfilFragment.class);
            startActivity(cmLogin);
            finish();
        }
    }

}