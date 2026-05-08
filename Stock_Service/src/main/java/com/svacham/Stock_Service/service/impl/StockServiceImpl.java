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

    public AuthValidationResponseDto validateToken(String token) {
        try {
//            System.out.println("STEP 3 : CALLING AUTH-SERVICE");

            AuthValidationResponseDto response = webClientBuilder.build()
                    .get()
                    .uri("http://AUTH-SERVICE/auth/validate")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(AuthValidationResponseDto.class)
                    .block();

//            System.out.println("STEP 4 : AUTH RESPONSE = " + response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("AUTH-SERVICE is unavailable : " + e.getMessage());
        }
    }

    @Override
    public Stock addStock(Stock stock) {

        stock.setRemainingStock(stock.getTotalStock() - stock.getUsedStock());
        stock.setStockValue(stock.getRemainingStock() * stock.getPurchasePricePerUnit());
        stock.setCreatedAt(LocalDateTime.now());
        stock.setStatus(calculateStatus(stock.getRemainingStock()));

        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(Long id, Stock stock) {

        Stock existing = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Not Found With Id : " + id));

        existing.setItemName(stock.getItemName());
        existing.setItemCategory(stock.getItemCategory());
        existing.setStockUnit(stock.getStockUnit());
        existing.setTotalStock(stock.getTotalStock());
        existing.setUsedStock(stock.getUsedStock());
        existing.setPurchasePricePerUnit(stock.getPurchasePricePerUnit());
        existing.setSellingPricePerUnit(stock.getSellingPricePerUnit());
        existing.setSupplierName(stock.getSupplierName());
        existing.setStockAddedDate(stock.getStockAddedDate());
        existing.setNotes(stock.getNotes());

        existing.setRemainingStock(stock.getTotalStock() - stock.getUsedStock());
        existing.setStockValue(existing.getRemainingStock() * stock.getPurchasePricePerUnit());
        existing.setStatus(calculateStatus(existing.getRemainingStock()));

        return stockRepository.save(existing);
    }

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Not Found With Id : " + id));
    }

    @Override
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public StockSummaryDto getStockSummary() {
        return StockSummaryDto.builder()
                .totalStockAvailable(stockRepository.sumRemainingStock())
                .totalUsedStock(stockRepository.sumUsedStock())
                .totalInventoryValue(stockRepository.sumStockValue())
                .lowStockItems(stockRepository.countByStatus("LOW_STOCK"))
                .outOfStockItems(stockRepository.countByStatus("OUT_OF_STOCK"))
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
                .orElseThrow(() -> new RuntimeException("Item not found"));

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
        } else if (remainingStock <= 10) {
            return "LOW_STOCK";
        } else {
            return "AVAILABLE";
        }
    }
}