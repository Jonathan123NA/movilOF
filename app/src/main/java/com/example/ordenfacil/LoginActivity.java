package com.example.ordenfacil;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    boolean passwordVisible = false;
    ImageView buttonPassword;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emailEditText = findViewById(R.id.login_email);
        buttonPassword = findViewById(R.id.passwordIcon);
        passwordEditText = findViewById(R.id.login_password);

        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordVisible) {
                    // Si la contraseña es visible, cambia el tipo de entrada a 'textPassword'
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    buttonPassword.setImageResource(R.drawable.ico_visibility_off);
                    passwordVisible = false;
                } else {
                    // Si la contraseña está oculta, cambia el tipo de entrada a 'text'
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    buttonPassword.setImageResource(R.drawable.ico_visibility);
                    passwordVisible = true;
                }

                // Mueve el cursor al final del texto de la contraseña
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores de los campos de texto
                String correo = emailEditText.getText().toString().trim();
                String contraseña = passwordEditText.getText().toString().trim();

                // Validar campos vacíos
                if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña)) {
                    Toast.makeText(getApplicationContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                    // Crear Intent para pasar los datos a MainActivity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish(); // Opcionalmente, puedes finalizar LoginActivity después de pasar a MainActivity
                }
            }
        });
    }


}