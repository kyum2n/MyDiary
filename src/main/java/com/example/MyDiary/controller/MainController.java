package com.example.mydiary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.web.csrf.CsrfToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainPage(HttpServletRequest request, Model model) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
        return "main"; // → templates/main.html
    }
}