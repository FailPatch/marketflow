package com.avance.marketflow.model;

import java.math.BigDecimal;

public class Product {
    private long id;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private String sellerEmail;
    private boolean approved;
    private double ratingAverage = 5.0;
    private int reviewCount;

    public Product(long id, String name, String category, String description, BigDecimal price, int stock, String imageUrl, String sellerEmail, boolean approved) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.sellerEmail = sellerEmail;
        this.approved = approved;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public String getSellerEmail() { return sellerEmail; }
    public boolean isApproved() { return approved; }
    public double getRatingAverage() { return ratingAverage; }
    public int getReviewCount() { return reviewCount; }
    public String getFilledStars() {
        int filled = (int) Math.round(ratingAverage);
        return "★".repeat(Math.max(0, Math.min(5, filled)));
    }
    public String getEmptyStars() {
        int filled = (int) Math.round(ratingAverage);
        return "☆".repeat(Math.max(0, 5 - Math.max(0, Math.min(5, filled))));
    }

    public void setStock(int stock) { this.stock = stock; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void addRating(int rating) {
        if (reviewCount == 0) {
            ratingAverage = rating;
        } else {
            ratingAverage = ((ratingAverage * reviewCount) + rating) / (reviewCount + 1);
        }
        reviewCount++;
    }
}
