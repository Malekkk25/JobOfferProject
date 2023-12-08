package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniprojet.adapter.CandidateAdapter;
import com.example.miniprojet.entites.Post;
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

public class listCandidatesActivity extends AppCompatActivity {

    public static String idJobfromlistCon = "";
    private DatabaseReference database;

    private FirebaseAuth authProfile;
    private List<Post> appliedJobsList;
    private List<User> UsersList;

    RecyclerView Condi_recyclerView;

    private CandidateAdapter candidateAdapter;

    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_candidates);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of Candidates");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_add) {
                    Intent i = new Intent(listCandidatesActivity.this, InsertJobPostActivity.class);
                    startActivity(i);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(listCandidatesActivity.this, ProfileCompagny.class);
                    startActivity(i);
                    finish();

                } else if (item.getItemId() == R.id.nav_jobs) {
                    Intent i = new Intent(listCandidatesActivity.this, firestPageCompagny.class);
                    startActivity(i);
                    finish();
                }  else if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent i=new Intent(listCandidatesActivity.this,login_user.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });





        Condi_recyclerView = findViewById(R.id.list_recycler_view);
        Condi_recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        Intent i = getIntent();

        appliedJobsList = new ArrayList<>();

        idJobfromlistCon = i.getStringExtra("idjob");
        UsersList = new ArrayList<>();
        candidateAdapter = new CandidateAdapter(listCandidatesActivity.this, UsersList);
        Condi_recyclerView.setAdapter(candidateAdapter);



        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference("Post");

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appliedJobsList.clear();
                UsersList.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null && post.getIdJob() != null && post.getIdJob().equals(idJobfromlistCon) && "en attente".equals(post.getEtat())) {
                        appliedJobsList.add(post);
                    }
                }

                // Assuming you have a method to get idJob from appliedJobsList
                List<String> idUserList = getIdUserList(appliedJobsList);

                DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference("Users");

                UsersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null && idUserList.contains(String.valueOf(user.getIdUser()))) {
                                UsersList.add(user);

                            }
                        }
                        candidateAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("DatabaseError", "Error fetching Users: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", "Error fetching Users: " + databaseError.getMessage());
            }
        });

    }


    private List<String> getIdUserList(List<Post> appliedJobsList) {
        List<String> idUserList = new ArrayList<>();
        for (Post post : appliedJobsList) {
            idUserList.add(post.getIdUser());
        }
        return idUserList;
    }
}

