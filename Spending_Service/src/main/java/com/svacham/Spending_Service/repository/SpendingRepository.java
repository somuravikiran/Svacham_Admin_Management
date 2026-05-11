package com.svacham.Spending_Service.repository;

import com.svacham.Spending_Service.entity.Spending;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpendingRepository extends MongoRepository<Spending, String> {

    List<Spending> findByStatus(String status);

    List<Spending> findByExpenseCategory(String expenseCategory);
}
