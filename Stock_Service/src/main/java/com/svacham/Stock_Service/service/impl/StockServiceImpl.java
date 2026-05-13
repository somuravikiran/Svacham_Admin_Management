package com.svacham.Stock_Service.service.impl;

import com.svacham.Stock_Service.dto.AuthValidationResponseDto;
import com.svacham.Stock_Service.dto.StockSummaryDto;
import com.svacham.Stock_Service.entity.Stock;
import com.svacham.Stock_Service.repository.StockRepository;
import com.svacham.Stock_Service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private final WebClient.Builder webClientBuilder;

    @Override
    public AuthValidationResponseDto validateToken(String token) {

        try {

            return webClientBuilder.build()
                    .get()
                    .uri("https://svacham-admin-management-1.onrender.com/auth/validate")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(AuthValidationResponseDto.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("AUTH-SERVICE unavailable : " + e.getMessage());
        }
    }

    @Override
    public Stock addStock(Stock stock) {

        stock.setRemainingStock(
                stock.getTotalStock() - stock.getUsedStock()
        );

        stock.setStockValue(
                stock.getRemainingStock() * stock.getPurchasePricePerUnit()
        );

        stock.setCreatedAt(LocalDateTime.now());

        stock.setStatus(
                calculateStatus(stock.getRemainingStock())
        );

        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(String id, Stock stock) {

        Stock existing = stockRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Stock Not Found"));

        existing.setItemName(stock.getItemName());
        existing.setQuantity(stock.getQuantity());
        existing.setItemCategory(stock.getItemCategory());
        existing.setStockUnit(stock.getStockUnit());
        existing.setTotalStock(stock.getTotalStock());
        existing.setUsedStock(stock.getUsedStock());
        existing.setPurchasePricePerUnit(stock.getPurchasePricePerUnit());
        existing.setSellingPricePerUnit(stock.getSellingPricePerUnit());
        existing.setSupplierName(stock.getSupplierName());
        existing.setStockAddedDate(stock.getStockAddedDate());
        existing.setNotes(stock.getNotes());

        existing.setRemainingStock(
                stock.getTotalStock() - stock.getUsedStock()
        );

        existing.setStockValue(
                existing.getRemainingStock() *
                        stock.getPurchasePricePerUnit()
        );

        existing.setStatus(
                calculateStatus(existing.getRemainingStock())
        );

        return stockRepository.save(existing);
    }

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Stock getStockById(String id) {

        return stockRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Stock Not Found"));
    }

    @Override
    public void deleteStock(String id) {
        stockRepository.deleteById(id);
    }

    @Override
    public StockSummaryDto getStockSummary() {

        List<Stock> stocks = stockRepository.findAll();

        double totalStockAvailable = stocks.stream()
                .mapToDouble(Stock::getRemainingStock)
                .sum();

        double totalUsedStock = stocks.stream()
                .mapToDouble(Stock::getUsedStock)
                .sum();

        double totalInventoryValue = stocks.stream()
                .mapToDouble(Stock::getStockValue)
                .sum();

        long lowStockItems = stocks.stream()
                .filter(s -> "LOW_STOCK".equalsIgnoreCase(s.getStatus()))
                .count();

        long outOfStockItems = stocks.stream()
                .filter(s -> "OUT_OF_STOCK".equalsIgnoreCase(s.getStatus()))
                .count();

        return StockSummaryDto.builder()
                .totalStockAvailable(totalStockAvailable)
                .totalUsedStock(totalUsedStock)
                .totalInventoryValue(totalInventoryValue)
                .lowStockItems(lowStockItems)
                .outOfStockItems(outOfStockItems)
                .build();
    }

    @Override
    public List<Stock> getByCategory(String itemCategory) {
        return stockRepository.findByItemCategory(itemCategory);
    }

    @Override
    public List<Stock> getByStatus(String status) {
        return stockRepository.findByStatus(status);
    }

    @Override
    public Boolean checkStock(String itemName) {

        return stockRepository.findByItemName(itemName)
                .map(stock -> stock.getQuantity() > 0)
                .orElse(false);
    }

    @Override
    public String reduceStock(String itemName, Integer qty) {

        Stock stock = stockRepository.findByItemName(itemName)
                .orElseThrow(() ->
                        new RuntimeException("Item not found"));

        if (stock.getQuantity() < qty) {
            throw new RuntimeException("Insufficient stock");
        }

        stock.setQuantity(stock.getQuantity() - qty);

        stockRepository.save(stock);

        return "Stock updated successfully";
    }

    private String calculateStatus(Double remainingStock) {

        if (remainingStock == 0) {
            return "OUT_OF_STOCK";
        }
        else if (remainingStock <= 10) {
            return "LOW_STOCK";
        }
        else {
            return "AVAILABLE";
        }
    }
}