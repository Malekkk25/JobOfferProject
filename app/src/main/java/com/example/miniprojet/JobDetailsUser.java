package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniprojet.entites.Job;
import com.example.miniprojet.entites.Post;
import com.example.miniprojet.entites.User;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobDetailsUser extends AppCompatActivity {

    TextView tvJobTitle, tvJobDate, tvJobDescription, tvJobLocation, tvCategory, tvExperience, tvSkills , tvIdJob;
    Button postBtn ;

    String key = "";
    Long idJob;
    Long maxid;
    DatabaseReference database;

    private FirebaseAuth authProfile;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details_user);


        tvJobDate = findViewById(R.id.detail_Date);
        tvJobDescription = findViewById(R.id.detail_Description);
        tvJobLocation = findViewById(R.id.detail_Location);
        tvCategory = findViewById(R.id.detail_Category);
        tvExperience = findViewById(R.id.detail_Experience);
        tvSkills = findViewById(R.id.detail_Skills);
        toolbar = findViewById(R.id.toolbar);
        postBtn = findViewById(R.id.postBtn);

        setSupportActionBar(toolbar);



        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            getSupportActionBar().setTitle("Details of "+bundle.getString("jobTitle"));
            tvJobDate.setText(bundle.getString("jobDate"));
            tvJobDescription.setText(bundle.getString("jobDescription"));
            tvJobLocation.setText(bundle.getString("jobLocation"));
            tvCategory.setText(bundle.getString("category"));
            tvExperience.setText(bundle.getString("experience"));
            tvSkills.setText(bundle.getString("skills"));
            key = bundle.getString("idValue");

        }
        database= FirebaseDatabase.getInstance().getReference("Post");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid = 0L;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Post post = dataSnapshot.getValue(Post.class);
                        if(post == null){
                            maxid=0L;
                            break;

                        }
                        else
                        {
                            if (post.getIdPost() > maxid)
                                maxid = post.getIdPost();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                if(user != null){
                    postBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Post post=new Post(maxid+1,user.getIdUser(),key);
                            DatabaseReference refrencePost=FirebaseDatabase.getInstance().getReference("Post");
                            refrencePost.child(String.valueOf(post.getIdPost())).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(JobDetailsUser.this, "Applied in this Job Succesfully !", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(JobDetailsUser.this,firstPageUser.class);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        Toast.makeText(JobDetailsUser.this, "Failed to Apply ", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}