package com.avance.marketflow.service;

import com.avance.marketflow.dao.AuditDao;
import com.avance.marketflow.dao.HelpMessageDao;
import com.avance.marketflow.dao.ProductDao;
import com.avance.marketflow.dao.ReviewDao;
import com.avance.marketflow.dao.SaleDao;
import com.avance.marketflow.dao.UserDao;
import com.avance.marketflow.model.CartItem;
import com.avance.marketflow.model.HelpMessage;
import com.avance.marketflow.model.Product;
import com.avance.marketflow.model.Review;
import com.avance.marketflow.model.Sale;
import com.avance.marketflow.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class MarketplaceService {
    private final ProductDao productDao;
    private final UserDao userDao;
    private final ReviewDao reviewDao;
    private final AuditDao auditDao;
    private final HelpMessageDao helpMessageDao;
    private final SaleDao saleDao;

    public MarketplaceService(ProductDao productDao, UserDao userDao, ReviewDao reviewDao, AuditDao auditDao, HelpMessageDao helpMessageDao, SaleDao saleDao) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.reviewDao = reviewDao;
        this.auditDao = auditDao;
        this.helpMessageDao = helpMessageDao;
        this.saleDao = saleDao;
    }

    public List<Product> approvedProducts() {
        return productDao.findApproved();
    }

    public List<Product> searchApproved(String query, String category) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        String normalizedCategory = category == null ? "" : category.trim().toLowerCase(Locale.ROOT);
        return productDao.findApproved().stream()
                .filter(product -> normalizedCategory.isBlank() || product.getCategory().toLowerCase(Locale.ROOT).equals(normalizedCategory))
                .filter(product -> normalizedQuery.isBlank()
                        || product.getName().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                        || product.getDescription().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                        || product.getCategory().toLowerCase(Locale.ROOT).contains(normalizedQuery))
                .toList();
    }

    public List<Product> allProducts() {
        return productDao.findAll();
    }

    public List<Product> sellerProducts(String sellerEmail) {
        return productDao.findBySeller(sellerEmail);
    }

    public Product publish(String name, String category, String description, BigDecimal price, int stock, String imageUrl, String sellerEmail) {
        Product product = productDao.save(name, category, description, price, stock, imageUrl, sellerEmail);
        auditDao.add("Catalogo", "PUBLICAR", sellerEmail + " publico " + name + " pendiente de aprobacion.");
        return product;
    }

    public void updateProduct(long productId, String name, String category, String description, BigDecimal price, int stock, String imageUrl, String sellerEmail, boolean admin) {
        productDao.findById(productId)
                .filter(product -> admin || product.getSellerEmail().equalsIgnoreCase(sellerEmail))
                .ifPresent(product -> {
                    product.setName(name);
                    product.setCategory(category);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setStock(stock);
                    if (imageUrl != null && !imageUrl.isBlank()) {
                        product.setImageUrl(imageUrl);
                    }
                    auditDao.add("Catalogo", "ACTUALIZAR_PRODUCTO", sellerEmail + " actualizo " + name);
                });
    }

    public void toggleProduct(long productId) {
        productDao.findById(productId).ifPresent(product -> {
            product.setApproved(!product.isApproved());
            auditDao.add("Catalogo", product.isApproved() ? "APROBAR" : "PAUSAR", product.getName());
        });
    }

    public void addToCart(List<CartItem> cart, long productId) {
        Product product = productDao.findById(productId).orElseThrow();
        if (product.getStock() <= 0 || !product.isApproved()) {
            return;
        }

        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                if (item.getQuantity() < product.getStock()) {
                    item.setQuantity(item.getQuantity() + 1);
                }
                return;
            }
        }
        cart.add(new CartItem(product, 1));
    }

    public void increase(List<CartItem> cart, long productId) {
        addToCart(cart, productId);
    }

    public void decrease(List<CartItem> cart, long productId) {
        cart.stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(item.getQuantity() - 1);
                    if (item.getQuantity() <= 0) {
                        cart.remove(item);
                    }
                });
    }

    public void remove(List<CartItem> cart, long productId) {
        cart.removeIf(item -> item.getProduct().getId() == productId);
    }

    public int itemCount(List<CartItem> cart) {
        return cart.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal total(List<CartItem> cart) {
        return cart.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal discount(List<CartItem> cart, String coupon) {
        BigDecimal total = total(cart);
        if (coupon == null || coupon.isBlank()) {
            return BigDecimal.ZERO;
        }
        String normalized = coupon.trim().toUpperCase(Locale.ROOT);
        if ("MARKET10".equals(normalized)) {
            return total.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        }
        if ("REGALO100".equals(normalized)) {
            return total.min(new BigDecimal("100.00")).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal payableTotal(List<CartItem> cart, String coupon) {
        return total(cart).subtract(discount(cart, coupon)).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    public List<String> checkout(User buyer, List<CartItem> cart, String payment, String receipt, String coupon) {
        List<String> reviewTokens = new ArrayList<>();
        BigDecimal subtotal = total(cart);
        BigDecimal appliedDiscount = discount(cart, coupon);
        BigDecimal payable = payableTotal(cart, coupon);
        int items = itemCount(cart);
        for (CartItem item : cart) {
            if (item.getQuantity() > item.getProduct().getStock()) {
                throw new IllegalStateException("Stock insuficiente para " + item.getProduct().getName());
            }
        }

        for (CartItem item : cart) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            reviewTokens.add(product.getName() + "|" + product.getSellerEmail());
        }

        buyer.setPurchases(buyer.getPurchases() + 1);
        if (coupon != null && !coupon.isBlank()) {
            buyer.getCoupons().remove(coupon);
        }
        if (buyer.getPurchases() >= 3 && !buyer.getCoupons().contains("REGALO100")) {
            buyer.getCoupons().add("REGALO100");
        }
        saleDao.save(buyer.getEmail(), payment, receipt, items, subtotal, appliedDiscount, payable);
        auditDao.add("Ventas", "CONFIRMAR_COMPRA", buyer.getEmail() + " pago con " + payment + " y comprobante " + receipt);
        cart.clear();
        return reviewTokens;
    }

    public Review review(String token, User buyer, int rating, String condition, String comment) {
        String[] parts = token.split("\\|");
        Review review = reviewDao.save(parts[0], buyer.getEmail(), parts[1], rating, condition, comment);
        productDao.findByNameAndSeller(parts[0], parts[1]).ifPresent(product -> product.addRating(rating));
        auditDao.add("Resenas", "CREAR_RESENA", buyer.getEmail() + " califico " + parts[0] + " con " + rating);
        return review;
    }

    public List<Review> reviews() {
        return reviewDao.findAll();
    }

    public HelpMessage sendHelpMessage(String name, String email, String message) {
        String cleanName = name == null || name.isBlank() ? "Visitante" : name.trim();
        String cleanEmail = email == null || email.isBlank() ? "sin correo" : email.trim();
        String cleanMessage = message == null || message.isBlank() ? "Consulta sin detalle." : message.trim();
        HelpMessage helpMessage = helpMessageDao.save(cleanName, cleanEmail, cleanMessage);
        auditDao.add("Ayuda", "NUEVA_CONSULTA", cleanName + " envio una consulta al admin.");
        return helpMessage;
    }

    public List<HelpMessage> helpMessages() {
        return helpMessageDao.findAll();
    }

    public void updateHelpMessage(long id, String status, String adminNote, String sellerEmail) {
        helpMessageDao.findById(id).ifPresent(message -> {
            message.setStatus(status);
            message.setAdminNote(adminNote == null ? "" : adminNote.trim());
            message.setSellerEmail(sellerEmail == null ? "" : sellerEmail.trim());
            if (sellerEmail != null && !sellerEmail.isBlank() && adminNote != null && !adminNote.isBlank()) {
                userDao.findByEmail(sellerEmail).ifPresent(seller -> seller.getAdminMessages().add(0, "Ayuda #" + id + " - " + status + ": " + adminNote));
            }
            auditDao.add("Ayuda", "ACTUALIZAR_CONSULTA", "Consulta #" + id + " marcada como " + status);
        });
    }

    public List<Sale> salesBetween(LocalDate start, LocalDate end) {
        return saleDao.findAll().stream()
                .filter(sale -> !sale.getDate().isBefore(start))
                .filter(sale -> !sale.getDate().isAfter(end))
                .sorted(Comparator.comparing(Sale::getDate).reversed())
                .toList();
    }

    public BigDecimal salesTotal(List<Sale> sales) {
        return sales.stream().map(Sale::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal salesIgv(List<Sale> sales) {
        return salesTotal(sales).multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal salesAverage(List<Sale> sales) {
        if (sales.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return salesTotal(sales).divide(new BigDecimal(sales.size()), 2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> totalsByPayment(List<Sale> sales) {
        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        for (Sale sale : sales) {
            totals.merge(sale.getPaymentMethod(), sale.getTotal(), BigDecimal::add);
        }
        return totals;
    }

    public void penalizeSeller(String email, String reason) {
        userDao.findByEmail(email).ifPresent(seller -> {
            seller.setPenalties(seller.getPenalties() + 1);
            seller.getPenaltyReasons().add(0, reason);
            if (seller.getPenalties() >= 5) {
                seller.setActive(false);
                productDao.findBySeller(email).forEach(product -> product.setApproved(false));
            }
            auditDao.add("Usuarios", "PENALIZAR", email + ": " + reason);
        });
    }

    public void removePenalty(String email) {
        userDao.findByEmail(email).ifPresent(seller -> {
            if (seller.getPenalties() > 0) {
                seller.setPenalties(seller.getPenalties() - 1);
                if (!seller.getPenaltyReasons().isEmpty()) {
                    seller.getPenaltyReasons().remove(0);
                }
                seller.setActive(true);
                auditDao.add("Usuarios", "RETIRAR_PENALIZACION", email);
            }
        });
    }
}
