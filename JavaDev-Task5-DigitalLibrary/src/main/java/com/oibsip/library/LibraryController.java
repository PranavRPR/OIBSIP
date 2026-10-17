package com.oibsip.library;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Controller
public class LibraryController {

    @Autowired
    private JdbcTemplate jdbc;

    // --- LOGIN ---
    @GetMapping("/")
    public String index() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        List<Map<String, Object>> users = jdbc.queryForList("SELECT * FROM users WHERE username=? AND password=?", username, password);
        if (users.isEmpty()) return "redirect:/?error=true";
        
        Map<String, Object> user = users.get(0);
        session.setAttribute("userId", user.get("id"));
        session.setAttribute("username", user.get("username"));
        session.setAttribute("role", user.get("role"));
        
        if ("ADMIN".equals(user.get("role"))) return "redirect:/admin";
        return "redirect:/user";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // --- USER DASHBOARD ---
    @GetMapping("/user")
    public String userDashboard(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/";
        Integer userId = (Integer) session.getAttribute("userId");
        
        List<Map<String, Object>> books = jdbc.queryForList("SELECT * FROM books");
        List<Map<String, Object>> issues = jdbc.queryForList(
            "SELECT i.*, b.title FROM issues i JOIN books b ON i.book_id = b.id WHERE i.user_id=? AND i.status='ISSUED'", userId);
        
        model.addAttribute("books", books);
        model.addAttribute("issues", issues);
        model.addAttribute("username", session.getAttribute("username"));
        return "user";
    }

    // --- USER ACTIONS ---
    @PostMapping("/user/issue")
    public String issueBook(@RequestParam Integer bookId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        Integer avail = jdbc.queryForObject("SELECT available FROM books WHERE id=?", Integer.class, bookId);
        
        if (avail > 0) {
            jdbc.update("UPDATE books SET available = available - 1 WHERE id=?", bookId);
            LocalDate issueDate = LocalDate.now();
            LocalDate dueDate = issueDate.plusDays(7);
            jdbc.update("INSERT INTO issues (user_id, book_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, 'ISSUED')",
                    userId, bookId, issueDate.toString(), dueDate.toString());
        }
        return "redirect:/user";
    }

    @PostMapping("/user/return")
    public String returnBook(@RequestParam Integer issueId, HttpSession session) {
        Map<String, Object> issue = jdbc.queryForMap("SELECT * FROM issues WHERE id=?", issueId);
        LocalDate dueDate = LocalDate.parse((String) issue.get("due_date"));
        LocalDate returnDate = LocalDate.now();
        
        long lateDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        double fine = lateDays > 0 ? lateDays * 5.0 : 0.0; // 5 per day fine
        
        jdbc.update("UPDATE issues SET return_date=?, fine=?, status='RETURNED' WHERE id=?", 
                returnDate.toString(), fine, issueId);
        jdbc.update("UPDATE books SET available = available + 1 WHERE id=?", issue.get("book_id"));
        
        return "redirect:/user";
    }

    // --- ADMIN DASHBOARD ---
    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/";
        
        List<Map<String, Object>> books = jdbc.queryForList("SELECT * FROM books");
        List<Map<String, Object>> issues = jdbc.queryForList(
            "SELECT i.*, b.title, u.username FROM issues i JOIN books b ON i.book_id = b.id JOIN users u ON i.user_id = u.id");
        
        model.addAttribute("books", books);
        model.addAttribute("issues", issues);
        return "admin";
    }

    // --- ADMIN ACTIONS ---
    @PostMapping("/admin/addBook")
    public String addBook(@RequestParam String title, @RequestParam String author, @RequestParam String category, @RequestParam Integer quantity) {
        jdbc.update("INSERT INTO books (title, author, category, quantity, available) VALUES (?, ?, ?, ?, ?)",
                title, author, category, quantity, quantity);
        return "redirect:/admin";
    }
}