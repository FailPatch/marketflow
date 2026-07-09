package com.avance.marketflow.model;

import java.math.BigDecimal;

public class Article {
    private long id;
    private String name;
    private BigDecimal price;

    public Article(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

