package com.example.sportspot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText edtSearch;
    Button btnFilter, btnSort;
    LinearLayout cardCourt1, cardCourt2, cardCourt3, cardCourt4, cardCourt5;
    TextView navFavorites, navBookings, navProfile;

    ImageView imgCourt1, imgCourt2, imgCourt3, imgCourt4, imgCourt5;

    TextView txtCourt1Name, txtCourt1Rating, txtCourt1Price;
    TextView txtCourt2Name, txtCourt2Rating, txtCourt2Price;
    TextView txtCourt3Name, txtCourt3Rating, txtCourt3Price;
    TextView txtCourt4Name, txtCourt4Rating, txtCourt4Price;
    TextView txtCourt5Name, txtCourt5Rating, txtCourt5Price;

    FirebaseFirestore db;
    GoogleMap mMap;

    boolean isSortedByPrice = false;
    String selectedSport = "";

    ArrayList<DocumentSnapshot> currentCourtDocs = new ArrayList<>();
    ArrayList<DocumentSnapshot> allCourtDocs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        edtSearch = findViewById(R.id.edtSearch);
        btnFilter = findViewById(R.id.btnFilter);
        btnSort = findViewById(R.id.btnSort);

        cardCourt1 = findViewById(R.id.cardCourt1);
        cardCourt2 = findViewById(R.id.cardCourt2);
        cardCourt3 = findViewById(R.id.cardCourt3);
        cardCourt4 = findViewById(R.id.cardCourt4);
        cardCourt5 = findViewById(R.id.cardCourt5);

        navFavorites = findViewById(R.id.navFavorites);
        navBookings = findViewById(R.id.navBookings);
        navProfile = findViewById(R.id.navProfile);

        imgCourt1 = findViewById(R.id.imgCourt1);
        imgCourt2 = findViewById(R.id.imgCourt2);
        imgCourt3 = findViewById(R.id.imgCourt3);
        imgCourt4 = findViewById(R.id.imgCourt4);
        imgCourt5 = findViewById(R.id.imgCourt5);

        txtCourt1Name = findViewById(R.id.txtCourt1Name);
        txtCourt1Rating = findViewById(R.id.txtCourt1Rating);
        txtCourt1Price = findViewById(R.id.txtCourt1Price);

        txtCourt2Name = findViewById(R.id.txtCourt2Name);
        txtCourt2Rating = findViewById(R.id.txtCourt2Rating);
        txtCourt2Price = findViewById(R.id.txtCourt2Price);

        txtCourt3Name = findViewById(R.id.txtCourt3Name);
        txtCourt3Rating = findViewById(R.id.txtCourt3Rating);
        txtCourt3Price = findViewById(R.id.txtCourt3Price);

        txtCourt4Name = findViewById(R.id.txtCourt4Name);
        txtCourt4Rating = findViewById(R.id.txtCourt4Rating);
        txtCourt4Price = findViewById(R.id.txtCourt4Price);

        txtCourt5Name = findViewById(R.id.txtCourt5Name);
        txtCourt5Rating = findViewById(R.id.txtCourt5Rating);
        txtCourt5Price = findViewById(R.id.txtCourt5Price);

        db = FirebaseFirestore.getInstance();

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.homeMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (getIntent().hasExtra("selectedSport")) {
            selectedSport = getIntent().getStringExtra("selectedSport");
            loadFilteredCourts(selectedSport);
        } else {
            loadCourtsDefault();
        }

        btnFilter.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, FilterActivity.class));
        });

        btnSort.setOnClickListener(v -> {
            if (!isSortedByPrice) {
                loadCourtsSortedByPrice();
                btnSort.setText("Sorted by Price");
                isSortedByPrice = true;
            } else {
                if (!selectedSport.isEmpty()) {
                    loadFilteredCourts(selectedSport);
                } else {
                    loadCourtsDefault();
                }
                btnSort.setText("Sort");
                isSortedByPrice = false;
            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = edtSearch.getText().toString().trim();
            searchCourts(searchText);
            return false;
        });

        cardCourt1.setOnClickListener(v -> openCourtDetails(0));
        cardCourt2.setOnClickListener(v -> openCourtDetails(1));
        cardCourt3.setOnClickListener(v -> openCourtDetails(2));
        cardCourt4.setOnClickListener(v -> openCourtDetails(3));
        cardCourt5.setOnClickListener(v -> openCourtDetails(4));

        navFavorites.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, FavoritesActivity.class)));
        navBookings.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MyBookingsActivity.class)));
        navProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng montreal = new LatLng(45.5017, -73.5673);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(montreal, 11f));
        loadMapMarkers();
    }

    private void loadMapMarkers() {
        if (mMap == null) return;

        db.collection("courts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mMap.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String name = doc.getString("name");
                        Double latitude = doc.getDouble("latitude");
                        Double longitude = doc.getDouble("longitude");

                        if (name != null && latitude != null && longitude != null) {
                            LatLng courtLocation = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(courtLocation).title(name));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Error loading map markers", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadCourtsDefault() {
        db.collection("courts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    allCourtDocs = new ArrayList<>(docs);
                    currentCourtDocs = new ArrayList<>(docs);
                    clearAllCards();
                    hideAllCards();
                    fillCards(docs);
                    loadMapMarkers();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Error loading courts", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadCourtsSortedByPrice() {
        db.collection("courts")
                .orderBy("price")
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    allCourtDocs = new ArrayList<>(docs);
                    currentCourtDocs = new ArrayList<>(docs);
                    clearAllCards();
                    hideAllCards();
                    fillCards(docs);
                    loadMapMarkers();
                    Toast.makeText(HomeActivity.this, "Sorted by price", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Error sorting courts", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadFilteredCourts(String sportType) {
        db.collection("courts")
                .whereEqualTo("sportType", sportType)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    allCourtDocs = new ArrayList<>(docs);
                    currentCourtDocs = new ArrayList<>(docs);
                    clearAllCards();
                    hideAllCards();
                    fillCards(docs);

                    if (docs.isEmpty()) {
                        Toast.makeText(HomeActivity.this, "No courts found for " + sportType, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Filtered: " + sportType, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Error filtering courts", Toast.LENGTH_SHORT).show();
                });
    }

    private void searchCourts(String searchText) {
        if (searchText.isEmpty()) {
            clearAllCards();
            hideAllCards();
            fillCards(allCourtDocs);
            currentCourtDocs = new ArrayList<>(allCourtDocs);
            return;
        }

        ArrayList<DocumentSnapshot> filteredList = new ArrayList<>();

        for (DocumentSnapshot doc : allCourtDocs) {
            String name = doc.getString("name");
            if (name != null && name.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(doc);
            }
        }

        currentCourtDocs = new ArrayList<>(filteredList);
        clearAllCards();
        hideAllCards();
        fillCards(filteredList);

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No matching courts found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillCards(List<DocumentSnapshot> docs) {
        if (docs.size() > 0) {
            cardCourt1.setVisibility(View.VISIBLE);
            setCourtData(docs.get(0), imgCourt1, txtCourt1Name, txtCourt1Rating, txtCourt1Price);
        }
        if (docs.size() > 1) {
            cardCourt2.setVisibility(View.VISIBLE);
            setCourtData(docs.get(1), imgCourt2, txtCourt2Name, txtCourt2Rating, txtCourt2Price);
        }
        if (docs.size() > 2) {
            cardCourt3.setVisibility(View.VISIBLE);
            setCourtData(docs.get(2), imgCourt3, txtCourt3Name, txtCourt3Rating, txtCourt3Price);
        }
        if (docs.size() > 3) {
            cardCourt4.setVisibility(View.VISIBLE);
            setCourtData(docs.get(3), imgCourt4, txtCourt4Name, txtCourt4Rating, txtCourt4Price);
        }
        if (docs.size() > 4) {
            cardCourt5.setVisibility(View.VISIBLE);
            setCourtData(docs.get(4), imgCourt5, txtCourt5Name, txtCourt5Rating, txtCourt5Price);
        }
    }

    private void setCourtData(DocumentSnapshot doc, ImageView imageView, TextView nameView, TextView ratingView, TextView priceView) {
        String name = doc.getString("name");
        Double rating = doc.getDouble("rating");
        Double distance = doc.getDouble("distance");
        Double price = doc.getDouble("price");
        String imageUrl = doc.getString("imageUrl");

        if (name != null) {
            nameView.setText(name);
        }

        if (rating != null && distance != null) {
            ratingView.setText("⭐ " + rating + "   📍 " + distance + " km");
        }

        if (price != null) {
            priceView.setText("$" + price + "/hr");
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.spot)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.spot);
        }
    }

    private void clearAllCards() {
        txtCourt1Name.setText("");
        txtCourt1Rating.setText("");
        txtCourt1Price.setText("");

        txtCourt2Name.setText("");
        txtCourt2Rating.setText("");
        txtCourt2Price.setText("");

        txtCourt3Name.setText("");
        txtCourt3Rating.setText("");
        txtCourt3Price.setText("");

        txtCourt4Name.setText("");
        txtCourt4Rating.setText("");
        txtCourt4Price.setText("");

        txtCourt5Name.setText("");
        txtCourt5Rating.setText("");
        txtCourt5Price.setText("");
    }

    private void hideAllCards() {
        cardCourt1.setVisibility(View.GONE);
        cardCourt2.setVisibility(View.GONE);
        cardCourt3.setVisibility(View.GONE);
        cardCourt4.setVisibility(View.GONE);
        cardCourt5.setVisibility(View.GONE);
    }

    private void openCourtDetails(int index) {
        if (index >= currentCourtDocs.size()) {
            Toast.makeText(this, "Court data not available", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentSnapshot doc = currentCourtDocs.get(index);

        String name = doc.getString("name");
        String sportType = doc.getString("sportType");
        String location = doc.getString("location");
        String ownerId = doc.getString("ownerId");
        String imageUrl = doc.getString("imageUrl");
        Double rating = doc.getDouble("rating");
        Double distance = doc.getDouble("distance");
        Double price = doc.getDouble("price");

        Intent intent = new Intent(HomeActivity.this, CourtDetailsActivity.class);
        intent.putExtra("courtName", name);
        intent.putExtra("sportType", sportType);
        intent.putExtra("location", location);
        intent.putExtra("ownerId", ownerId);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("rating", rating != null ? rating : 0.0);
        intent.putExtra("distance", distance != null ? distance : 0.0);
        intent.putExtra("price", price != null ? price : 0.0);
        startActivity(intent);
    }
}