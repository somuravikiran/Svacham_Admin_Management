package com.svacham.Order_Service.repository;

import com.svacham.Order_Service.dto.StateOrderSummaryDto;
import com.svacham.Order_Service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

