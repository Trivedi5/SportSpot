package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class OwnerLoginActivity extends AppCompatActivity {

    TextView btnBackOwner, txtOwnerRegister;
    Button btnOwnerLogin;
    EditText edtOwnerEmail, edtOwnerPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_login);

        btnBackOwner = findViewById(R.id.btnBackOwner);
        txtOwnerRegister = findViewById(R.id.txtOwnerRegister);
        btnOwnerLogin = findViewById(R.id.btnOwnerLogin);
        edtOwnerEmail = findViewById(R.id.edtOwnerEmail);
        edtOwnerPassword = findViewById(R.id.edtOwnerPassword);

        mAuth = FirebaseAuth.getInstance();

        btnBackOwner.setOnClickListener(v -> {
            finish();
        });

        txtOwnerRegister.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerLoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnOwnerLogin.setOnClickListener(v -> {
            String email = edtOwnerEmail.getText().toString().trim();
            String password = edtOwnerPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(OwnerLoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(OwnerLoginActivity.this, "Owner login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OwnerLoginActivity.this, OwnerBookingsActivity.class);
                            startActivity(intent);
                        } else {
                            if (task.getException() != null) {
                                Toast.makeText(OwnerLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(OwnerLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}