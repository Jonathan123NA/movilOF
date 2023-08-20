package com.example.navbotdialog;
import static android.content.ContentValues.TAG;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.navbotdialog.Fragment.PerfilFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    TextView forgetPassword;
    LinearLayout enviar_a_Registro;
    boolean passwordVisible = false;
    ImageView buttonPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        forgetPassword = findViewById(R.id.forgetPassword);
        enviar_a_Registro = findViewById(R.id.enviar_a_Registro);

        buttonPassword = findViewById(R.id.passwordIcon);

        iniciarSesion();

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

        enviar_a_Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Definir las animaciones
                ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat( enviar_a_Registro, "scaleX", 1f, 0.5f, 1f);
                scaleXAnimation.setDuration(500);

                ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat( enviar_a_Registro, "scaleY", 1f, 0.5f, 1f);
                scaleYAnimation.setDuration(500);

                // Crear un conjunto de animaciones
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleXAnimation, scaleYAnimation);

                // Iniciar las animaciones
                animatorSet.start();
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Definir las animaciones
                ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat( forgetPassword, "scaleX", 1f, 0.5f, 1f);
                scaleXAnimation.setDuration(500);

                ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat( forgetPassword, "scaleY", 1f, 0.5f, 1f);
                scaleYAnimation.setDuration(500);

                // Crear un conjunto de animaciones
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleXAnimation, scaleYAnimation);

                // Iniciar las animaciones
                animatorSet.start();
                Intent intent = new Intent(LoginActivity.this, forgetPassword.class);
                startActivity(intent);
            }
        });

    }

    private boolean isValidEmail(String email) {
        // Expresión regular para validar el formato del correo electrónico
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Verificar si el correo electrónico coincide con el patrón
        return email.matches(emailPattern);
    }

    private void iniciarSesion() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validar campos vacíos
                if (email.isEmpty()) {
                    emailEditText.setError("Campo obligatorio");
                } else if (!isValidEmail(email)) {
                    emailEditText.setError("Correo electrónico inválido");
                } else if (password.isEmpty()) {
                    passwordEditText.setError("Campo obligatorio");
                } else {
                    // Los campos no están vacíos, realizar el inicio de sesión

                    // Crear una solicitud HTTP para verificar las credenciales
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    String route = "/api/verificar-credenciales"; // Ruta específica del endpoint
                    String url = APIUtils.getFullUrl(route);

                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Manejar la respuesta del servidor
                                    try {
                                        boolean loggedIn = response.getBoolean("loggedIn");
                                        if (loggedIn) {
                                            // Credenciales válidas, iniciar sesión en la aplicación
                                            //int userId = response.getInt("id"); // Obtener el ID del usuario de la respuesta del servidor
                                            //UserSession.getInstance().setUserId(userId); // Establecer el ID del usuario en la sesión
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // Credenciales inválidas, mostrar mensaje de error
                                            Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Manejar el error de la solicitud HTTP
                                    Log.d("Error de conexion", String.valueOf(error));
                                    Toast.makeText(LoginActivity.this, "Error de conexión"+error, Toast.LENGTH_SHORT).show();

                                }
                            });

                    // Agregar la solicitud a la cola
                    queue.add(request);
                }
            }
        });

    }

}
