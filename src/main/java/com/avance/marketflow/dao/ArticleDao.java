package com.avance.marketflow.dao;

import com.avance.marketflow.model.Article;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleDao {
    private final AtomicLong ids = new AtomicLong(4);
    private final List<Article> articles = new ArrayList<>();

    public ArticleDao() {
        articles.add(new Article(1, "Mouse inalambrico", new BigDecimal("45.90")));
        articles.add(new Article(2, "Monitor LED 24 pulgadas", new BigDecimal("459.90")));
        articles.add(new Article(3, "Silla ergonomica", new BigDecimal("299.90")));
    }

    public List<Article> findAll() {
        return articles;
    }

    public List<Article> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }
        String normalized = query.toLowerCase();
        return articles.stream()
                .filter(article -> article.getName().toLowerCase().contains(normalized)
                        || String.valueOf(article.getId()).equals(normalized))
                .toList();
    }

    public Optional<Article> findById(long id) {
        return articles.stream().filter(article -> article.getId() == id).findFirst();
    }

    public Article save(String name, BigDecimal price) {
        Article article = new Article(ids.getAndIncrement(), name, price);
        articles.add(0, article);
        return article;
    }

    public void update(long id, String name, BigDecimal price) {
        findById(id).ifPresent(article -> {
            article.setName(name);
            article.setPrice(price);
        });
    }

    public void delete(long id) {
        articles.removeIf(article -> article.getId() == id);
    }
}
