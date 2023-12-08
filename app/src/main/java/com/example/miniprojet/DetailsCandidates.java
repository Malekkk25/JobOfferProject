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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miniprojet.adapter.CandidateAdapter;
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

public class DetailsCandidates extends AppCompatActivity {

    private EditText name, contact, exp, proExp, skills, education, summary;
    private Button ShowPdf,Accept_btn,Reject_btn;
    private String iduser, idjob;
    private DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_candidates);

        // Retrieve iduser from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("iduser")) {
            iduser = intent.getStringExtra("iduser");
        }

        idjob = listCandidatesActivity.idJobfromlistCon;


        // Initialize Firebase reference
        usersReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize your EditText fields
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        exp = findViewById(R.id.exp);
        proExp = findViewById(R.id.proExp);
        skills = findViewById(R.id.skills);
        education = findViewById(R.id.education);
        summary = findViewById(R.id.summary);
        ShowPdf = findViewById(R.id.Show_cv);
        Accept_btn = findViewById(R.id.Accept);
        Reject_btn = findViewById(R.id.Reject);

        // Fetch user details
        fetchUserDetails();

        ShowPdf.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailsCandidates.this,PdfView.class);
            startActivity(intent1);

        });
        Accept_btn.setOnClickListener(v -> updateJobApplicationStatus("Accepted"));
        Reject_btn.setOnClickListener(v -> updateJobApplicationStatus("Rejected"));
        System.out.println("iduser"+iduser + "idjob"+listCandidatesActivity.idJobfromlistCon);
    }
    private void updateJobApplicationStatus(String newStatus) {
        DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("Post");

        postReference.orderByChild("idUser").equalTo(iduser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null && post.getIdJob().equals(idjob)) {
                        // Update the status to the new value
                        post.setEtat(newStatus);

                        // Save the updated post back to the database
                        postSnapshot.getRef().setValue(post)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DetailsCandidates.this,
                                                "Job application status updated to " + newStatus,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetailsCandidates.this,
                                                "Failed to update job application status",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                Intent intent = new Intent(DetailsCandidates.this, listCandidatesActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }




    private void fetchUserDetails() {
        if (iduser == null) {
            System.out.println("iduser is null");
        }
            usersReference.child(iduser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        // Directly set the text without casting
                        name.setText(user.getFullName());

                        // Set other EditText fields in a similar way
                        contact.setText(user.getContact());
                        exp.setText("test");
                        proExp.setText(user.getSpecialization());
                        skills.setText(user.getSkills());
                        education.setText(user.getEducation());
                        summary.setText(user.getSummary());

                        // Set up other fields as needed
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }
}