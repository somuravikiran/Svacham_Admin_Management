package com.svacham.Spending_Service.repository;

import com.svacham.Spending_Service.entity.Spending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, Long> {

    List<Spending> findByStatus(String status);

    List<Spending> findByExpenseCategory(String expenseCategory);
}
