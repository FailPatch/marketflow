package com.avance.marketflow.dao;

import com.avance.marketflow.model.PurchaseOrder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PurchaseOrderDao {
    private final AtomicLong ids = new AtomicLong(1);
    private final List<PurchaseOrder> orders = new ArrayList<>();

    public PurchaseOrder create() {
        return new PurchaseOrder(ids.getAndIncrement());
    }

    public void save(PurchaseOrder order) {
        order.setSaved(true);
        orders.add(0, order);
    }

    public List<PurchaseOrder> findAll() {
        return orders;
    }
}

