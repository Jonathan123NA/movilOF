package com.example.navbotdialog.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.navbotdialog.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        // Referencia al botón de escaneo
        View btnScan = view.findViewById(R.id.btn_scan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia el escáner y Configuración del escáner al hacer clic en el botón
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ScannerFragment.this);
                integrator.setOrientationLocked(false);  // Bloquea la orientación
                integrator.initiateScan();
            }
        });

        return view;
    }

    // Método para procesar el resultado del escaneo
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Aquí obtienes el contenido del código escaneado
                String scannedData = result.getContents();
                // Puedes hacer lo que necesites con el contenido escaneado
                // Por ejemplo, mostrarlo en un Toast
                Toast.makeText(getContext(), scannedData, Toast.LENGTH_SHORT).show();
            }
        }
    }
}