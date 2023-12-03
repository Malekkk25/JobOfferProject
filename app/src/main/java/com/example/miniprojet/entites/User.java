package com.example.miniprojet.entites;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {


    @Exclude
    public long idUser;
    int enabled,experiences;

    String fullName,email,contact,password,role,summary,specialization,skills,education;




    public User(String fullName, String email, String contact, String password, String role, String summary) {
        this.enabled = 0;
        this.fullName = fullName;
        this.email = email;
        this.contact = contact;
        this.password = password;
        this.role = role;
        this.summary = summary;
    }
    public User() {

    }
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
    public int getExperiences() {
        return experiences;
    }

    public void setExperiences(int experiences) {
        this.experiences = experiences;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", enabled=" + enabled +
                ", experiences=" + experiences +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", summary='" + summary + '\'' +
                ", specialization='" + specialization + '\'' +
                ", skills='" + skills + '\'' +
                ", education='" + education + '\'' +
                '}';
    }

    /*@Exclude
    public String generateUserId() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").push();
        idUser = ref.getKey();
        return idUser;
    }*/
    @Exclude
    public long generateUserId() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Get the current user count from the database
        usersRef.child("userCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long count = (long) dataSnapshot.getValue();

                    // Increment the count
                    count++;

                    // Set the new count back to the database
                    usersRef.child("userCount").setValue(count);

                    // Generate the user ID using the incremented count
                    idUser = count;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

        return idUser; // Return the generated user ID
    }

}
