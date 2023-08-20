package com.example.navbotdialog.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navbotdialog.LoginActivity;
import com.example.navbotdialog.MainActivity;
import com.example.navbotdialog.R;
import com.example.navbotdialog.databinding.FragmentNotificacionesBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends Activity {

    private int scannedArticleId = -1; // Valor por defecto para indicar que no se ha escaneado
    private OrderArticleAdapter orderArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scanner);

        // Obtén una referencia al botón de escaneo
        Button btnScan = findViewById(R.id.btn_scan);

        // Agrega un OnClickListener al botón
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia el escáner
                IntentIntegrator integrator = new IntentIntegrator(ScannerActivity.this);
                // Habilita el sonido del escáner
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Procesa el resultado del escaneo
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Aquí obtienes el contenido del código escaneado
                String scannedData = result.getContents();

                try {
                    scannedArticleId = Integer.parseInt(scannedData);
                } catch (NumberFormatException e) {
                    scannedArticleId = -1;
                }

                // Actualiza la vista del RecyclerView si es necesario
                if (orderArticleAdapter != null) {
                    orderArticleAdapter.setScannedArticleId(scannedArticleId);
                    orderArticleAdapter.notifyDataSetChanged();
                }

                // Puedes hacer lo que necesites con el contenido escaneado
                Toast.makeText(this, scannedData, Toast.LENGTH_SHORT).show();

                // Validar si el escaneo es un número antes de iniciar la nueva actividad
                if (scannedArticleId != -1) {
                    /*
                    Intent scannerIntent = new Intent(ScannerActivity.this, FragmentNotificacionesBinding.class);
                    startActivity(scannerIntent);
                    finish();
                    */
                } else {
                    // Mostrar un mensaje de error en caso de que el escaneo no sea un número válido
                    Toast.makeText(this, "El escaneo no es un ID de articulo válido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error al escanear", Toast.LENGTH_SHORT).show();
            }
        }
    }


}