package com.example.bahi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder> {

    private List<Listings> apartmentList;
    private String accountType;

    public ApartmentAdapter(List<Listings> apartmentList, String accountType) {
        this.apartmentList = apartmentList;
        this.accountType = accountType;
    }

    @NonNull
    @Override
    public ApartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_layout, parent, false);
        return new ApartmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentViewHolder holder, int position) {
        Listings apartment = apartmentList.get(position);

        // Set data to TextViews
        holder.descriptionTextView.setText(apartment.getDescription());
        holder.priceTextView.setText(apartment.getPrice());
        holder.contactNumberTextView.setText(apartment.getContactNumber());
        holder.locationTextView.setText(apartment.getLocation());

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(apartment.getImageUrl())
                .into(holder.image);

        if ("Landlord".equalsIgnoreCase(accountType)) {
            holder.imageViewAction.setImageResource(R.drawable.delete_button);
            holder.imageViewAction.setOnClickListener(v -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("listings").document(apartment.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(holder.itemView.getContext(), "Apartment deleted!", Toast.LENGTH_SHORT).show();
                            apartmentList.remove(position);
                            notifyItemRemoved(position);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(holder.itemView.getContext(), "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });
        } else {
            holder.imageViewAction.setImageResource(R.drawable.baseline_favorite_24);
            holder.imageViewAction.setOnClickListener(v -> {
                // Handle "favorite" action if needed
                Toast.makeText(holder.itemView.getContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView, priceTextView, contactNumberTextView, locationTextView;
        ImageView image, imageViewAction;

        public ApartmentViewHolder(@NonNull View view) {
            super(view);
            descriptionTextView = view.findViewById(R.id.textViewDescription);
            priceTextView = view.findViewById(R.id.textViewPrice);
            contactNumberTextView = view.findViewById(R.id.textViewContactNumber);
            locationTextView = view.findViewById(R.id.textViewLocation);
            image = view.findViewById(R.id.imageView);
            imageViewAction = view.findViewById(R.id.imageViewAction);
        }
    }
}
