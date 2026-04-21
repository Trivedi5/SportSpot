package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FavoritesActivity extends AppCompatActivity {

    TextView btnBackFavorites;
    LinearLayout cardFavorite1, cardFavorite2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        btnBackFavorites = findViewById(R.id.btnBackFavorites);
        cardFavorite1 = findViewById(R.id.cardFavorite1);
        cardFavorite2 = findViewById(R.id.cardFavorite2);

        btnBackFavorites.setOnClickListener(v -> finish());

        cardFavorite1.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, CourtDetailsActivity.class);
            startActivity(intent);
        });

        cardFavorite2.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritesActivity.this, CourtDetailsActivity.class);
            startActivity(intent);
        });
    }
}