package com.example.miniprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kotlinx.coroutines.Job;

public class DetailActivity extends AppCompatActivity {

    TextView tvJobTitle, tvJobDate, tvJobDescription, tvJobLocation, tvCategory, tvExperience, tvSkills , tvIdJob;
    FloatingActionButton deleteBtn , updateBtn;

    String key = "";
    Long idJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvJobTitle = findViewById(R.id.detail_title);
        tvJobDate = findViewById(R.id.detail_Date);
        tvJobDescription = findViewById(R.id.detail_Description);
        tvJobLocation = findViewById(R.id.detail_Location);
        tvCategory = findViewById(R.id.detail_Category);
        tvExperience = findViewById(R.id.detail_Experience);
        tvSkills = findViewById(R.id.detail_Skills);
        deleteBtn = findViewById(R.id.deleteButton);
        updateBtn = findViewById(R.id.editButton);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tvJobTitle.setText(bundle.getString("jobTitle"));
            tvJobDate.setText(bundle.getString("jobDate"));
            tvJobDescription.setText(bundle.getString("jobDescription"));
            tvJobLocation.setText(bundle.getString("jobLocation"));
            tvCategory.setText(bundle.getString("category"));
            tvExperience.setText(bundle.getString("experience"));
            tvSkills.setText(bundle.getString("skills"));
            key = bundle.getString("idValue");

        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this job?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Job").child(key);
                                databaseReference.removeValue();
                                Toast.makeText(DetailActivity.this, "Job Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
                intent.putExtra("jobTitle", tvJobTitle.getText().toString());
                intent.putExtra("jobDate", tvJobDate.getText().toString());
                intent.putExtra("jobDescription", tvJobDescription.getText().toString());
                intent.putExtra("jobLocation", tvJobLocation.getText().toString());
                intent.putExtra("category", tvCategory.getText().toString());
                intent.putExtra("experience", tvExperience.getText().toString());
                intent.putExtra("skills", tvSkills.getText().toString());
                intent.putExtra("idValue", key);

                startActivity(intent);
            }
        });

    }
}