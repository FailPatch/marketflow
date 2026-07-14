package com.avance.marketflow.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Sale {
    private final long id;
    private final LocalDate date;
    private final String buyerEmail;
    private final String paymentMethod;
    private final String receiptType;
    private final int items;
    private final BigDecimal subtotal;
    private final BigDecimal discount;
    private final BigDecimal total;

    public Sale(long id, LocalDate date, String buyerEmail, String paymentMethod, String receiptType, int items, BigDecimal subtotal, BigDecimal discount, BigDecimal total) {
        this.id = id;
        this.date = date;
        this.buyerEmail = buyerEmail;
        this.paymentMethod = paymentMethod;
        this.receiptType = receiptType;
        this.items = items;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
    }

    public long getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getBuyerEmail() { return buyerEmail; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getReceiptType() { return receiptType; }
    public int getItems() { return items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getTotal() { return total; }

    public String getDateLabel() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
