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
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniprojet.entites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        Intent i=getIntent();

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.signupText);
        login=findViewById(R.id.loginBtn);

        authProfile=FirebaseAuth.getInstance();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(login_user.this);
                    builder.setCancelable(false);
                    builder.setView(R.layout.progress_layout);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    loginUser(dialog,email.getText().toString(),password.getText().toString());

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

    private void loginUser(AlertDialog dialog,String email, String password) {
        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(login_user.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId= authProfile.getCurrentUser().getUid();
                    DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("Users");
                    referenceProfile.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User u=snapshot.getValue(User.class);
                        if(u!=null){
                            if (u.getRole().equals("user")) {
                                if (u.getEnabled() == 0) {
                                    Intent i = new Intent(login_user.this, AddDetailsUser.class);
                                    startActivity(i);
                                    clearForm();


                                } else {
                                    Intent i = new Intent(login_user.this, firstPageUser.class);
                                    startActivity(i);
                                    clearForm();
                                }
                            }
                            else {
                                Intent i = new Intent(login_user.this, firestPageCompagny.class);
                                startActivity(i);
                                clearForm();

                            }
                        }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {
                    Toast.makeText(login_user.this,"Wrong password or email",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void clearForm() {
        email.setText("");
        password.setText("");
    }

}