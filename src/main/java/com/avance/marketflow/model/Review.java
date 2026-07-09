package com.avance.marketflow.model;

public class Review {
    private long id;
    private String productName;
    private String buyerEmail;
    private String sellerEmail;
    private int rating;
    private String condition;
    private String comment;

    public Review(long id, String productName, String buyerEmail, String sellerEmail, int rating, String condition, String comment) {
        this.id = id;
        this.productName = productName;
        this.buyerEmail = buyerEmail;
        this.sellerEmail = sellerEmail;
        this.rating = rating;
        this.condition = condition;
        this.comment = comment;
    }

    public long getId() { return id; }
    public String getProductName() { return productName; }
    public String getBuyerEmail() { return buyerEmail; }
    public String getSellerEmail() { return sellerEmail; }
    public int getRating() { return rating; }
    public String getCondition() { return condition; }
    public String getComment() { return comment; }
}

