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

import java.time.LocalDate;

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
        model.addAttribute("helpMessages", marketplaceService.helpMessages());
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

    @PostMapping("/admin/help/read")
    public String readHelp(@RequestParam long messageId, @RequestParam String status, @RequestParam(required = false) String adminNote, @RequestParam(required = false) String sellerEmail, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        marketplaceService.updateHelpMessage(messageId, status, adminNote, sellerEmail);
        return "redirect:/admin#ayuda-admin";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || (user.getRole() != Role.ADMIN && user.getRole() != Role.EMPLOYEE)) return "redirect:/login";
        model.addAttribute("products", marketplaceService.allProducts().size());
        model.addAttribute("reviews", marketplaceService.reviews().size());
        model.addAttribute("salesTotal", marketplaceService.salesTotal(marketplaceService.salesBetween(LocalDate.now().minusDays(6), LocalDate.now())));
        model.addAttribute("salesCount", marketplaceService.salesBetween(LocalDate.now().minusDays(6), LocalDate.now()).size());
        model.addAttribute("helpMessages", marketplaceService.helpMessages());
        model.addAttribute("logs", auditDao.findAll());
        return "dashboard";
    }

    @GetMapping("/reports")
    public String reports(HttpSession session) {
        if (!canAccessDashboard(session)) return "redirect:/login";
        return "reports";
    }

    @GetMapping("/reports/sales")
    public String salesReport(@RequestParam(required = false) String start, @RequestParam(required = false) String end, Model model, HttpSession session) {
        if (!canAccessDashboard(session)) return "redirect:/login";
        LocalDate endDate = end == null || end.isBlank() ? LocalDate.now() : LocalDate.parse(end);
        LocalDate startDate = start == null || start.isBlank() ? endDate.minusDays(6) : LocalDate.parse(start);
        var sales = marketplaceService.salesBetween(startDate, endDate);
        model.addAttribute("start", startDate);
        model.addAttribute("end", endDate);
        model.addAttribute("sales", sales);
        model.addAttribute("salesTotal", marketplaceService.salesTotal(sales));
        model.addAttribute("salesIgv", marketplaceService.salesIgv(sales));
        model.addAttribute("salesAverage", marketplaceService.salesAverage(sales));
        model.addAttribute("paymentTotals", marketplaceService.totalsByPayment(sales));
        return "sales-report";
    }

    @GetMapping("/performance")
    public String performance(HttpSession session) {
        if (!canAccessDashboard(session)) return "redirect:/login";
        return "performance";
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == Role.ADMIN;
    }

    private boolean canAccessDashboard(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.EMPLOYEE);
    }
}
