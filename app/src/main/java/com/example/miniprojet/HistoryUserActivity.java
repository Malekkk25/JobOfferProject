package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.miniprojet.adapter.CompagnyAdapter;
import com.example.miniprojet.adapter.HistoryAdapter;
import com.example.miniprojet.adapter.MyAdapter;
import com.example.miniprojet.entites.Job;
import com.example.miniprojet.entites.Post;
import com.example.miniprojet.entites.User;
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

public class HistoryUserActivity extends AppCompatActivity {

    private ListView historyListView;
    private List<Post> appliedJobsList;
    private HistoryAdapter historyAdapter;
    private FirebaseAuth authProfile;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    RecyclerView recyclerView;

    List<Job> jobList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_user);

        recyclerView = findViewById(R.id.recycler_view);



        appliedJobsList = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_comp) {
                    Intent i = new Intent(HistoryUserActivity.this, firstPageUser.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(HistoryUserActivity.this, ProfileUser.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_jobs) {
                    Intent i = new Intent(HistoryUserActivity.this, listJobsUser.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_history) {

                   // Intent i = new Intent(HistoryUserActivity.this, HistoryUserActivity.class);
                    //startActivity(i);
                }
                if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(HistoryUserActivity.this, login_user.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(HistoryUserActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryUserActivity.this);
        builder.setCancelable(true);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        jobList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this,  jobList);
        recyclerView.setAdapter(historyAdapter);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userId = firebaseUser.getUid();

        if (userId != null) {
            DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("Post");

            historyRef.orderByChild("idUser").equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    appliedJobsList.clear();
                    jobList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Post post = snapshot.getValue(Post.class);
                        if (post != null && post.getIdUser().equals(userId)) {
                            appliedJobsList.add(post);
                        }
                    }

                    // Assuming you have a method to get idJob from appliedJobsList
                    List<String> idJobList = getIdJobList(appliedJobsList);

                    DatabaseReference jobsRef = FirebaseDatabase.getInstance().getReference("Job");

                    jobsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Job job = snapshot.getValue(Job.class);
                                if (job != null && idJobList.contains(String.valueOf(job.getIdJob()))) {
                                    job.setKey(snapshot.getKey());
                                    jobList.add(job);
                                }
                            }

                            historyAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    // Method to get idJobList from appliedJobsList
    private List<String> getIdJobList(List<Post> appliedJobsList) {
        List<String> idJobList = new ArrayList<>();
        for (Post post : appliedJobsList) {
            idJobList.add(post.getIdJob());
        }
        return idJobList;
    }
}