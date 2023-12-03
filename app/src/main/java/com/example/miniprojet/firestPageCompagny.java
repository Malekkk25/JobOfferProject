package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.miniprojet.entites.User;
import com.google.android.material.navigation.NavigationView;

public class firestPageCompagny extends AppCompatActivity {

    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firest_page_compagny);
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
                if (item.getItemId()== R.id.nav_add){
                    Intent i = new Intent(firestPageCompagny.this, InsertJobPostActivity.class);
                    startActivity(i);

                }
                if (item.getItemId() == R.id.nav_profile) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(firestPageCompagny.this, "profile", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);

                    fragementR(new compagnyProfile(), bundle);

                }


                if (item.getItemId()== R.id.nav_jobs){
                    Intent i=new Intent(firestPageCompagny.this,listJobs_compagny.class);
                    startActivity(i);

                }
                if (item.getItemId()== R.id.nav_candidates){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(firestPageCompagny.this, "history", Toast.LENGTH_SHORT).show();
                    fragementR(new listCandidates());
                }

                if (item.getItemId()== R.id.nav_logout){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(firestPageCompagny.this, "logout", Toast.LENGTH_SHORT).show();
                    fragementR(new fragementHistory());
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            private void fragementR(Fragment fragment){
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.commit();
            }

            private void fragementR(Fragment fragment, Bundle bundle) {
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });



    }}