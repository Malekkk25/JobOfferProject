package com.example.miniprojet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniprojet.entites.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InsertJobPostActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    TextView etDate;
    EditText etJobTitle, etJobDate ,etJobDescription, etJobLocation, etCategory, etExperience, etSkills;
    Button saveJob;
    Long maxid;
    DatabaseReference database;
    DatePickerDialog.OnDateSetListener setListener;

    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_job_post);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Job Post");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()== R.id.nav_add){

                    Intent i=new Intent(InsertJobPostActivity.this,InsertJobPostActivity.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_profile) {
                }
                if (item.getItemId()== R.id.nav_jobs){
                    Intent i=new Intent(InsertJobPostActivity.this,listJobs_compagny.class);
                    startActivity(i);
                }
                if (item.getItemId()== R.id.nav_candidates){
                }
                if (item.getItemId()== R.id.nav_logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent i=new Intent(InsertJobPostActivity.this,login_user.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }});


        etDate = findViewById(R.id.job_startdate);
        etJobTitle = findViewById(R.id.job_title);
        etJobDate = findViewById(R.id.job_startdate);
        etJobDescription = findViewById(R.id.job_Description);
        etJobLocation = findViewById(R.id.job_location);
        etCategory = findViewById(R.id.job_Category);
        etExperience = findViewById(R.id.job_Experience);
        etSkills = findViewById(R.id.job_skillsrequired);
        saveJob = findViewById(R.id.btn_job_post);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        database= FirebaseDatabase.getInstance().getReference("Job");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid = 0L;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Job job = dataSnapshot.getValue(Job.class);
                        if(job == null){
                            maxid=0L;
                            break;

                        }
                        else
                        {
                            if (job.getIdJob() > maxid)
                            maxid = job.getIdJob();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(InsertJobPostActivity.this, (view, year1, month1, dayOfMonth) -> {
                month1 = month1 + 1;
                String date = dayOfMonth + "/" + month1 + "/" + year1;
                etDate.setText(date);
            }, year, month, dayofmonth);
            datePickerDialog.show();
        });
        setListener = (view, year12, month12, dayOfMonth) -> {
            month12 = month12 + 1;
            String date = dayOfMonth + "/" + month12 + "/" + year12;
            etDate.setText(date);
        };

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();


                        }
                    }
                }

        );

        saveJob.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boolean valid=true;
                if(etJobTitle.getText().toString().isEmpty()){
                    etJobTitle.setError("Job Tiltle Required");
                    valid=false;
                }
                if(etJobDate.getText().toString().isEmpty()){
                    etJobDate.setError("Start Date Required");
                    valid=false;
                }
                if(etJobLocation.getText().toString().isEmpty()){
                    etJobLocation.setError("Job Location Required");
                    valid=false;
                }
                if(etJobDescription.getText().toString().isEmpty()){
                    etJobDescription.setError("Job Description Required");
                    valid=false;
                }
                if(etCategory.getText().toString().isEmpty()){
                    etCategory.setError("Job Category Required");
                    valid=false;
                }
                if(etJobDescription.getText().toString().isEmpty()){
                    etJobDescription.setError("Job Description Required");
                    valid=false;
                }
                if(etSkills.getText().toString().isEmpty()){
                    etSkills.setError("Skills Required");
                    valid=false;
                }
                if(etExperience.getText().toString().isEmpty()){
                    etExperience.setError("Experience Required");
                    valid=false;
                }
                if(valid==true)
                saveData(userId);
            }
        });


    }

    public void  saveData(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertJobPostActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        uploadData(id);

    }

    public void uploadData(String id){

        String jobTitle = etJobTitle.getText().toString();
        String jobDate = etJobDate.getText().toString();
        String jobDescription = etJobDescription.getText().toString();
        String jobLocation = etJobLocation.getText().toString();
        String category = etCategory.getText().toString();
        String experience = etExperience.getText().toString();
        String skills = etSkills.getText().toString();


        Job job = new Job(maxid+1,jobTitle, jobDescription, jobLocation, category, jobDate, experience, skills);
        job.setIdComp(id);

        DatabaseReference refrenceJob=FirebaseDatabase.getInstance().getReference("Job");
        refrenceJob.child(job.getIdJob().toString()).setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                Toast.makeText(InsertJobPostActivity.this, "Job Posted Successfully", Toast.LENGTH_SHORT).show();
                etJobTitle.setText("");
                etJobDate.setText("");
                etJobDescription.setText("");
                etJobLocation.setText("");
                etCategory.setText("");
                etExperience.setText("");
                etSkills.setText("");
                finish();}
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertJobPostActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}