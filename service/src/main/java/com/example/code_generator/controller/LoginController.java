package com.example.code_generator.controller;

import com.example.code_generator.model.Click;
import com.example.code_generator.model.CodeHistory;
import com.example.code_generator.service.ClickService;
import com.example.code_generator.service.CodeServiceClient;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    private final List<CodeHistory> codeHistory = new ArrayList<>();

    private final CodeServiceClient codeServiceClient;

    final
    ClickService clickService;

    public LoginController(CodeServiceClient codeServiceClient, ClickService clickService) {
        this.codeServiceClient = codeServiceClient;
        this.clickService = clickService;
    }

    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String code, Model model) {
        if (codeServiceClient.validateCode(username, code)) {
            model.addAttribute("username", username);
            return "home";
        } else {
            model.addAttribute("error", "Invalid code. Please try again.");
            return "login";
        }
    }

    @PostMapping("/register")
    public String generateCode(@RequestParam String username, Model model) {
        String code = codeServiceClient.generateCode(username);
        codeHistory.add(new CodeHistory(username, code, LocalDateTime.now()));
        model.addAttribute("username", username);
        model.addAttribute("code", code);
        model.addAttribute("codeHistory", codeHistory);
        return "showCode";
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            List<Click> clicks = clickService.getClicks(username);
            model.addAttribute("username", username);
            model.addAttribute("clicks", clicks);
            return "home";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/click")
    @ResponseBody
    public String recordClick(@RequestParam int x, @RequestParam int y, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            clickService.addClick(username, x, y);
            return "ok";
        }
        return "error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
