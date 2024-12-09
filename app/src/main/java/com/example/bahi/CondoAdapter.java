package com.example.bahi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CondoAdapter extends RecyclerView.Adapter<CondoAdapter.CondoViewHolder> {

    private List<Listings> condoList;
    private String accountType;

    public CondoAdapter(List<Listings> condoList, String accountType) {
        this.condoList = condoList;
        this.accountType = accountType;
    }

    @Override
    public CondoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_layout, parent, false);
        return new CondoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CondoViewHolder holder, int position) {
        Listings condo = condoList.get(position);
        holder.descriptionTextView.setText(condo.getDescription());
        holder.priceTextView.setText(condo.getPrice());
        holder.contactNumberTextView.setText(condo.getContactNumber());
        holder.locationTextView.setText(condo.getLocation());
        Glide.with(holder.itemView.getContext())
                .load(condo.getImageUrl())
                .into(holder.image);
        if ("Landlord".equalsIgnoreCase(accountType)) {
            holder.imageViewAction.setImageResource(R.drawable.delete_button);
            holder.imageViewAction.setOnClickListener(v -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("listings").document(condo.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(holder.itemView.getContext(), "Apartment deleted!", Toast.LENGTH_SHORT).show();
                            condoList.remove(position);
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
        return condoList.size();
    }

    public static class CondoViewHolder extends RecyclerView.ViewHolder {

        public TextView descriptionTextView;
        public TextView priceTextView;
        public TextView contactNumberTextView;
        public TextView locationTextView;
        ImageView image, imageViewAction;

        public CondoViewHolder(View view) {
            super(view);
            descriptionTextView = view.findViewById(R.id.textViewDescription);
            priceTextView = view.findViewById(R.id.textViewPrice);
            contactNumberTextView = view.findViewById(R.id.textViewContactNumber);
            locationTextView = view.findViewById(R.id.textViewLocation);
            image = itemView.findViewById(R.id.imageView);
            imageViewAction = view.findViewById(R.id.imageViewAction);
        }
    }
}
