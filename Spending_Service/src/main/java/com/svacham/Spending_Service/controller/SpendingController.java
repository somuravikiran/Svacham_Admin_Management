package com.svacham.Spending_Service.controller;
import com.svacham.Spending_Service.entity.Spending;
import com.svacham.Spending_Service.service.SpendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spending")
@RequiredArgsConstructor
public class SpendingController {

    private final SpendingService spendingService;

    @PostMapping("/add")
    public Spending addSpending(@RequestHeader("Authorization") String token,
                                @RequestBody Spending spending) {

        spendingService.validateToken(token);

        return spendingService.addSpending(spending);
    }

    @GetMapping("/all")
    public List<Spending> getAllSpendings(@RequestHeader("Authorization") String token) {

        spendingService.validateToken(token);

        return spendingService.getAllSpendings();
    }

    @GetMapping("/{id}")
    public Spending getSpendingById(@RequestHeader("Authorization") String token,
                                    @PathVariable String id) {

        spendingService.validateToken(token);

        return spendingService.getSpendingById(id);
    }

    @PutMapping("/update/{id}")
    public Spending updateSpending(@RequestHeader("Authorization") String token,
                                   @PathVariable String id,
                                   @RequestBody Spending spending) {

        spendingService.validateToken(token);

        return spendingService.updateSpending(id, spending);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSpending(@RequestHeader("Authorization") String token,
                                 @PathVariable String id) {

        spendingService.validateToken(token);

        spendingService.deleteSpending(id);

        return "Spending deleted successfully";
    }

    @GetMapping("/status/{status}")
    public List<Spending> getByStatus(@RequestHeader("Authorization") String token,
                                      @PathVariable String status) {

        spendingService.validateToken(token);

        return spendingService.getByStatus(status);
    }

    @GetMapping("/summary")
    public Map<String, Object> getSpendingSummary(@RequestHeader("Authorization") String token) {

        spendingService.validateToken(token);

        return spendingService.getSpendingSummary();
    }
}