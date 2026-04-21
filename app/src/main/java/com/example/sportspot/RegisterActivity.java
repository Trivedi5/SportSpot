package com.example.sportspot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView btnBackRegister;
    EditText edtRegisterName, edtRegisterEmail, edtRegisterPassword;
    RadioButton radioRegisterCustomer, radioRegisterOwner;
    Button btnRegister;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        btnBackRegister = findViewById(R.id.btnBackRegister);
        edtRegisterName = findViewById(R.id.edtRegisterName);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        radioRegisterCustomer = findViewById(R.id.radioRegisterCustomer);
        radioRegisterOwner = findViewById(R.id.radioRegisterOwner);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnBackRegister.setOnClickListener(v -> {
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String name = edtRegisterName.getText().toString().trim();
            String email = edtRegisterEmail.getText().toString().trim();
            String password = edtRegisterPassword.getText().toString().trim();
            String selectedRole;

            if (radioRegisterCustomer.isChecked()) {
                selectedRole = "customer";
            } else if (radioRegisterOwner.isChecked()) {
                selectedRole = "owner";
            } else {
                selectedRole = "";
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRole.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            final String finalRole = selectedRole;
            final String finalName = name;
            final String finalEmail = email;

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser() != null) {
                                String uid = mAuth.getCurrentUser().getUid();

                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("uid", uid);
                                userMap.put("name", finalName);
                                userMap.put("email", finalEmail);
                                userMap.put("role", finalRole);

                                db.collection("users").document(uid)
                                        .set(userMap)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(RegisterActivity.this, "Firestore Error", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(RegisterActivity.this, "User not found after registration", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (task.getException() != null) {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}