package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.miniprojet.entites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileCompagny extends AppCompatActivity {

    EditText name, email, contact, pass, confPass,summary;
    Button updateProfile;
    ArrayList<User> listUsers=new ArrayList<>();
    DatabaseReference database;

    private FirebaseAuth authProfile;

    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_compagny);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);

        summary=findViewById(R.id.summary);
        updateProfile = findViewById(R.id.updateProfile);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_add) {
                    Intent i = new Intent(ProfileCompagny.this, InsertJobPostActivity.class);
                    startActivity(i);
                    finish();
                } else if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(ProfileCompagny.this, ProfileCompagny.class);
                    startActivity(i);
                    finish();

                } else if (item.getItemId() == R.id.nav_jobs) {
                    Intent i = new Intent(ProfileCompagny.this, firestPageCompagny.class);
                    startActivity(i);
                    finish();

                }
                else if (item.getItemId() == R.id.nav_add) {
                    Intent i = new Intent(ProfileCompagny.this, InsertJobPostActivity.class);
                    startActivity(i);
                    finish();
                }
                else if (item.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent i=new Intent(ProfileCompagny.this,login_user.class);
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
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if (user != null) {
                    name.setText(user.getFullName());
                    email.setText(user.getEmail());
                    contact.setText(user.getContact());
                    summary.setText(user.getSummary());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;
                boolean userFound=false;
                if(name.getText().toString().isEmpty() ){
                    name.setError("Required Name");
                    valid=false;
                }
                if(email.getText().toString().isEmpty()){
                    email.setError("Required Email");
                    valid=false;
                }
                if(!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                    email.setError("Invalid Email Format");
                    valid=false;
                }
                if(contact.getText().toString().isEmpty()){
                    contact.setError("Required Contact");
                    valid=false;
                }
                if(contact.getText().toString().length()<8 ||contact.getText().toString().length()>8  ){
                    contact.setError("At least 8 numbers");
                    valid=false;
                }
                /*if(!pass.getText().toString().isEmpty()){
                    if(!pass.getText().toString().equals(confPass.getText().toString())){
                        confPass.setError("Wrong Password");
                        valid=false;
                    }}*/
                if(summary.getText().toString().isEmpty()){
                    summary.setError("Required Summary");
                    valid=false;
                }
                if(valid ==true){
                    updateData(userId,name.getText().toString(),email.getText().toString(),contact.getText().toString(),summary.getText().toString());
                }
            }
        });


    }

    private void updateData(String id,String name, String email, String contact,  String summary) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", name);
        userMap.put("email", email);
        userMap.put("contact", contact);

        userMap.put("summary", summary);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(id).updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileCompagny.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileCompagny.this, "Failed to update details", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileCompagny.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

