package com.avance.marketflow.dao;

import com.avance.marketflow.model.Provider;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProviderDao {
    private final AtomicLong ids = new AtomicLong(2);
    private final List<Provider> providers = new ArrayList<>();

    public ProviderDao() {
        providers.add(new Provider(1, "Proveedor Central SAC", "20481234567"));
    }

    public List<Provider> findAll() {
        return providers;
    }

    public Provider save(String name, String ruc) {
        Provider provider = new Provider(ids.getAndIncrement(), name, ruc);
        providers.add(0, provider);
        return provider;
    }

    public Optional<Provider> search(String query) {
        String term = query.toLowerCase();
        return providers.stream()
                .filter(provider -> provider.getName().toLowerCase().contains(term) || provider.getRuc().contains(query))
                .findFirst();
    }
}

