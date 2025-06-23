package com.example.finsage;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.finsage.model.Expense;

@Service
public class DashboardService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private UserRepository userRepo;

    public Map<String, Object> getDashboardSummary(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();

        double totalIncome = user.getIncome();
        List<Expense> expenses = expenseRepo.findByUser(user);

        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double savings = totalIncome - totalExpense;

        // Monthly breakdown for chart
        Map<String, Double> monthly = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Expense e : expenses) {
            String key = e.getDate().format(formatter);
            monthly.put(key, monthly.getOrDefault(key, 0.0) + e.getAmount());
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("savings", savings);
        summary.put("monthlyExpenses", monthly);

        return summary;
    }
}
