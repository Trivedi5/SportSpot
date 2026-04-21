package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BookingSummaryActivity extends AppCompatActivity {

    TextView btnBackBooking;
    Button btnProceedPayment;

    TextView txtBookingCourtName, txtBookingCourtInfo, txtBookingDate, txtBookingTime, txtPriceCourt, txtPriceTax, txtPriceTotal;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    String courtName, ownerId, location;
    double rating, distance, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_summary);

        btnBackBooking = findViewById(R.id.btnBackBooking);
        btnProceedPayment = findViewById(R.id.btnProceedPayment);

        txtBookingCourtName = findViewById(R.id.txtBookingCourtName);
        txtBookingCourtInfo = findViewById(R.id.txtBookingCourtInfo);
        txtBookingDate = findViewById(R.id.txtBookingDate);
        txtBookingTime = findViewById(R.id.txtBookingTime);
        txtPriceCourt = findViewById(R.id.txtPriceCourt);
        txtPriceTax = findViewById(R.id.txtPriceTax);
        txtPriceTotal = findViewById(R.id.txtPriceTotal);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        courtName = intent.getStringExtra("courtName");
        ownerId = intent.getStringExtra("ownerId");
        location = intent.getStringExtra("location");
        rating = intent.getDoubleExtra("rating", 0.0);
        distance = intent.getDoubleExtra("distance", 0.0);
        price = intent.getDoubleExtra("price", 0.0);

        double tax = Math.round(price * 0.25 * 100.0) / 100.0;
        double total = price + tax;

        if (courtName != null) {
            txtBookingCourtName.setText(courtName);
        }

        txtBookingCourtInfo.setText("⭐ " + rating + "   📍 " + distance + " km");
        txtBookingDate.setText("Date: 03/19/2026");
        txtBookingTime.setText("Time: 09:00 AM");
        txtPriceCourt.setText("Court Price: $" + price);
        txtPriceTax.setText("Tax: $" + tax);
        txtPriceTotal.setText("Total: $" + total);

        btnBackBooking.setOnClickListener(v -> {
            finish();
        });

        btnProceedPayment.setOnClickListener(v -> {
            saveBookingToFirestore(total);
        });
    }

    private void saveBookingToFirestore(double total) {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String customerId = mAuth.getCurrentUser().getUid();

        Map<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("courtName", courtName);
        bookingMap.put("customerId", customerId);
        bookingMap.put("ownerId", ownerId != null ? ownerId : "");
        bookingMap.put("date", "03/19/2026");
        bookingMap.put("time", "09:00 AM");
        bookingMap.put("status", "pending");
        bookingMap.put("total", total);

        db.collection("bookings")
                .add(bookingMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(BookingSummaryActivity.this, "Booking saved", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BookingSummaryActivity.this, BookingConfirmationActivity.class);
                    intent.putExtra("courtName", courtName);
                    intent.putExtra("location", location);
                    intent.putExtra("total", total);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookingSummaryActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}