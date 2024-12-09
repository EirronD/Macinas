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

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private List<Listings> houseList;
    private String accountType;

    public HouseAdapter(List<Listings> houseList, String accountType) {
        this.houseList = houseList;
        this.accountType = accountType;
    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_layout, parent, false);
        return new HouseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HouseViewHolder holder, int position) {
        Listings house = houseList.get(position);
        holder.descriptionTextView.setText(house.getDescription());
        holder.priceTextView.setText(house.getPrice());
        holder.contactNumberTextView.setText(house.getContactNumber());
        holder.locationTextView.setText(house.getLocation());
        Glide.with(holder.itemView.getContext())
                .load(house.getImageUrl())
                .into(holder.image);
        if ("Landlord".equalsIgnoreCase(accountType)) {
            holder.imageViewAction.setImageResource(R.drawable.delete_button);
            holder.imageViewAction.setOnClickListener(v -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("listings").document(house.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(holder.itemView.getContext(), "Apartment deleted!", Toast.LENGTH_SHORT).show();
                            houseList.remove(position);
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
        return houseList.size();
    }

    public static class HouseViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTextView;
        public TextView priceTextView;
        public TextView contactNumberTextView;
        public TextView locationTextView;
        ImageView image, imageViewAction;

        public HouseViewHolder(View view) {
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
