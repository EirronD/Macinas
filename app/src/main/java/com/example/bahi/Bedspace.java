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

public class Bedspace extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView bedspaceRecyclerView;
    private BedspaceAdapter bedspaceAdapter;
    private List<Listings> bedspaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedspace);

        db = FirebaseFirestore.getInstance();
        String accountType = getIntent().getStringExtra("accountType");

        bedspaceRecyclerView = findViewById(R.id.bedspace_RecyclerView);
        bedspaceRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bedspaceList = new ArrayList<>();
        bedspaceAdapter = new BedspaceAdapter(bedspaceList, accountType);
        bedspaceRecyclerView.setAdapter(bedspaceAdapter);

        fetchBedspacesFromFirestore();
    }

    private void fetchBedspacesFromFirestore() {
        db.collection("listings")
                .whereEqualTo("unitType", "Bedspace")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bedspaceList.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String id = document.getId();
                            String description = document.getString("description");
                            String price = document.getString("price");
                            String contactNumber = document.getString("contactNumber");
                            String location = document.getString("location");
                            String imageUrl = document.getString("imageUrl");

                            Listings bedspace = new Listings(id, description, price, contactNumber, location, imageUrl);
                            bedspaceList.add(bedspace);
                        }
                        bedspaceAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Bedspace.this, "Error getting bedspaces: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
