package com.example.navbotdialog.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.navbotdialog.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scanner);
        // Inicia el escáner
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();

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
                // Puedes hacer lo que necesites con el contenido escaneado
                Toast.makeText(this, scannedData, Toast.LENGTH_SHORT).show();

            } else {
                // Si el escaneo no fue exitoso
            }
        }
    }

}