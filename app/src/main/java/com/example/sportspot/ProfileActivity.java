package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView btnBackProfile;
    LinearLayout optionMyBookings, optionFavorites, optionLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        btnBackProfile = findViewById(R.id.btnBackProfile);
        optionMyBookings = findViewById(R.id.optionMyBookings);
        optionFavorites = findViewById(R.id.optionFavorites);
        optionLogout = findViewById(R.id.optionLogout);

        btnBackProfile.setOnClickListener(v -> finish());

        optionMyBookings.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyBookingsActivity.class);
            startActivity(intent);
        });

        optionFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        optionLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, RoleSelectionActivity.class);
            startActivity(intent);
        });
    }
}