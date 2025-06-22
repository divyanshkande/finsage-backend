package com.example.finsage;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getExpenseSummary(@RequestParam String email) {
        Map<String, Object> summary = expenseService.getExpenseSummary(email);

        if (summary.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(summary);
        }

        return ResponseEntity.ok(summary);
    }
    
    @PutMapping("/update-income")
    public ResponseEntity<String> updateIncome(
            @RequestParam String email,
            @RequestParam double income
    ) {
        String result = expenseService.updateIncome(email, income);
        if (result.equals("User not found")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }


}