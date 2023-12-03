package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miniprojet.entites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddDetailsUser extends AppCompatActivity {
    Button detail, uploadBtn;
    EditText experiences, specialization, skills, pdf, education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details_user);
        Intent i = getIntent();
        User user = (User) i.getSerializableExtra("user");

        detail = findViewById(R.id.addDet);
        experiences = findViewById(R.id.experiences);
        specialization = findViewById(R.id.specialization);
        skills = findViewById(R.id.skills);
        education = findViewById(R.id.education);

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
                    user.setExperiences(Integer.valueOf(experiences.getText().toString()));
                    user.setSpecialization(specialization.getText().toString());
                    user.setSkills(skills.getText().toString());
                    user.setEducation(education.getText().toString());
                    user.setEnabled(1);

                    updateData(user.getIdUser(),user.getFullName(), user.getExperiences(), user.getSpecialization(),
                            user.getSkills(), user.getEducation(), user.getEnabled());
                    Intent i = new Intent(AddDetailsUser.this, firstPageUser.class);
                    i.putExtra("user", user);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void updateData(Long id,String name, int exp, String spec, String skills, String education, int enabled) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("experiences", exp);
        userMap.put("specialization", spec);
        userMap.put("skills", skills);
        userMap.put("education", education);
        userMap.put("enabled", enabled);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(String.valueOf(id)).updateChildren(userMap)
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

