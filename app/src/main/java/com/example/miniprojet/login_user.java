package com.example.miniprojet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniprojet.entites.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class login_user extends AppCompatActivity {

    EditText email,password;
    TextView register;
    Button login;

    ArrayList<User> listUsers=new ArrayList<>();
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        Intent i=getIntent();

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.signupText);
        login=findViewById(R.id.loginBtn);


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
                Toast.makeText(login_user.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u=null;
                boolean userFound=false;
                boolean valid=true;
                if(email.getText().toString().isEmpty()){
                    email.setError("Required Email");
                    valid=false;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("Required Password");
                    valid=false;
                }
                if(valid == true){
                for (User user : listUsers) {
                    if (email.getText().toString().equals(user.getEmail())){
                        u=user;
                        userFound=true;
                        break;
                    }
                }
                if(userFound ==true){
                    if(password.getText().toString().equals(u.getPassword())) {
                        if (u.getRole().equals("user")) {
                            if (u.getEnabled() == 0) {
                                Intent i = new Intent(login_user.this, AddDetailsUser.class);
                                i.putExtra("user", u);
                                startActivity(i);
                                clearForm();


                            } else {
                                Intent i = new Intent(login_user.this, firstPageUser.class);
                                i.putExtra("user", u);
                                startActivity(i);
                                clearForm();
                            }
                        }
                        else {
                            Intent i = new Intent(login_user.this, firestPageCompagny.class);
                            i.putExtra("user",u);
                            startActivity(i);
                            clearForm();

                        }
                    }
                    else {
                        password.setError("Wrong Password");
                    }
                }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(login_user.this, signup_user.class);
                startActivity(i);
            }
        });

    }
    private void clearForm() {
        email.setText("");
        password.setText("");
    }

}