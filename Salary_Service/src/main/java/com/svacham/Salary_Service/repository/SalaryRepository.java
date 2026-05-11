package com.svacham.Salary_Service.repository;

import com.svacham.Salary_Service.entity.Salary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalaryRepository extends MongoRepository<Salary, String> {

    List<Salary> findByStatus(String status);
}