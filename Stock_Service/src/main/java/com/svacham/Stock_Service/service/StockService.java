package com.svacham.Stock_Service.service;


import com.svacham.Stock_Service.dto.AuthValidationResponseDto;
import com.svacham.Stock_Service.dto.StockSummaryDto;
import com.svacham.Stock_Service.entity.Stock;

import java.util.List;

public interface StockService {

    Boolean checkStock(String itemName);

    String reduceStock(String itemName, Integer qty);

    AuthValidationResponseDto validateToken(String token);

    Stock addStock(Stock stock);

    Stock updateStock(String id, Stock stock);

    List<Stock> getAllStock();

    Stock getStockById(String id);

    void deleteStock(String id);

    StockSummaryDto getStockSummary();

    List<Stock> getByCategory(String itemCategory);

    List<Stock> getByStatus(String status);
}