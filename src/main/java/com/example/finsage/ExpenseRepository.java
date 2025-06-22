package com.example.finsage;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finsage.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
	List<Expense> findByUserAndDate(User user, LocalDate date);
    List<Expense> findByUser(User user);
    List<Expense> findByUserEmail(String email);

}
