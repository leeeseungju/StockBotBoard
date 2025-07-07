package com.example.stockboard.controller;

import com.example.stockboard.domain.User;
import com.example.stockboard.service.SearchLogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {

    @Autowired
    private SearchLogService searchLogService;

    @GetMapping("/logs")
    public String viewLogs(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getUserType())) {
            return "redirect:/";
        }

        model.addAttribute("logs", searchLogService.getAllLogs());
        return "logs";
    }
}
