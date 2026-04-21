package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class CourtDetailsActivity extends AppCompatActivity {

    TextView btnBackCourtDetails;
    Button btnBookNow;

    ImageView imgCourtDetails;
    TextView txtCourtDetailsName, txtCourtDetailsType, txtCourtDetailsInfo, txtAboutDescription;

    String courtName, sportType, location, ownerId, imageUrl;
    double rating, distance, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_court_details);

        btnBackCourtDetails = findViewById(R.id.btnBackCourtDetails);
        btnBookNow = findViewById(R.id.btnBookNow);

        imgCourtDetails = findViewById(R.id.imgCourtDetails);
        txtCourtDetailsName = findViewById(R.id.txtCourtDetailsName);
        txtCourtDetailsType = findViewById(R.id.txtCourtDetailsType);
        txtCourtDetailsInfo = findViewById(R.id.txtCourtDetailsInfo);
        txtAboutDescription = findViewById(R.id.txtAboutDescription);

        Intent intent = getIntent();
        courtName = intent.getStringExtra("courtName");
        sportType = intent.getStringExtra("sportType");
        location = intent.getStringExtra("location");
        ownerId = intent.getStringExtra("ownerId");
        imageUrl = intent.getStringExtra("imageUrl");
        rating = intent.getDoubleExtra("rating", 0.0);
        distance = intent.getDoubleExtra("distance", 0.0);
        price = intent.getDoubleExtra("price", 0.0);

        if (courtName != null) {
            txtCourtDetailsName.setText(courtName);
        }

        if (sportType != null) {
            txtCourtDetailsType.setText(sportType + " Court");
        }

        txtCourtDetailsInfo.setText("⭐ " + rating + "   📍 " + distance + " km   $" + price + "/hr");

        if (location != null) {
            txtAboutDescription.setText("Court location: " + location + ". Good facilities and booking availability for players.");
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.spot)
                    .into(imgCourtDetails);
        } else {
            imgCourtDetails.setImageResource(R.drawable.spot);
        }

        btnBackCourtDetails.setOnClickListener(v -> {
            finish();
        });

        btnBookNow.setOnClickListener(v -> {
            Intent bookingIntent = new Intent(CourtDetailsActivity.this, BookingSummaryActivity.class);
            bookingIntent.putExtra("courtName", courtName);
            bookingIntent.putExtra("sportType", sportType);
            bookingIntent.putExtra("location", location);
            bookingIntent.putExtra("ownerId", ownerId);
            bookingIntent.putExtra("imageUrl", imageUrl);
            bookingIntent.putExtra("rating", rating);
            bookingIntent.putExtra("distance", distance);
            bookingIntent.putExtra("price", price);
            startActivity(bookingIntent);
        });
    }
}