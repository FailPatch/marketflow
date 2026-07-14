package com.avance.marketflow.controller;

import com.avance.marketflow.model.CartItem;
import com.avance.marketflow.model.Role;
import com.avance.marketflow.model.User;
import com.avance.marketflow.service.MarketplaceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Controller
public class MarketplaceController {
    private final MarketplaceService marketplaceService;

    public MarketplaceController(MarketplaceService marketplaceService) {
        this.marketplaceService = marketplaceService;
    }

    @GetMapping({"/", "/products"})
    public String home(@RequestParam(required = false) String query, @RequestParam(required = false) String category, Model model, HttpSession session) {
        List<CartItem> cart = cart(session);
        List<String> categories = marketplaceService.approvedProducts().stream()
                .map(product -> product.getCategory())
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        model.addAttribute("products", marketplaceService.searchApproved(query, category));
        model.addAttribute("featured", marketplaceService.approvedProducts().stream().limit(3).toList());
        model.addAttribute("categories", categories);
        model.addAttribute("query", query);
        model.addAttribute("category", category);
        model.addAttribute("cartCount", marketplaceService.itemCount(cart));
        return "products";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam long productId, HttpSession session) {
        marketplaceService.addToCart(cart(session), productId);
        return "redirect:/products#catalogo";
    }

    @PostMapping("/cart/increase")
    public String increase(@RequestParam long productId, HttpSession session) {
        marketplaceService.increase(cart(session), productId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease")
    public String decrease(@RequestParam long productId, HttpSession session) {
        marketplaceService.decrease(cart(session), productId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam long productId, HttpSession session) {
        marketplaceService.remove(cart(session), productId);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(@RequestParam(required = false) String coupon, Model model, HttpSession session) {
        List<CartItem> cart = cart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("total", marketplaceService.total(cart));
        model.addAttribute("discount", marketplaceService.discount(cart, coupon));
        model.addAttribute("payableTotal", marketplaceService.payableTotal(cart, coupon));
        model.addAttribute("coupon", coupon);
        return "cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String payment, @RequestParam String receipt, @RequestParam(required = false) String coupon, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRole() != Role.BUYER && user.getRole() != Role.ADMIN)) {
            return "redirect:/login";
        }
        List<String> reviewTokens = marketplaceService.checkout(user, cart(session), payment, receipt, coupon);
        session.setAttribute("reviewTokens", reviewTokens);
        model.addAttribute("message", "Compra confirmada con " + payment + ". Comprobante: " + receipt);
        return "confirmation";
    }

    @GetMapping("/seller")
    public String seller(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
            return "redirect:/login";
        }
        model.addAttribute("products", user.getRole() == Role.ADMIN ? marketplaceService.allProducts() : marketplaceService.sellerProducts(user.getEmail()));
        return "seller";
    }

    @PostMapping("/seller/products")
    public String publish(@RequestParam String name, @RequestParam String category, @RequestParam String description, @RequestParam BigDecimal price, @RequestParam int stock, @RequestParam(required = false) String imageUrl, @RequestParam(required = false) MultipartFile imageFile, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
            return "redirect:/login";
        }
        marketplaceService.publish(name, category, description, price, stock, imageSource(imageUrl, imageFile), user.getEmail());
        return "redirect:/seller";
    }

    @PostMapping("/reviews")
    public String review(@RequestParam String token, @RequestParam int rating, @RequestParam String condition, @RequestParam String comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != Role.BUYER) {
            return "redirect:/login";
        }
        marketplaceService.review(token, user, rating, condition, comment);
        return "redirect:/products";
    }

    @GetMapping("/faq")
    public String faq(@RequestParam(required = false) String sent, Model model) {
        model.addAttribute("sent", sent != null);
        return "faq";
    }

    @PostMapping("/faq")
    public String sendHelp(@RequestParam String message, @RequestParam(required = false) String name, @RequestParam(required = false) String email, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String senderName = user == null ? name : user.getName();
        String senderEmail = user == null ? email : user.getEmail();
        marketplaceService.sendHelpMessage(senderName, senderEmail, message);
        return "redirect:/faq?sent=1";
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> cart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private String imageSource(String imageUrl, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String contentType = imageFile.getContentType() == null ? "image/png" : imageFile.getContentType();
                return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(imageFile.getBytes());
            }
        } catch (Exception ignored) {
            // If the demo upload fails, the optional URL keeps the publication usable.
        }
        if (imageUrl != null && !imageUrl.isBlank()) {
            return imageUrl;
        }
        return "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=700";
    }
}
