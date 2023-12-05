package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.miniprojet.entites.Job;
import com.example.miniprojet.entites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import android.net.Uri;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class signup_user extends AppCompatActivity {

    EditText name,email,contact,password,passConf,summary;
    Button signupbtn;
    RadioButton user,compagny;
    ArrayList<User> listUsers=new ArrayList<>();
    DatabaseReference database;
    Long maxid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_user);
        Intent i=getIntent();

        name=findViewById(R.id.firstName);
        email=findViewById(R.id.email);
        contact=findViewById(R.id.contact);
        password=findViewById(R.id.password);
        passConf=findViewById(R.id.passConf);
        summary=findViewById(R.id.summary);
        user=findViewById(R.id.user);
        compagny=findViewById(R.id.compagny);
        signupbtn=findViewById(R.id.signUser);


        database= FirebaseDatabase.getInstance().getReference("Users");

        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u=snapshot.getValue(User.class);
                if (u!= null){
                    listUsers.add(u);
                }
                System.out.println(u);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(signup_user.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        boolean valid=true;
        boolean userFound=false;
        if(name.getText().toString().isEmpty() ){
            name.setError("Required Name");
            valid=false;
        }
        if(email.getText().toString().isEmpty()){
            email.setError("Required Email");
            valid=false;
        }
        if(!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            email.setError("Invalid Email Format");
            valid=false;
        }
        if(contact.getText().toString().isEmpty()){
            contact.setError("Required Contact");
            valid=false;
        }
        if(contact.getText().toString().length()<8 ||contact.getText().toString().length()>8  ){
            contact.setError("At least 8 numbers");
            valid=false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Required Password");
            valid=false;
        }
        if(password.getText().toString().length()<6){
            password.setError("Password must be at least 6 Characters");
            valid=false;
        }
        if(!password.getText().toString().equals(passConf.getText().toString())){
            passConf.setError("Wrong Password");
            valid=false;
        }
        if(summary.getText().toString().isEmpty()){
            summary.setError("Required Summary");
            valid=false;
        }
        if (!user.isChecked() && !compagny.isChecked()) {
            compagny.setError("Required Role");
            valid = false;
        } else {
            compagny.setError(null);
        }
        for (User user : listUsers) {
            if (email.getText().toString().equals(user.getEmail())) {
                userFound = true;
                break;
            }
        }
        if(userFound == true){
            email.setError("User Already Exists");
            valid=false;
        }
        if(valid==true){
            saveUser();
        }

    }
        });

    }

    private void saveUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(signup_user.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        uploadData(dialog);
    }

    public void uploadData(AlertDialog dialog){
        String full = name.getText().toString();
        String Email = email.getText().toString();
        String Contact=contact.getText().toString();
        String Password=password.getText().toString();
        String Summary=summary.getText().toString();
        String role;
        if (user.isChecked()) role="user";
        else role="compagny";
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(signup_user.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser=auth.getCurrentUser();

                    User user = new User(firebaseUser.getUid(),full, Email, Contact,Password,role,Summary);
                    DatabaseReference refrenceProfile=FirebaseDatabase.getInstance().getReference("Users");
                    refrenceProfile.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(signup_user.this, full+"   Registered Successfully,Please verify your email!", Toast.LENGTH_SHORT).show();
                                firebaseUser.sendEmailVerification();
                                Intent i=new Intent(signup_user.this,login_user.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(signup_user.this, "User registered failed,Please Try again ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



                }
            }
        });}
}