package com.example.miniprojet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miniprojet.entites.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class compagnyProfile extends Fragment {

    EditText name, email, contact, pass, confPass,summary;
    Button updateProfile;
    ArrayList<User> listUsers=new ArrayList<>();
    DatabaseReference database;

    Long id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_compagny_profile, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        contact = view.findViewById(R.id.contact);
        pass = view.findViewById(R.id.pass);
        confPass = view.findViewById(R.id.confPass);
        summary=view.findViewById(R.id.summary);
        updateProfile = view.findViewById(R.id.updateProfile);

        database= FirebaseDatabase.getInstance().getReference("Users");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User u=snapshot.getValue(User.class);
                if(u!=null){
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

            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            User user = (User) bundle.getSerializable("user");
            if (user != null) {
                name.setText(user.getFullName());
                email.setText(user.getEmail());
                contact.setText(user.getContact());
                summary.setText(user.getSummary());
                id=user.getIdUser();
            }
        }

        updateProfile.setOnClickListener(new View.OnClickListener() {
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
                if(!pass.getText().toString().isEmpty()){
                    if(!pass.getText().toString().equals(confPass.getText().toString())){
                        confPass.setError("Wrong Password");
                        valid=false;
                    }}
                if(summary.getText().toString().isEmpty()){
                    summary.setError("Required Summary");
                    valid=false;
                }
                if(valid ==true){
                    updateData(id,name.getText().toString(),email.getText().toString(),contact.getText().toString(),pass.getText().toString(),summary.getText().toString());
                }
            }
        });

        return view;
    }

    private void updateData(Long id,String name, String email, String contact, String password, String summary) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("fullName", name);
        userMap.put("email", email);
        userMap.put("contact", contact);
        userMap.put("password", password);
        userMap.put("summary", summary);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(String.valueOf(id)).updateChildren(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to update details", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }
