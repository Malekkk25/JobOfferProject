package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojet.adapter.CompagnyAdapter;
import com.example.miniprojet.entites.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class firstPageUser extends AppCompatActivity {


    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;

    RecyclerView recyclerView;

    List<User> compagnyList;

    DatabaseReference databaseReference;

    ValueEventListener valueEventListener;
    SearchView searchView;

    CompagnyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_user);


        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of Compagnies");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_comp) {
                    Intent i = new Intent(firstPageUser.this, firstPageUser.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(firstPageUser.this, ProfileUser.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_jobs) {
                    Intent i = new Intent(firstPageUser.this, listJobsUser.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_history) {
                    Intent i = new Intent(firstPageUser.this, HistoryUserActivity.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(firstPageUser.this, login_user.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(firstPageUser.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(firstPageUser.this);
        builder.setCancelable(true);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        compagnyList = new ArrayList<>();
        myAdapter = new CompagnyAdapter(firstPageUser.this, compagnyList);
        recyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        dialog.show();

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                compagnyList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    User comp = itemSnapshot.getValue(User.class);
                    if (comp != null && comp.getRole().equals("compagny")) {
                        comp.setIdUser(itemSnapshot.getKey());
                        compagnyList.add(comp);
                    }
                }
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchComp(newText);
                return true;
            }
        });
    }

    public void searchComp(String newText) {
        ArrayList<User> compArrayList = new ArrayList<>();
        for (User comp : compagnyList) {
            if (comp.getFullName().toLowerCase().contains(newText.toLowerCase())) {
                compArrayList.add(comp);
            }
        }
        myAdapter.searchCompagny(compArrayList);
    }
}
