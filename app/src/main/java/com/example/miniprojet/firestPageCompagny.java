package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.miniprojet.adapter.MyAdapter;
import com.example.miniprojet.entites.Job;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class firestPageCompagny extends AppCompatActivity {

    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    FloatingActionButton fab;
    RecyclerView recyclerView;

    List<Job> jobList;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    SearchView searchView;

    MyAdapter myAdapter;
    DatabaseReference database;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firest_page_compagny);

        fab = findViewById(R.id.btn_fab);
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of Jobs");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_add) {
                    Intent i = new Intent(firestPageCompagny.this, InsertJobPostActivity.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(firestPageCompagny.this, ProfileCompagny.class);
                    startActivity(i);

                } else if (item.getItemId() == R.id.nav_jobs) {

                } else if (item.getItemId() == R.id.nav_candidates) {
                    Intent i = new Intent(firestPageCompagny.this, firestPageCompagny.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent i=new Intent(firestPageCompagny.this,login_user.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(firestPageCompagny.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(firestPageCompagny.this);
        builder.setCancelable(true);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        jobList = new ArrayList<>();
        myAdapter = new MyAdapter(firestPageCompagny.this, jobList);
        recyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Job");

        dialog.show();

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Job job = itemSnapshot.getValue(Job.class);


                    if (job != null && job.getIdComp() != null && job.getIdComp().equals(userId)) {
                        job.setKey(itemSnapshot.getKey());
                        jobList.add(job);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchJob(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(firestPageCompagny.this, InsertJobPostActivity.class);
                startActivity(intent);
            }
        });
    }

    public void searchJob(String newText) {
        ArrayList<Job> jobArrayList = new ArrayList<>();
        for (Job job : jobList) {
            if (job.getJobTitle().toLowerCase().contains(newText.toLowerCase())) {
                jobArrayList.add(job);
            }
        }
        myAdapter.searchJob(jobArrayList);
    }}