package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity {

    TextView btnBackFilter;
    Button btnApplyFilter;

    RadioButton radioTennis, radioBasketball, radioSoccer, radioBadminton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);

        btnBackFilter = findViewById(R.id.btnBackFilter);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);

        radioTennis = findViewById(R.id.radioTennis);
        radioBasketball = findViewById(R.id.radioBasketball);
        radioSoccer = findViewById(R.id.radioSoccer);
        radioBadminton = findViewById(R.id.radioBadminton);

        btnBackFilter.setOnClickListener(v -> {
            finish();
        });

        btnApplyFilter.setOnClickListener(v -> {
            String selectedSport = "";

            if (radioTennis.isChecked()) {
                selectedSport = "Tennis";
            } else if (radioBasketball.isChecked()) {
                selectedSport = "BasketBall";
            } else if (radioSoccer.isChecked()) {
                selectedSport = "Soccer";
            } else if (radioBadminton.isChecked()) {
                selectedSport = "Badminton";
            }

            if (selectedSport.isEmpty()) {
                Toast.makeText(FilterActivity.this, "Please select a sport type", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(FilterActivity.this, HomeActivity.class);
            intent.putExtra("selectedSport", selectedSport);
            startActivity(intent);
        });
    }
}