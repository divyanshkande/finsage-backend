package com.example.finsage;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.finsage.model.Expense;
@Service
public class ExpenseService {
	@Autowired
    private ExpenseRepository expenseRepo;

    public Expense addExpense(Expense expense) {
        return expenseRepo.save(expense);
    }

    public List<Expense> getTodayExpenses(User user) {
        return expenseRepo.findByUserAndDate(user, LocalDate.now());
    }

    public List<Expense> getAllExpenses(User user) {
        return expenseRepo.findByUser(user);
    }

    public void deleteExpense(Long id) {
        expenseRepo.deleteById(id);
    }
}