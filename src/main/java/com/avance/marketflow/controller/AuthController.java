package com.avance.marketflow.controller;

import com.avance.marketflow.model.Role;
import com.avance.marketflow.model.User;
import com.avance.marketflow.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return redirectByRole(user);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, @RequestParam Role role, HttpSession session, Model model) {
        return authService.login(email, password, role)
                .map(user -> {
                    session.setAttribute("user", user);
                    return redirectByRole(user);
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Credenciales incorrectas o cuenta inactiva.");
                    return "login";
                });
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return redirectByRole(user);
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam Role role, HttpSession session) {
        User user = authService.register(name, email, password, role);
        session.setAttribute("user", user);
        return redirectByRole(user);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private String redirectByRole(User user) {
        if (user.getRole() == Role.SELLER) return "redirect:/seller";
        if (user.getRole() == Role.ADMIN) return "redirect:/admin";
        if (user.getRole() == Role.EMPLOYEE) return "redirect:/dashboard";
        return "redirect:/";
    }
}
