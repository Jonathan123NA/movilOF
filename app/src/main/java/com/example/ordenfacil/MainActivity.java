package com.example.ordenfacil;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordenfacil.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private int selectedTab = 1;

    private LinearLayout homeLayout, galleryLayout, slineshowLayout;
    private LottieAnimationView homeIcon, galleryIcon, slineshowIcon;
    private TextView homeText, galleryText, slineshowText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //declarar variables
        homeLayout = findViewById(R.id.Home_Layout);
        galleryLayout = findViewById(R.id.Gallery_Layout);
        slineshowLayout = findViewById(R.id.Slideshow_Layout);

        homeIcon = findViewById(R.id.lottie_layer_home);
        galleryIcon = findViewById(R.id.lottie_layer_Gallery);
        slineshowIcon = findViewById(R.id.lottie_layer_Slideshow);

        homeText = findViewById(R.id.Home_text);
        galleryText = findViewById(R.id.Gallery_text);
        slineshowText = findViewById(R.id.Slideshow_text);
        //

        //HOME
        homeLayout.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            MenuItem item = binding.navView.getMenu().findItem(R.id.nav_home);
            NavigationUI.onNavDestinationSelected(item,navController);
            DrawerLayout drawerLayout = binding.drawerLayout;
            drawerLayout.closeDrawer(GravityCompat.START);

            if (selectedTab != 1) {
                galleryText.setVisibility(View.GONE);
                slineshowText.setVisibility(View.GONE);
                galleryIcon.cancelAnimation();
                slineshowIcon.cancelAnimation();

                galleryLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                slineshowLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                homeLayout.setBackgroundResource(R.drawable.menu_item);
                homeText.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                homeLayout.startAnimation(scaleAnimation);
                homeIcon.playAnimation();
                selectedTab = 1;
            }
        });

        //GALLERY
        galleryLayout.setOnClickListener(view -> {
            NavController navController1 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            MenuItem item = binding.navView.getMenu().findItem(R.id.nav_gallery);
            NavigationUI.onNavDestinationSelected(item,navController1);
            DrawerLayout drawerLayout = binding.drawerLayout;
            drawerLayout.closeDrawer(GravityCompat.START);

            if (selectedTab != 2) {
                homeText.setVisibility(View.GONE);
                slineshowText.setVisibility(View.GONE);
                homeIcon.cancelAnimation();
                slineshowIcon.cancelAnimation();

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                slineshowLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                galleryLayout.setBackgroundResource(R.drawable.menu_item);
                galleryText.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                galleryLayout.startAnimation(scaleAnimation);
                galleryIcon.playAnimation();
                selectedTab = 2;
            }
        });

        //SLIDESHOW
        slineshowLayout.setOnClickListener(view -> {
            NavController navController2 = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            MenuItem item = binding.navView.getMenu().findItem(R.id.nav_slideshow);
            NavigationUI.onNavDestinationSelected(item,navController2);
            DrawerLayout drawerLayout = binding.drawerLayout;
            drawerLayout.closeDrawer(GravityCompat.START);

            if (selectedTab != 3) {
                homeText.setVisibility(View.GONE);
                galleryText.setVisibility(View.GONE);
                homeIcon.cancelAnimation();
                galleryIcon.cancelAnimation();

                homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                galleryLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                slineshowLayout.setBackgroundResource(R.drawable.menu_item);
                slineshowText.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                slineshowLayout.startAnimation(scaleAnimation);
                slineshowIcon.playAnimation();
                selectedTab = 3;
            }
        });

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
}