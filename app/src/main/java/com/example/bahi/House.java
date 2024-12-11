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
import java.util.List;

public class House extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView houseRecyclerView;
    private HouseAdapter houseAdapter;
    private List<Listings> houseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        db = FirebaseFirestore.getInstance();
        String accountType = getIntent().getStringExtra("accountType");

        houseRecyclerView = findViewById(R.id.house_RecyclerView);
        houseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        houseList = new ArrayList<>();
        houseAdapter = new HouseAdapter(houseList, accountType);
        houseRecyclerView.setAdapter(houseAdapter);

        fetchHousesFromFirestore();
    }

    private void fetchHousesFromFirestore() {
        db.collection("listings")
                .whereEqualTo("unitType", "House")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        houseList.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String id = document.getId();
                            String description = document.getString("description");
                            Object priceObject = document.get("price");
                            String price = priceObject != null ? String.valueOf(priceObject) : "N/A";
                            String contactNumber = document.getString("contactNumber");
                            String location = document.getString("location");
                            String imageUrl = document.getString("imageUrl");

                            Listings house = new Listings(id, description, price, contactNumber, location, imageUrl);
                            houseList.add(house);
                        }
                        houseAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(House.this, "Error getting houses: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
