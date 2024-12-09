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

public class BedspaceAdapter extends RecyclerView.Adapter<BedspaceAdapter.BedspaceViewHolder> {

    private List<Listings> bedspaceList;
    private String accountType;

    public BedspaceAdapter(List<Listings> bedspaceList, String accountType) {
        this.bedspaceList = bedspaceList;
        this.accountType = accountType;
    }

    @Override
    public BedspaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_layout, parent, false);
        return new BedspaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BedspaceViewHolder holder, int position) {
        Listings bedspace = bedspaceList.get(position);
        holder.descriptionTextView.setText(bedspace.getDescription());
        holder.priceTextView.setText(bedspace.getPrice());
        holder.contactNumberTextView.setText(bedspace.getContactNumber());
        holder.locationTextView.setText(bedspace.getLocation());
        Glide.with(holder.itemView.getContext())
                .load(bedspace.getImageUrl())
                .into(holder.image);
        if ("Landlord".equalsIgnoreCase(accountType)) {
            holder.imageViewAction.setImageResource(R.drawable.delete_button);
            holder.imageViewAction.setOnClickListener(v -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("listings").document(bedspace.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(holder.itemView.getContext(), "Apartment deleted!", Toast.LENGTH_SHORT).show();
                            bedspaceList.remove(position);
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
        return bedspaceList.size();
    }

    public static class BedspaceViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTextView;
        public TextView priceTextView;
        public TextView contactNumberTextView;
        public TextView locationTextView;
        ImageView image, imageViewAction;

        public BedspaceViewHolder(View view) {
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
