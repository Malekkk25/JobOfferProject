package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {

    EditText old, password, newEmail;
    Button auth, update;
    TextView text;
    private String userOldEmail, userNewEmail, userPassword;

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private AlertDialog dialog; // Declare AlertDialog as a class variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        old = findViewById(R.id.currentEmail);
        newEmail = findViewById(R.id.newEmail);
        password = findViewById(R.id.password);
        text = findViewById(R.id.text);
        auth = findViewById(R.id.auth);
        update = findViewById(R.id.update);

        update.setEnabled(false);
        newEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(UpdateEmail.this, "Something went wrong, User's details not available", Toast.LENGTH_SHORT).show();
        } else {
            userOldEmail = firebaseUser.getEmail();
            old.setText(userOldEmail);
            reAuthenticate();
        }
    }

    private void reAuthenticate() {
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassword = password.getText().toString();

                if (userPassword.isEmpty()) {
                    password.setError("Please enter your password for authentication");
                    password.requestFocus();
                } else {
                    showProgressDialog();
                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, userPassword);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dismissProgressDialog();
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(UpdateEmail.this, "Password has been verified. You can update email now.", Toast.LENGTH_LONG).show();
                                text.setText("You are authenticated. You can update your email now.");

                                newEmail.setEnabled(true);
                                password.setEnabled(false);
                                auth.setEnabled(false);
                                update.setEnabled(true);

                                update.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmail.this, R.color.green));

                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = newEmail.getText().toString();
                                        boolean valid = true;
                                        if (userNewEmail.isEmpty()) {
                                            newEmail.setError("New Email is required");
                                            valid = false;
                                            newEmail.requestFocus();
                                        }
                                        if (!userNewEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                                            newEmail.setError("Invalid Email Format");
                                            valid = false;
                                            newEmail.requestFocus();
                                        }
                                        if (userOldEmail.equals(userNewEmail)) {
                                            newEmail.setError("Please Enter new Email");
                                            valid = false;
                                            newEmail.requestFocus();
                                        }
                                        if (valid) {
                                            showProgressDialog();
                                            updateEm();
                                        }
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateEm() {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissProgressDialog(); // Dismiss the existing dialog
                if (task.isSuccessful()) {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(UpdateEmail.this, "Email has been updated. Please verify your new Email", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdateEmail.this, ProfileUser.class);
                    startActivity(i);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showProgressDialog() {
        if (dialog == null || !dialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEmail.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            dialog = builder.create();
            dialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
