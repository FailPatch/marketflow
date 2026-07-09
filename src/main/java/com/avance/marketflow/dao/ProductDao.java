package com.avance.marketflow.dao;

import com.avance.marketflow.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductDao {
    private final AtomicLong ids = new AtomicLong(15);
    private final List<Product> products = new ArrayList<>();

    public ProductDao() {
        products.add(new Product(1, "Audifonos Bluetooth", "Tecnologia", "Audifonos inalambricos con estuche de carga.", new BigDecimal("89.90"), 20, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=700", "vendedor@demo.com", true));
        products.add(new Product(2, "Teclado mecanico", "Tecnologia", "Teclado compacto para estudio, trabajo y juegos.", new BigDecimal("149.90"), 12, "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=700", "vendedor@demo.com", true));
        products.add(new Product(3, "Lampara LED", "Hogar", "Lampara regulable para escritorio y oficina.", new BigDecimal("59.90"), 15, "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=700", "vendedor@demo.com", true));
        products.add(new Product(4, "Mochila urbana", "Moda", "Mochila resistente con compartimiento para laptop.", new BigDecimal("79.90"), 18, "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=700", "vendedor@demo.com", true));
        products.add(new Product(5, "Smartwatch Fit Pro", "Tecnologia", "Reloj inteligente con monitor cardiaco y modos deportivos.", new BigDecimal("129.90"), 22, "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=700", "vendedor@demo.com", true));
        products.add(new Product(6, "Zapatillas Gold Rose", "Moda", "Zapatillas urbanas comodas con acabado rose gold.", new BigDecimal("49.90"), 40, "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=700", "vendedor@demo.com", true));
        products.add(new Product(7, "Silla Gamer Nebula", "Gaming", "Silla ergonomica reclinable para sesiones largas de juego.", new BigDecimal("399.90"), 8, "https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=700", "vendedor@demo.com", true));
        products.add(new Product(8, "Mouse RGB Precision", "Gaming", "Mouse liviano con sensor de alta precision y luces RGB.", new BigDecimal("69.90"), 35, "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=700", "vendedor@demo.com", true));
        products.add(new Product(9, "Set Skincare Glow", "Belleza", "Rutina facial basica con limpiador, serum e hidratante.", new BigDecimal("119.90"), 16, "https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=700", "vendedor@demo.com", true));
        products.add(new Product(10, "Freidora de aire 5L", "Hogar", "Freidora sin aceite con canasta antiadherente y control digital.", new BigDecimal("259.90"), 10, "https://images.unsplash.com/photo-1612817159949-195b6eb9e31a?w=700", "vendedor@demo.com", true));
        products.add(new Product(11, "Celular Nova X", "Celulares", "Smartphone con pantalla AMOLED, 128GB y carga rapida.", new BigDecimal("899.90"), 11, "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=700", "vendedor@demo.com", true));
        products.add(new Product(12, "Cargador USB-C 65W", "Celulares", "Cargador compacto compatible con celulares, tablets y laptops.", new BigDecimal("59.90"), 55, "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=700", "vendedor@demo.com", true));
        products.add(new Product(13, "Balon Pro Training", "Deportes", "Balon resistente para entrenamiento en cancha sintetica.", new BigDecimal("39.90"), 28, "https://images.unsplash.com/photo-1614632537197-38a17061c2bd?w=700", "vendedor@demo.com", true));
        products.add(new Product(14, "Mat Yoga Antideslizante", "Deportes", "Mat ligero con textura firme para yoga, pilates y ejercicios.", new BigDecimal("54.90"), 24, "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=700", "vendedor@demo.com", true));
    }

    public List<Product> findAll() {
        return products;
    }

    public List<Product> findApproved() {
        return products.stream().filter(Product::isApproved).toList();
    }

    public List<Product> findBySeller(String sellerEmail) {
        return products.stream().filter(product -> product.getSellerEmail().equalsIgnoreCase(sellerEmail)).toList();
    }

    public Optional<Product> findById(long id) {
        return products.stream().filter(product -> product.getId() == id).findFirst();
    }

    public Optional<Product> findByNameAndSeller(String name, String sellerEmail) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name))
                .filter(product -> product.getSellerEmail().equalsIgnoreCase(sellerEmail))
                .findFirst();
    }

    public Product save(String name, String category, String description, BigDecimal price, int stock, String imageUrl, String sellerEmail) {
        Product product = new Product(ids.getAndIncrement(), name, category, description, price, stock, imageUrl, sellerEmail, false);
        products.add(0, product);
        return product;
    }
}
