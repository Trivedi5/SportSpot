package com.example.sportspot;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddCourtActivity extends AppCompatActivity {

    TextView btnBackAddCourt;
    Button btnSaveCourt, btnChooseImage;
    EditText edtCourtName, edtSportType, edtPrice, edtLocation;
    CheckBox checkParkingAdd, checkLightsAdd, checkWashroomAdd;
    ImageView imgCourtPreview;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageRef;

    Uri selectedImageUri = null;

    ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_court);

        btnBackAddCourt = findViewById(R.id.btnBackAddCourt);
        btnSaveCourt = findViewById(R.id.btnSaveCourt);
        btnChooseImage = findViewById(R.id.btnChooseImage);

        edtCourtName = findViewById(R.id.edtCourtName);
        edtSportType = findViewById(R.id.edtSportType);
        edtPrice = findViewById(R.id.edtPrice);
        edtLocation = findViewById(R.id.edtLocation);

        checkParkingAdd = findViewById(R.id.checkParkingAdd);
        checkLightsAdd = findViewById(R.id.checkLightsAdd);
        checkWashroomAdd = findViewById(R.id.checkWashroomAdd);

        imgCourtPreview = findViewById(R.id.imgCourtPreview);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        imgCourtPreview.setImageURI(uri);
                    }
                }
        );

        btnBackAddCourt.setOnClickListener(v -> finish());

        btnChooseImage.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        btnSaveCourt.setOnClickListener(v -> {
            saveCourtWithImage();
        });
    }

    private void saveCourtWithImage() {
        String courtName = edtCourtName.getText().toString().trim();
        String sportType = edtSportType.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        if (courtName.isEmpty() || sportType.isEmpty() || priceText.isEmpty() || location.isEmpty()) {
            Toast.makeText(AddCourtActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(AddCourtActivity.this, "No logged in owner found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(AddCourtActivity.this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String ownerId = mAuth.getCurrentUser().getUid();
        double price = Double.parseDouble(priceText);

        String imageFileName = "court_images/" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(imageFileName);

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveCourtToFirestore(courtName, sportType, price, location, ownerId, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    String message = e.getMessage();

                    if (e instanceof com.google.firebase.storage.StorageException) {
                        com.google.firebase.storage.StorageException se =
                                (com.google.firebase.storage.StorageException) e;

                        int code = se.getErrorCode();
                        Toast.makeText(
                                AddCourtActivity.this,
                                "Upload failed. Code: " + code + "\n" + message,
                                Toast.LENGTH_LONG
                        ).show();
                    } else {
                        Toast.makeText(
                                AddCourtActivity.this,
                                "Image upload failed: " + message,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void saveCourtToFirestore(String courtName, String sportType, double price,
                                      String location, String ownerId, String imageUrl) {

        Map<String, Object> courtMap = new HashMap<>();
        courtMap.put("name", courtName);
        courtMap.put("sportType", sportType);
        courtMap.put("price", price);
        courtMap.put("location", location);
        courtMap.put("ownerId", ownerId);
        courtMap.put("rating", 4.5);
        courtMap.put("distance", 2.0);
        courtMap.put("imageUrl", imageUrl);
        courtMap.put("latitude", 45.5017);
        courtMap.put("longitude", -73.5673);
        courtMap.put("createdAt", System.currentTimeMillis());

        courtMap.put("parking", checkParkingAdd.isChecked());
        courtMap.put("lights", checkLightsAdd.isChecked());
        courtMap.put("washroom", checkWashroomAdd.isChecked());

        db.collection("courts")
                .add(courtMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddCourtActivity.this, "Court saved successfully", Toast.LENGTH_SHORT).show();

                    edtCourtName.setText("");
                    edtSportType.setText("");
                    edtPrice.setText("");
                    edtLocation.setText("");

                    checkParkingAdd.setChecked(false);
                    checkLightsAdd.setChecked(false);
                    checkWashroomAdd.setChecked(false);

                    selectedImageUri = null;
                    imgCourtPreview.setImageResource(R.drawable.spot);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddCourtActivity.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}