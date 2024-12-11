package com.example.bahi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Listing extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextDescription, editTextPrice, editTextContactNumber, editTextLocation;
    private Spinner spinnerUnitType;
    private Button buttonSubmit, buttonCapturePicture;
    private ImageView imageViewPicture;

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("listings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextContactNumber = view.findViewById(R.id.editTextContactNumber);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        spinnerUnitType = view.findViewById(R.id.spinnerUnitType);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonCapturePicture = view.findViewById(R.id.buttonCapturePicture);
        imageViewPicture = view.findViewById(R.id.imageViewPicture);

        String[] unitTypes = {"Condo", "House", "Apartment", "Bedspace"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, unitTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnitType.setAdapter(adapter);

        buttonCapturePicture.setOnClickListener(v -> openFileChooser());
        buttonSubmit.setOnClickListener(v -> uploadImageAndSaveData());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewPicture.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSaveData() {
        if (imageUri == null) {
            Toast.makeText(getContext(), "Please select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = editTextDescription.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String contactNumber = editTextContactNumber.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String unitType = spinnerUnitType.getSelectedItem().toString();

        if (!isValidPhilippinePrefix(contactNumber)) {
            Toast.makeText(getContext(), "Invalid phone number prefix.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty() || price.isEmpty() || contactNumber.isEmpty() || location.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    Map<String, Object> listingData = new HashMap<>();
                    listingData.put("description", description);
                    listingData.put("price", price);
                    listingData.put("contactNumber", contactNumber);
                    listingData.put("location", location);
                    listingData.put("unitType", unitType);
                    listingData.put("imageUrl", imageUrl);

                    db.collection("listings")
                            .add(listingData)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(getContext(), "Listing saved successfully.", Toast.LENGTH_SHORT).show();
                                resetFields();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Error saving listing: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                })
        ).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void resetFields() {
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextContactNumber.setText("");
        editTextLocation.setText("");
        spinnerUnitType.setSelection(0);
        imageViewPicture.setImageResource(0);
        imageUri = null;
    }

    private boolean isValidPhilippinePrefix(String cPhone) {
        String[] validPrefixes = {
                "0905", "0906", "0907", "0908", "0909", "0910", "0912", "0915", "0916", "0917",
                "0918", "0919", "0920", "0921", "0922", "0923", "0925", "0926", "0927", "0928",
                "0929", "0930", "0935", "0936", "0937", "0938", "0939", "0940", "0942", "0943",
                "0945", "0946", "0947", "0948", "0949", "0950", "0951", "0955", "0956", "0957",
                "0961", "0963", "0965", "0966", "0967", "0970", "0971", "0973", "0974", "0975",
                "0977", "0978", "0979", "0981", "0989", "0991", "0992", "0993", "0994", "0995",
                "0996", "0997", "0998", "0999"
        };

        for (String prefix : validPrefixes) {
            if (cPhone.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
