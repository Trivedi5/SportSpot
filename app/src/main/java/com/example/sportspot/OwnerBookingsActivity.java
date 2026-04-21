package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerBookingsActivity extends AppCompatActivity {

    TextView btnBackOwnerBookings;
    Button btnGoAddCourt, btnOwnerLogout;
    LinearLayout ownerBookingsContainer;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_bookings);

        btnBackOwnerBookings = findViewById(R.id.btnBackOwnerBookings);
        btnGoAddCourt = findViewById(R.id.btnGoAddCourt);
        btnOwnerLogout = findViewById(R.id.btnOwnerLogout);
        ownerBookingsContainer = findViewById(R.id.ownerBookingsContainer);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnBackOwnerBookings.setOnClickListener(v -> finish());

        btnGoAddCourt.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerBookingsActivity.this, AddCourtActivity.class);
            startActivity(intent);
        });

        btnOwnerLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(OwnerBookingsActivity.this, RoleSelectionActivity.class);
            startActivity(intent);
            finish();
        });

        loadOwnerBookings();
    }

    private void loadOwnerBookings() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String ownerId = mAuth.getCurrentUser().getUid();

        db.collection("bookings")
                .whereEqualTo("ownerId", ownerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    ownerBookingsContainer.removeAllViews();

                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No owner bookings found", Toast.LENGTH_SHORT).show();
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

                        ownerBookingsContainer.addView(bookingView);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading owner bookings", Toast.LENGTH_SHORT).show();
                });
    }
}