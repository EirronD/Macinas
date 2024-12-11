package com.example.bahi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Condo extends AppCompatActivity {

    private RecyclerView condoRecyclerView;
    private CondoAdapter condoAdapter;
    private List<Listings> condoList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condo);

        condoRecyclerView = findViewById(R.id.condo_RecyclerView);
        condoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String accountType = getIntent().getStringExtra("accountType");
        condoList = new ArrayList<>();
        condoAdapter = new CondoAdapter(condoList, accountType);
        condoRecyclerView.setAdapter(condoAdapter);

        db = FirebaseFirestore.getInstance();


        fetchCondosFromFirestore();
    }

    private void fetchCondosFromFirestore() {
        db.collection("listings")
                .whereEqualTo("unitType", "Condo")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        condoList.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String id = document.getId();
                            String description = document.getString("description");
                            Object priceObject = document.get("price");
                            String price = priceObject != null ? String.valueOf(priceObject) : "N/A";
                            String contactNumber = document.getString("contactNumber");
                            String location = document.getString("location");
                            String imageUrl = document.getString("imageUrl");

                            Listings condo = new Listings(id, description, price, contactNumber, location, imageUrl);
                            condoList.add(condo);
                        }
                        condoAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Condo.this, "Error getting condos: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
