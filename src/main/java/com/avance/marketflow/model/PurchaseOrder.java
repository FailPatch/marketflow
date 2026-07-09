package com.avance.marketflow.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrder {
    private long id;
    private String providerName;
    private boolean saved;
    private final List<Article> articles = new ArrayList<>();

    public PurchaseOrder(long id) {
        this.id = id;
    }

    public long getId() { return id; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public boolean isSaved() { return saved; }
    public void setSaved(boolean saved) { this.saved = saved; }
    public List<Article> getArticles() { return articles; }

    public BigDecimal getTotal() {
        return articles.stream().map(Article::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

