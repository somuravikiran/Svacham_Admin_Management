package com.svacham.Order_Service.repository;

import com.svacham.Order_Service.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface OrderRepository extends MongoRepository<Order, String> {
}

