package com.svacham.Stock_Service.controller;

import com.svacham.Stock_Service.dto.StockSummaryDto;
import com.svacham.Stock_Service.entity.Stock;
import com.svacham.Stock_Service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderValidationUtil.validateToken;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/add")
    public Stock addStock(@RequestHeader("Authorization") String token,@RequestBody Stock stock) {
        stockService.validateToken(token);
        return stockService.addStock(stock);
    }

    @PutMapping("/update/{id}")
    public Stock updateStock(@RequestHeader("Authorization") String token,@PathVariable Long id, @RequestBody Stock stock) {
        stockService.validateToken(token);
//        validateToken(token);
        return stockService.updateStock(id, stock);
    }

    @GetMapping("/all")
    public List<Stock> getAllStock(@RequestHeader("Authorization") String token) {
        stockService.validateToken(token);
//        validateToken(token);
        return stockService.getAllStock();
    }

    @GetMapping("/{id}")
    public Stock getStockById(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        stockService.validateToken(token);
//        validateToken(token);
        return stockService.getStockById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStock(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        stockService.validateToken(token);
//        validateToken(token);
        stockService.deleteStock(id);
        return "Stock Deleted Successfully";
    }

    @GetMapping("/summary")
    public StockSummaryDto getStockSummary(@RequestHeader("Authorization") String token) {
        stockService.validateToken(token);
//        validateToken(token);
        return stockService.getStockSummary();
    }

    @GetMapping("/category/{itemCategory}")
    public List<Stock> getByCategory(@RequestHeader("Authorization") String token,@PathVariable String itemCategory) {
        stockService.validateToken(token);
//        validateToken(token);
        return stockService.getByCategory(itemCategory);
    }
    @GetMapping("/check/{item}")
    public Boolean checkStock(@PathVariable String item) {
        return stockService.checkStock(item);
    }

    @PutMapping("/reduce/{item}/{qty}")
    public String reduceStock(@PathVariable String item,
                              @PathVariable Integer qty) {
        return stockService.reduceStock(item, qty);
    }

    @GetMapping("/status/{status}")
    public List<Stock> getByStatus(@RequestHeader("Authorization") String token,@PathVariable String status) {
        stockService.validateToken(token);
        return stockService.getByStatus(status);
    }
}