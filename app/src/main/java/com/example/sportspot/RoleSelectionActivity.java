package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    RadioButton radioOwner;
    RadioButton radioCustomer;
    Button btnSignInRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        radioOwner = findViewById(R.id.radioOwner);
        radioCustomer = findViewById(R.id.radioCustomer);
        btnSignInRole = findViewById(R.id.btnSignInRole);

        btnSignInRole.setOnClickListener(v -> {
            Intent intent;

            if (radioOwner.isChecked()) {
                intent = new Intent(RoleSelectionActivity.this, OwnerLoginActivity.class);
                startActivity(intent);
            } else if (radioCustomer.isChecked()) {
                intent = new Intent(RoleSelectionActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(RoleSelectionActivity.this, "Please select a role", Toast.LENGTH_SHORT).show();
            }
        });
    }
}