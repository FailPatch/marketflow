package com.avance.marketflow.controller;

import com.avance.marketflow.dao.AuditDao;
import com.avance.marketflow.dao.UserDao;
import com.avance.marketflow.model.Role;
import com.avance.marketflow.model.User;
import com.avance.marketflow.service.MarketplaceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private final MarketplaceService marketplaceService;
    private final UserDao userDao;
    private final AuditDao auditDao;

    public AdminController(MarketplaceService marketplaceService, UserDao userDao, AuditDao auditDao) {
        this.marketplaceService = marketplaceService;
        this.userDao = userDao;
        this.auditDao = auditDao;
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("products", marketplaceService.allProducts());
        model.addAttribute("sellers", userDao.findSellers());
        model.addAttribute("reviews", marketplaceService.reviews());
        model.addAttribute("logs", auditDao.findAll());
        return "admin";
    }

    @PostMapping("/admin/product")
    public String toggleProduct(@RequestParam long productId, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        marketplaceService.toggleProduct(productId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/penalize")
    public String penalize(@RequestParam String email, @RequestParam String reason, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        marketplaceService.penalizeSeller(email, reason);
        return "redirect:/admin";
    }

    @PostMapping("/admin/remove-penalty")
    public String removePenalty(@RequestParam String email, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        marketplaceService.removePenalty(email);
        return "redirect:/admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRole() != Role.ADMIN && user.getRole() != Role.EMPLOYEE)) return "redirect:/login";
        model.addAttribute("products", marketplaceService.allProducts().size());
        model.addAttribute("reviews", marketplaceService.reviews().size());
        model.addAttribute("logs", auditDao.findAll());
        return "dashboard";
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == Role.ADMIN;
    }
}
