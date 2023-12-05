package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.miniprojet.entites.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class firstPageUser extends AppCompatActivity  {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar; // Change to androidx.appcompat.widget.Toolbar
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_user);
        Intent i=getIntent();
        User user=(User) i.getSerializableExtra("user");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()== R.id.nav_comp){
                    Intent i = new Intent(firstPageUser.this, ProfileUser.class);
                    i.putExtra("user",user);
                    startActivity(i);


                }
                if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(firstPageUser.this, ProfileUser.class);
                    i.putExtra("user",user);
                    startActivity(i);

                }


                if (item.getItemId()== R.id.nav_jobs){


                }
                if (item.getItemId()== R.id.nav_history){

                }

                if (item.getItemId()== R.id.nav_logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent i=new Intent(firstPageUser.this,login_user.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }


        });



}}
