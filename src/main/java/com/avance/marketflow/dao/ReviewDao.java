package com.avance.marketflow.dao;

import com.avance.marketflow.model.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReviewDao {
    private final AtomicLong ids = new AtomicLong(1);
    private final List<Review> reviews = new ArrayList<>();

    public List<Review> findAll() {
        return reviews;
    }

    public Review save(String productName, String buyerEmail, String sellerEmail, int rating, String condition, String comment) {
        Review review = new Review(ids.getAndIncrement(), productName, buyerEmail, sellerEmail, rating, condition, comment);
        reviews.add(0, review);
        return review;
    }
}

