package com.example.bahi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Apartments extends AppCompatActivity {

    private RecyclerView apartmentRecyclerView;
    private ApartmentAdapter apartmentAdapter;
    private java.util.List<Listings> apartmentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartments);
        String accountType = getIntent().getStringExtra("accountType");

        apartmentRecyclerView = findViewById(R.id.apartment_RecyclerView);
        apartmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        apartmentList = new ArrayList<>();
        apartmentAdapter = new ApartmentAdapter(apartmentList, accountType);
        apartmentRecyclerView.setAdapter(apartmentAdapter);

        db = FirebaseFirestore.getInstance();

        fetchApartmentsFromFirestore();
    }

    private void fetchApartmentsFromFirestore() {
        db.collection("listings")
                .whereEqualTo("unitType", "Apartment")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        apartmentList.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String id = document.getId();
                            String description = document.getString("description");
                            String price = document.getString("price");
                            String contactNumber = document.getString("contactNumber");
                            String location = document.getString("location");
                            String imageUrl = document.getString("imageUrl");

                            Listings apartment = new Listings(id, description, price, contactNumber, location, imageUrl);
                            apartmentList.add(apartment);
                        }
                        apartmentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Apartments.this, "Error getting apartments: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
