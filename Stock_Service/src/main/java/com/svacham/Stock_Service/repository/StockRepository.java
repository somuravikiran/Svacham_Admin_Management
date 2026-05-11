package com.svacham.Stock_Service.repository;
import com.svacham.Stock_Service.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends MongoRepository<Stock, String> {

    Optional<Stock> findByItemName(String itemName);

    List<Stock> findByItemCategory(String itemCategory);

    List<Stock> findByStatus(String status);

    Long countByStatus(String status);
}