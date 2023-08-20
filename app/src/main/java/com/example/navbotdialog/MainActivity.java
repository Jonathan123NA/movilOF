package com.example.navbotdialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.example.navbotdialog.Orden.AgregarArticuloOrden;
import com.example.navbotdialog.Orden.CrearOrden;
import com.example.navbotdialog.Scanner.ScannerActivity;
import com.example.navbotdialog.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private int selectedTab = 1;
    FloatingActionButton fab;
    private LinearLayout homeLayout, ordenLayout, notifLayout, perfilLayout;
    private LottieAnimationView homeIcon, ordenIcon, notifIcon, perfilIcon;
    private TextView homeText, ordenText, notifText, perfilText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //declarar variables
        homeLayout = findViewById(R.id.Home_Layout);
        ordenLayout = findViewById(R.id.Ordenes_Layout);
        notifLayout = findViewById(R.id.Notificaciones_Layout);
        perfilLayout = findViewById(R.id.Perfil_Layout);
        fab = findViewById(R.id.fab);

        /*homeIcon = findViewById(R.id.lottie_layer_home);
        ordenIcon = findViewById(R.id.lottie_layer_Ordenes);
        notifIcon = findViewById(R.id.lottie_layer_Notificaciones);
        perfilIcon = findViewById(R.id.lottie_layer_Perfil);*/

        homeText = findViewById(R.id.Home_text);
        ordenText = findViewById(R.id.Ordenes_text);
        notifText = findViewById(R.id.Notificaciones_text);
        perfilText = findViewById(R.id.Perfil_text);
        //

        //HOME
        homeLayout.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            MenuItem item = binding.navView.getMenu().findItem(R.id.nav_home);
            NavigationUI.onNavDestinationSelected(item,navController);
            DrawerLayout drawerLayout = binding.drawerLayout;
            drawerLayout.closeDrawer(GravityCompat.START);

           if (selectedTab != 1) {
                //visivilidad del texto
                ordenText.setVisibility(View.GONE);
                notifText.setVisibility(View.GONE);
                perfilText.setVisibility(View.GONE);

                //Cancelar animaciones
                /*ordenIcon.cancelAnimation();
                notifIcon.cancelAnimation();
                perfilIcon.cancelAnimation();
                */

                //Color de fondo
                ordenLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                notifLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                perfilLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                homeLayout.setBackgroundResource(R.drawable.menu_item);
                homeText.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                homeLayout.startAnimation(scaleAnimation);
                //homeIcon.playAnimation();
                selectedTab = 1;
            }
        });

        //Ordenes
        ordenLayout.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            MenuItem item = binding.navView.getMenu().findItem(R.id.nav_ordenes);
            NavigationUI.onNavDestinationSelected(item,navController);
            DrawerLayout drawerLayout = binding.drawerLayout;
            drawerLayout.closeDrawer(GravityCompat.START);

            if (selectedTab != 2) {
                //visivilidad del texto
                homeText.setVisibility(View.GONE);
                notifText.setVisibility(View.GONE);
                perfilText.setVisibility(View.GONE);

                //Cancelar animaciones
                /*homeIcon.cancelAnimation();
                notifIcon.cancelAnimation();
                perfilIcon.cancelAnimation();
                */

                //Color de fondo
                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                notifLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                perfilLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                ordenLayout.setBackgroundResource(R.drawable.menu_item);
                ordenText.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                ordenLayout.startAnimation(scaleAnimation);
                //ordenIcon.playAnimation();
                selectedTab = 2;
            }


        });

        //Notificaciones
        notifLayout.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            MenuItem item = binding.navView.getMenu().findItem(R.id.nav_notificaciones);
            NavigationUI.onNavDestinationSelected(item,navController);
            DrawerLayout drawerLayout = binding.drawerLayout;
            drawerLayout.closeDrawer(GravityCompat.START);

            if (selectedTab != 3) {
                //visivilidad del texto
                ordenText.setVisibility(View.GONE);
                homeText.setVisibility(View.GONE);
                perfilText.setVisibility(View.GONE);

                //Cancelar animaciones
                /*ordenIcon.cancelAnimation();
                notifIcon.cancelAnimation();
                perfilIcon.cancelAnimation();
                */

                //Color de fondo
                ordenLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                perfilLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                notifLayout.setBackgroundResource(R.drawable.menu_item);
                notifText.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                notifLayout.startAnimation(scaleAnimation);
                //notifIcon.playAnimation();
                selectedTab = 3;
            }
        });

        //Perfil
        perfilLayout.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            MenuItem item = binding.navView.getMenu().findItem(R.id.nav_perfil);
            NavigationUI.onNavDestinationSelected(item,navController);
            DrawerLayout drawerLayout = binding.drawerLayout;
            drawerLayout.closeDrawer(GravityCompat.START);

            if (selectedTab != 4) {
                //visivilidad del texto
                ordenText.setVisibility(View.GONE);
                notifText.setVisibility(View.GONE);
                homeText.setVisibility(View.GONE);

                //Cancelar animaciones
                /*ordenIcon.cancelAnimation();
                notifIcon.cancelAnimation();
                perfilIcon.cancelAnimation();
                */

                //Color de fondo
                ordenLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                notifLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                perfilLayout.setBackgroundResource(R.drawable.menu_item);
                perfilText.setVisibility(View.VISIBLE);
               ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                perfilLayout.startAnimation(scaleAnimation);
                //perfilIcon.playAnimation();
                selectedTab = 4;
            }


        });

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();

            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_ordenes, R.id.nav_notificaciones, R.id.nav_perfil)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Salir
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Obtener el ID del elemento de menú seleccionado
                int id = item.getItemId();

                if (id == R.id.salir) {
                    // Mostrar el cuadro de diálogo de confirmación antes de salir
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("¿Estás seguro de que deseas salir?");
                    builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Realizar acciones para continuar (redirigir al login, por ejemplo)
                            // ...

                            // Redirigir al login
                             Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                             startActivity(loginIntent);
                             finish();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.show();
                }

                // ...

                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout post_layot = dialog.findViewById(R.id.buttomsheetlayout_orden);
        post_layot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, CrearOrden.class);
                startActivity(intent);

            }
        });

        LinearLayout post_layot2 = dialog.findViewById(R.id.buttomsheetlayout_scanner);
        post_layot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}