package com.example.bahi;

public class Listings {
    private String id;
    private String description;
    private String price;
    private String contactNumber;
    private String location;
    private String imageUrl;

    // Constructor
    public Listings(String id,String description, String price, String contactNumber, String location, String imageUrl) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.contactNumber = contactNumber;
        this.location = location;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getLocation() {
        return location;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
