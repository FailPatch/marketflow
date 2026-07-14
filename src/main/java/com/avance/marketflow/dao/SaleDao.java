package com.avance.marketflow.dao;

import com.avance.marketflow.model.Sale;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SaleDao {
    private final AtomicLong ids = new AtomicLong(13);
    private final List<Sale> sales = new ArrayList<>();

    public SaleDao() {
        sales.add(new Sale(1, LocalDate.of(2026, 6, 14), "comprador@demo.com", "Yape", "Boleta", 2, new BigDecimal("179.80"), BigDecimal.ZERO, new BigDecimal("179.80")));
        sales.add(new Sale(2, LocalDate.of(2026, 6, 15), "comprador@demo.com", "Tarjeta", "Factura", 1, new BigDecimal("149.90"), new BigDecimal("14.99"), new BigDecimal("134.91")));
        sales.add(new Sale(3, LocalDate.of(2026, 6, 18), "cliente@nexora.com", "Plin", "Ticket", 3, new BigDecimal("239.70"), BigDecimal.ZERO, new BigDecimal("239.70")));
        sales.add(new Sale(4, LocalDate.of(2026, 6, 19), "cliente@nexora.com", "Efectivo", "Boleta", 1, new BigDecimal("59.90"), BigDecimal.ZERO, new BigDecimal("59.90")));
        sales.add(new Sale(5, LocalDate.of(2026, 7, 8), "ana@nexora.com", "Yape", "Boleta", 2, new BigDecimal("149.80"), new BigDecimal("14.98"), new BigDecimal("134.82")));
        sales.add(new Sale(6, LocalDate.of(2026, 7, 9), "luis@nexora.com", "Plin", "Ticket", 1, new BigDecimal("89.90"), BigDecimal.ZERO, new BigDecimal("89.90")));
        sales.add(new Sale(7, LocalDate.of(2026, 7, 10), "maria@nexora.com", "Tarjeta", "Factura", 3, new BigDecimal("329.70"), new BigDecimal("32.97"), new BigDecimal("296.73")));
        sales.add(new Sale(8, LocalDate.of(2026, 7, 11), "diego@nexora.com", "Efectivo", "Boleta", 1, new BigDecimal("79.90"), BigDecimal.ZERO, new BigDecimal("79.90")));
        sales.add(new Sale(9, LocalDate.of(2026, 7, 12), "sofia@nexora.com", "Yape", "Ticket", 2, new BigDecimal("189.80"), new BigDecimal("18.98"), new BigDecimal("170.82")));
        sales.add(new Sale(10, LocalDate.of(2026, 7, 13), "comprador@demo.com", "Efectivo", "Ticket", 1, new BigDecimal("59.90"), BigDecimal.ZERO, new BigDecimal("59.90")));
        sales.add(new Sale(11, LocalDate.of(2026, 7, 13), "cliente@nexora.com", "Tarjeta", "Boleta", 2, new BigDecimal("219.80"), new BigDecimal("21.98"), new BigDecimal("197.82")));
        sales.add(new Sale(12, LocalDate.of(2026, 7, 14), "comprador@demo.com", "Mixto", "Boleta", 2, new BigDecimal("219.80"), new BigDecimal("21.98"), new BigDecimal("197.82")));
    }

    public List<Sale> findAll() {
        return sales;
    }

    public Sale save(String buyerEmail, String paymentMethod, String receiptType, int items, BigDecimal subtotal, BigDecimal discount, BigDecimal total) {
        Sale sale = new Sale(ids.getAndIncrement(), LocalDate.now(), buyerEmail, paymentMethod, receiptType, items, subtotal, discount, total);
        sales.add(0, sale);
        return sale;
    }
}
