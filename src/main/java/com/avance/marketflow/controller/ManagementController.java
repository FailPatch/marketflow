package com.avance.marketflow.controller;

import com.avance.marketflow.model.PurchaseOrder;
import com.avance.marketflow.model.Role;
import com.avance.marketflow.model.User;
import com.avance.marketflow.service.PurchaseOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class ManagementController {
    private final PurchaseOrderService purchaseOrderService;

    public ManagementController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping("/employees")
    public String employees(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("employees", purchaseOrderService.employees());
        return "employees";
    }

    @PostMapping("/employees")
    public String saveEmployee(@RequestParam String name, @RequestParam String email, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        purchaseOrderService.saveEmployee(name, email);
        return "redirect:/employees";
    }

    @GetMapping("/providers")
    public String providers(Model model, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        model.addAttribute("providers", purchaseOrderService.providers());
        return "providers";
    }

    @PostMapping("/providers")
    public String saveProvider(@RequestParam String name, @RequestParam String ruc, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        purchaseOrderService.saveProvider(name, ruc);
        return "redirect:/providers";
    }

    @GetMapping("/articles")
    public String articles(@RequestParam(required = false) String query, Model model, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        model.addAttribute("articles", purchaseOrderService.searchArticles(query));
        model.addAttribute("query", query);
        return "articles";
    }

    @PostMapping("/articles")
    public String saveArticle(@RequestParam String name, @RequestParam BigDecimal price, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        purchaseOrderService.saveArticle(name, price);
        return "redirect:/articles";
    }

    @PostMapping("/articles/update")
    public String updateArticle(@RequestParam long id, @RequestParam String name, @RequestParam BigDecimal price, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        purchaseOrderService.updateArticle(id, name, price);
        return "redirect:/articles";
    }

    @PostMapping("/articles/delete")
    public String deleteArticle(@RequestParam long id, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        purchaseOrderService.deleteArticle(id);
        return "redirect:/articles";
    }

    @GetMapping("/purchase-order")
    public String purchaseOrder(Model model, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        model.addAttribute("articles", purchaseOrderService.articles());
        model.addAttribute("providers", purchaseOrderService.providers());
        model.addAttribute("orders", purchaseOrderService.orders());
        model.addAttribute("order", order(session));
        return "purchase-order";
    }

    @PostMapping("/purchase-order")
    public String purchaseOrder(@RequestParam String action, @RequestParam(required = false) Long articleId, @RequestParam(required = false) Integer index, @RequestParam(required = false) String providerName, HttpSession session) {
        if (!canManage(session)) return "redirect:/login";
        PurchaseOrder order = order(session);
        if ("new".equals(action)) {
            session.setAttribute("purchaseOrder", purchaseOrderService.newOrder());
        } else if ("add".equals(action) && articleId != null) {
            purchaseOrderService.addArticle(order, articleId);
        } else if ("remove".equals(action) && index != null) {
            purchaseOrderService.removeArticle(order, index);
        } else if ("save".equals(action)) {
            purchaseOrderService.saveOrder(order, providerName);
        }
        return "redirect:/purchase-order";
    }

    private PurchaseOrder order(HttpSession session) {
        PurchaseOrder order = (PurchaseOrder) session.getAttribute("purchaseOrder");
        if (order == null) {
            order = purchaseOrderService.newOrder();
            session.setAttribute("purchaseOrder", order);
        }
        return order;
    }

    private boolean canManage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.EMPLOYEE);
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == Role.ADMIN;
    }
}
