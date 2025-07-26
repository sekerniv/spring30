package com.example;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginForm() {
        return "login";
    }

    @PostMapping
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        // Very basic authentication (match user in SQLite)
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:sqlite.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                session.setAttribute("username", username);
                return "redirect:/";
            } else {
                model.addAttribute("message", "Wrong details");
                return "login";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("message", "Database error");
            return "login";
        }
    }
}
