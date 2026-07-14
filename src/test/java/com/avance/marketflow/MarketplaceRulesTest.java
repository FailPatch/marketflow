package com.avance.marketflow;

import com.avance.marketflow.dao.AuditDao;
import com.avance.marketflow.dao.HelpMessageDao;
import com.avance.marketflow.dao.ProductDao;
import com.avance.marketflow.dao.ReviewDao;
import com.avance.marketflow.dao.UserDao;
import com.avance.marketflow.model.CartItem;
import com.avance.marketflow.model.Product;
import com.avance.marketflow.model.User;
import com.avance.marketflow.service.MarketplaceService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarketplaceRulesTest {
    @Test
    void checkoutRejectsQuantityGreaterThanStock() {
        ProductDao productDao = new ProductDao();
        UserDao userDao = new UserDao();
        MarketplaceService service = new MarketplaceService(productDao, userDao, new ReviewDao(), new AuditDao(), new HelpMessageDao());
        User buyer = userDao.findByEmail("comprador@demo.com").orElseThrow();
        Product product = productDao.findById(1).orElseThrow();
        List<CartItem> cart = new ArrayList<>();

        cart.add(new CartItem(product, product.getStock() + 1));

        assertThrows(IllegalStateException.class, () -> service.checkout(buyer, cart, "Efectivo", "Ticket", ""));
        assertTrue(product.getStock() >= 0);
    }

    @Test
    void badReviewLowersProductRating() {
        ProductDao productDao = new ProductDao();
        UserDao userDao = new UserDao();
        MarketplaceService service = new MarketplaceService(productDao, userDao, new ReviewDao(), new AuditDao(), new HelpMessageDao());
        User buyer = userDao.findByEmail("comprador@demo.com").orElseThrow();
        Product product = productDao.findById(1).orElseThrow();

        service.review("Audifonos Bluetooth|vendedor@demo.com", buyer, 1, "No coincide", "Producto distinto al publicado");

        assertEquals(1.0, product.getRatingAverage());
        assertEquals("★", product.getFilledStars());
        assertEquals("☆☆☆☆", product.getEmptyStars());
    }
}
