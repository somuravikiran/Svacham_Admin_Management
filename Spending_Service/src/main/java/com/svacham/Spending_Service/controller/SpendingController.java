package com.svacham.Spending_Service.controller;
import com.svacham.Spending_Service.entity.Spending;
import com.svacham.Spending_Service.service.SpendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderValidationUtil.validateToken;

@RestController
@RequestMapping("/api/spending")
@RequiredArgsConstructor
public class SpendingController {

    private final SpendingService spendingService;

    @PostMapping("/add")
    public Spending addSpending(@RequestHeader("Authorization") String token,@RequestBody Spending spending) {
        validateToken(token);
        return spendingService.addSpending(spending);
    }

    @GetMapping("/all")
    public List<Spending> getAllSpendings(@RequestHeader("Authorization") String token) {
        validateToken(token);
        return spendingService.getAllSpendings();
    }

    @GetMapping("/{id}")
    public Spending getSpendingById(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        validateToken(token);
        return spendingService.getSpendingById(id);
    }

    @PutMapping("/update/{id}")
    public Spending updateSpending(@RequestHeader("Authorization") String token,@PathVariable Long id,
                                   @RequestBody Spending spending) {
        validateToken(token);
        return spendingService.updateSpending(id, spending);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSpending(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        validateToken(token);
        spendingService.deleteSpending(id);
        return "Spending deleted successfully";
    }

    @GetMapping("/status/{status}")
    public List<Spending> getByStatus(@RequestHeader("Authorization") String token,@PathVariable String status) {
        validateToken(token);
        return spendingService.getByStatus(status);
    }

    @GetMapping("/summary")
    public Map<String, Object> getSpendingSummary(@RequestHeader("Authorization") String token) {
        validateToken(token);
        return spendingService.getSpendingSummary();
    }
}