package com.example.finsage;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finsage.model.Expense;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/add")
    public ResponseEntity<?> addExpense(@RequestBody Expense expense, Authentication authentication) {
        String email = authentication.getName(); // from JWT
        User user = userRepo.findByEmail(email).orElseThrow();
        expense.setUser(user);
        return ResponseEntity.ok(expenseService.addExpense(expense));
    }

    @GetMapping("/today")
    public ResponseEntity<List<Expense>> getTodayExpenses(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(expenseService.getTodayExpenses(user));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Expense>> getAllExpenses(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepo.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(expenseService.getAllExpenses(user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Deleted");
    }
}