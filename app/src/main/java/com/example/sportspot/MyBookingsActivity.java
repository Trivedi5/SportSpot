package com.example.sportspot;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyBookingsActivity extends AppCompatActivity {

    LinearLayout bookingsContainer;
    TextView btnBackMyBookings;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_bookings);

        bookingsContainer = findViewById(R.id.bookingsContainer);
        btnBackMyBookings = findViewById(R.id.btnBackMyBookings);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnBackMyBookings.setOnClickListener(v -> {
            finish();
        });

        loadBookings();
    }

    private void loadBookings() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String customerId = mAuth.getCurrentUser().getUid();

        db.collection("bookings")
                .whereEqualTo("customerId", customerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    bookingsContainer.removeAllViews();

                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No bookings found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {

                        String courtName = doc.getString("courtName");
                        String date = doc.getString("date");
                        String time = doc.getString("time");
                        String status = doc.getString("status");

                        TextView bookingView = new TextView(this);
                        bookingView.setText(
                                "Court: " + courtName + "\n" +
                                        "Date: " + date + "\n" +
                                        "Time: " + time + "\n" +
                                        "Status: " + status
                        );

                        bookingView.setPadding(20, 20, 20, 20);
                        bookingView.setTextSize(16f);

                        bookingsContainer.addView(bookingView);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading bookings", Toast.LENGTH_SHORT).show();
                });
    }
}