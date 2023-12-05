package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

import java.util.HashMap;

public class ProfileUser extends AppCompatActivity {
    EditText name, email, contact, pass, confPass, exp, proExp, skills, education, summary;
    Button updateProfile,updateEmail,updatePassword;
    DatabaseReference database;

    String oldEmail, oldPassword;
    private FirebaseAuth authProfile;

    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        Intent i = getIntent();

        name = findViewById(R.id.name);

        contact = findViewById(R.id.contact);

        exp = findViewById(R.id.exp);
        proExp = findViewById(R.id.proExp);
        skills = findViewById(R.id.skills);
        education = findViewById(R.id.education);
        summary = findViewById(R.id.summary);
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
                if (item.getItemId()== R.id.nav_comp){
                    Intent i = new Intent(ProfileUser.this, firstPageUser.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_profile) {
                    Intent i = new Intent(ProfileUser.this, ProfileUser.class);
                    startActivity(i);
                }
                if (item.getItemId()== R.id.nav_jobs){
                    Intent i = new Intent(ProfileUser.this, listJobsUser.class);
                    startActivity(i);
                }
                if (item.getItemId()== R.id.nav_history){
                }
                if (item.getItemId()== R.id.nav_logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent i=new Intent(ProfileUser.this,login_user.class);
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
                    //email.setText(user.getEmail());
                    contact.setText(user.getContact());
                    exp.setText(String.valueOf(user.getExperiences()));
                    proExp.setText(user.getSpecialization());
                    skills.setText(user.getSkills());
                    education.setText(user.getEducation());
                    summary.setText(user.getSummary());

                    oldEmail = user.getEmail();
                    oldPassword = user.getPassword();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                boolean userFound = false;
                if (name.getText().toString().isEmpty()) {
                    name.setError("Required Name");
                    valid = false;
                }
               /* if (email.getText().toString().isEmpty()) {
                    email.setError("Required Email");
                    valid = false;
                }
                if (!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    email.setError("Invalid Email Format");
                    valid = false;
                }*/
                if (contact.getText().toString().isEmpty()) {
                    contact.setError("Required Contact");
                    valid = false;
                }
                if (contact.getText().toString().length() != 8) {
                    contact.setError("Exactly 8 numbers required");
                    valid = false;
                }
                /*if (!pass.getText().toString().isEmpty()) {
                    if (!pass.getText().toString().equals(confPass.getText().toString())) {
                        confPass.setError("Passwords do not match");
                        valid = false;
                    }
                }*/
                if (summary.getText().toString().isEmpty()) {
                    summary.setError("Required Summary");
                    valid = false;
                }
                if (exp.getText().toString().isEmpty()) {
                    exp.setError("Years of Experience Required");
                    valid = false;
                }
                if (proExp.getText().toString().isEmpty()) {
                    proExp.setError("Specialization Required");
                    valid = false;
                }

                if (skills.getText().toString().isEmpty()) {
                    skills.setError("Skills Required");
                    valid = false;
                }
                if (education.getText().toString().isEmpty()) {
                    education.setError("Education Required");
                    valid = false;
                }

                if (valid) {
                    updateData(userId, name.getText().toString(),  contact.getText().toString(),  summary.getText().toString(), Integer.valueOf(exp.getText().toString()), proExp.getText().toString(), skills.getText().toString(), education.getText().toString());
                }
            }
        });


    }

    private void updateData(String id, String name, String contact, String summary, int exp, String spec, String skills, String education) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", name);
        //userMap.put("email", email);
        userMap.put("contact", contact);

        userMap.put("summary", summary);
        userMap.put("experiences", exp);
        userMap.put("specialization", spec);
        userMap.put("skills", skills);
        userMap.put("education", education);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(id).updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileUser.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileUser.this, "Failed to update details", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        invalidateOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            if(item.getItemId()==R.id.menu_update_email){
                Intent i=new Intent(ProfileUser.this,UpdateEmail.class);
                startActivity(i);
                finish();
                return true;}

        if(item.getItemId()==R.id.menu_update_password){
            return true;}
else
                return super.onOptionsItemSelected(item);
        }

}
