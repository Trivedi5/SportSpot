package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BookingConfirmationActivity extends AppCompatActivity {

    Button btnViewBooking;
    Button btnBackHome;

    TextView txtConfirmCourtName, txtConfirmLocation, txtConfirmTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_confirmation);

        btnViewBooking = findViewById(R.id.btnViewBooking);
        btnBackHome = findViewById(R.id.btnBackHome);

        txtConfirmCourtName = findViewById(R.id.txtConfirmCourtName);
        txtConfirmLocation = findViewById(R.id.txtConfirmLocation);
        txtConfirmTotal = findViewById(R.id.txtConfirmTotal);

        Intent intent = getIntent();
        String courtName = intent.getStringExtra("courtName");
        String location = intent.getStringExtra("location");
        double total = intent.getDoubleExtra("total", 0.0);

        if (courtName != null) {
            txtConfirmCourtName.setText(courtName);
        }

        if (location != null) {
            txtConfirmLocation.setText(location);
        }

        txtConfirmTotal.setText("Total Paid: $" + total);

        btnViewBooking.setOnClickListener(v -> {
            Intent bookingsIntent = new Intent(BookingConfirmationActivity.this, MyBookingsActivity.class);
            startActivity(bookingsIntent);
        });

        btnBackHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(BookingConfirmationActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        });
    }
}