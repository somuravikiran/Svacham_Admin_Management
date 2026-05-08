package com.svacham.Stock_Service.repository;
import com.svacham.Stock_Service.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT COALESCE(SUM(s.remainingStock),0) FROM Stock s")
    Double sumRemainingStock();

    @Query("SELECT COALESCE(SUM(s.usedStock),0) FROM Stock s")
    Double sumUsedStock();

    @Query("SELECT COALESCE(SUM(s.stockValue),0) FROM Stock s")
    Double sumStockValue();

    Optional<Stock> findByItemName(String itemName);

    Long countByStatus(String status);

    List<Stock> findByItemCategory(String itemCategory);

    List<Stock> findByStatus(String status);
}