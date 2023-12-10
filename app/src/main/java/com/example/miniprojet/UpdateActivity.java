package com.example.miniprojet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.miniprojet.entites.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class UpdateActivity extends AppCompatActivity {

    TextView uJobTitle, utvJobDate, utvJobDescription, utvJobLocation, utvCategory, utvExperience, utvSkills;
    Button UpdateBtn;

    String title,desc,location,category,experience,skills,date;
    androidx.appcompat.widget.Toolbar toolbar;
    String key = "";
    String idComp="";
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private  final int MAPS_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        UpdateBtn = findViewById(R.id.btn_update_job_post);
        uJobTitle = findViewById(R.id.update_job_title);
        utvJobDate = findViewById(R.id.update_job_startdate);
        utvJobDescription = findViewById(R.id.update_job_Description);
        utvJobLocation = findViewById(R.id.update_job_location);
        utvCategory = findViewById(R.id.update_job_Category);
        utvExperience = findViewById(R.id.update_job_Experience);
        utvSkills = findViewById(R.id.update_job_skillsrequired);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Job Post");

        Bundle mBundle = getIntent().getExtras();
        if(mBundle != null){

            uJobTitle.setText(mBundle.getString("jobTitle"));
            utvJobDate.setText(mBundle.getString("jobDate"));
            utvJobDescription.setText(mBundle.getString("jobDescription"));
            utvJobLocation.setText(mBundle.getString("jobLocation"));
            utvCategory.setText(mBundle.getString("category"));
            utvExperience.setText(mBundle.getString("experience"));
            utvSkills.setText(mBundle.getString("skills"));
            key = mBundle.getString("idValue");
            idComp=mBundle.getString("idComp");
        }
        if (key != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Job").child(key);
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(UpdateActivity.this, firestPageCompagny.class);
                startActivity(intent);
            }
        });
        utvJobLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, MapsActivity2.class);
                startActivityForResult(intent, MAPS_ACTIVITY_REQUEST_CODE);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAPS_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra("address");
            utvJobLocation.setText(address);
        }
    }

    public void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        updateData();
        dialog.dismiss();


    }
    public void updateData(){
        title = uJobTitle.getText().toString().trim();
        desc = utvJobDescription.getText().toString().trim();
        location = utvJobLocation.getText().toString().trim();
        category = utvCategory.getText().toString().trim();
        experience = utvExperience.getText().toString().trim();
        skills = utvSkills.getText().toString().trim();
        date = utvJobDate.getText().toString().trim();
        Job job = new Job(title,desc,location,category,experience,skills,date);
        job.setIdComp(idComp);
        databaseReference.setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UpdateActivity.this, "Job Post Updated", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}