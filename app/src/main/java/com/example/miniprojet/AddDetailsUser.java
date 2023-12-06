package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miniprojet.entites.Pdf;
import com.example.miniprojet.entites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddDetailsUser extends AppCompatActivity {
    Button detail, uploadBtn;
    EditText experiences, specialization, skills, pdf, education;

    private FirebaseAuth authProfile;

    StorageReference storageReference;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details_user);
        Intent i = getIntent();
        //User user = (User) i.getSerializableExtra("user");

        detail = findViewById(R.id.addDet);
        experiences = findViewById(R.id.experiences);
        specialization = findViewById(R.id.specialization);
        skills = findViewById(R.id.skills);
        education = findViewById(R.id.education);
        pdf=findViewById(R.id.pdf);
        uploadBtn=findViewById(R.id.uploadButton);

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        String userId=firebaseUser.getUid();


        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference("Uploads");

       /* uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFiles();

            }
        });*/

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                if (experiences.getText().toString().isEmpty()) {
                    experiences.setError("Years of Experience Required");
                    valid = false;
                }
                if (specialization.getText().toString().isEmpty()) {
                    specialization.setError("Specialization Required");
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

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user=snapshot.getValue(User.class);
                            if(user != null){
                                user.setExperiences(Integer.valueOf(experiences.getText().toString()));
                                user.setSpecialization(specialization.getText().toString());
                                user.setSkills(skills.getText().toString());
                                user.setEducation(education.getText().toString());
                                user.setEnabled(1);

                                updateData(userId,user.getFullName(), user.getExperiences(), user.getSpecialization(),
                                        user.getSkills(), user.getEducation(), user.getEnabled());
                                Intent i = new Intent(AddDetailsUser.this, firstPageUser.class);
                                i.putExtra("user", user);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }
        });
    }

   /* private void selectFiles() {
        Intent i=new Intent();
        i.setType("appliation/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"select PDF Files..."),1);
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            UploadFiles(data.getData());
        }
    }*/

   /* private void UploadFiles(Uri data) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference=storageReference.child("Uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri url=uriTask.getResult();

                Pdf p=new Pdf(pdf)
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
    }*/

    private void updateData(String id,String name, int exp, String spec, String skills, String education, int enabled) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("experiences", exp);
        userMap.put("specialization", spec);
        userMap.put("skills", skills);
        userMap.put("education", education);
        userMap.put("enabled", enabled);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(id).updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddDetailsUser.this, "Details Added Successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AddDetailsUser.this, "Failed to add details", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddDetailsUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

