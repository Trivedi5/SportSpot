package com.example.sportspot;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class OwnerLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_login);
        TextView btnBackOwner = findViewById(R.id.btnBackOwner);

        btnBackOwner.setOnClickListener(v -> {
            finish(); // goes back
        });
    }
}