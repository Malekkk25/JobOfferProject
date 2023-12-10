package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniprojet.entites.Post;
import com.example.miniprojet.entites.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsCandidates extends AppCompatActivity {

    private EditText name, contact, exp, proExp, skills, education, summary;
    private Button ShowPdf,Accept_btn,Reject_btn;
    private String iduser, idjob;
    private DatabaseReference usersReference;

    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_candidates);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("iduser")) {
            iduser = intent.getStringExtra("iduser");
        }

        idjob = listCandidatesActivity.idJobfromlistCon;
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Candidate's Detail");


        usersReference = FirebaseDatabase.getInstance().getReference("Users");


        name = findViewById(R.id.name);

        name.setFocusable(false);
        name.setClickable(false);
        name.setCursorVisible(false);

        contact = findViewById(R.id.contact);

        contact.setFocusable(false);
        contact.setClickable(false);
        contact.setCursorVisible(false);


        exp = findViewById(R.id.exp);


        exp.setFocusable(false);
        exp.setClickable(false);
        exp.setCursorVisible(false);

        proExp = findViewById(R.id.proExp);

        proExp.setFocusable(false);
        proExp.setClickable(false);
        proExp.setCursorVisible(false);

        skills = findViewById(R.id.skills);

        skills.setFocusable(false);
        skills.setClickable(false);
        skills.setCursorVisible(false);

        education = findViewById(R.id.education);

        education.setFocusable(false);
        education.setClickable(false);
        education.setCursorVisible(false);

        summary = findViewById(R.id.summary);

        summary.setFocusable(false);
        summary.setClickable(false);
        summary.setCursorVisible(false);
        ShowPdf = findViewById(R.id.Show_cv);
        Accept_btn = findViewById(R.id.Accept);
        Reject_btn = findViewById(R.id.Reject);


        fetchUserDetails();


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

                        post.setEtat(newStatus);


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

                        name.setText(user.getFullName());
                        contact.setText(user.getContact());
                        exp.setText("test");
                        proExp.setText(user.getSpecialization());
                        skills.setText(user.getSkills());
                        education.setText(user.getEducation());
                        summary.setText(user.getSummary());
                        ShowPdf.setOnClickListener(v -> {
                            Intent intent1 = new Intent(DetailsCandidates.this,PdfView.class);
                            intent1.putExtra("userId",user.getIdUser());
                            startActivity(intent1);

                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}