package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.miniprojet.adapter.MyAdapter;
import com.example.miniprojet.entites.Job;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class listJobsUser extends AppCompatActivity {

    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;

    RecyclerView recyclerView;

    List<Job> jobList;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    SearchView searchView;

    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jobs_user);

        Intent i = getIntent();
        String nom = i.getStringExtra("nomCom");
        String idComp = i.getStringExtra("idComp");

        Log.d("CompagnyAdapter", "idComp: " + idComp);
        Log.d("CompagnyAdapter", "nomComp: " + nom);


        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if(nom != null)
        getSupportActionBar().setTitle("List of Jobs of "+ nom);
        else
            getSupportActionBar().setTitle("List of Jobs");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_comp) {
                    Intent i = new Intent(listJobsUser.this, firstPageUser.class);
                    startActivity(i);
                    finish();
                }
                if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(listJobsUser.this, ProfileUser.class);
                    startActivity(i);
                    finish();
                }
                if (item.getItemId() == R.id.nav_jobs) {
                    Intent i = new Intent(listJobsUser.this, listJobsUser.class);
                    startActivity(i);
                    finish();
                }
                if (item.getItemId() == R.id.nav_history) {
                    Intent i = new Intent(listJobsUser.this, HistoryUserActivity.class);
                    startActivity(i);
                    finish();

                }
                if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(listJobsUser.this, login_user.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(listJobsUser.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(listJobsUser.this);
        builder.setCancelable(true);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        jobList = new ArrayList<>();
        myAdapter = new MyAdapter(listJobsUser.this, jobList);
        recyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Job");

        dialog.show();



        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Job job = itemSnapshot.getValue(Job.class);
                    if(idComp != null){
                        if(job != null && job.getIdComp() != null && job.getIdComp().equals(idComp)){
                            job.setKey(itemSnapshot.getKey());
                            jobList.add(job);
                        }
                    } else {
                        if (job != null) {
                            job.setKey(itemSnapshot.getKey());
                            jobList.add(job);
                        }
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
    }


    public void searchJob(String newText) {
        ArrayList<Job> jobArrayList = new ArrayList<>();
        for (Job job : jobList) {
            if (job.getJobTitle().toLowerCase().contains(newText.toLowerCase())) {
                jobArrayList.add(job);
            }
        }
        myAdapter.searchJob(jobArrayList);
    }





}