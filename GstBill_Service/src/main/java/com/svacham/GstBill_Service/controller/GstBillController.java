package com.svacham.GstBill_Service.controller;
import com.svacham.GstBill_Service.entity.GstBill;
import com.svacham.GstBill_Service.service.GstBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// use service-based token validation instead of a static netty utility

@RestController
@RequestMapping("/api/gst-bills")
@RequiredArgsConstructor
public class GstBillController {

    private final GstBillService gstBillService;

    @PostMapping("/create")
    public ResponseEntity<?> createBill(@RequestHeader("Authorization") String token,@RequestBody GstBill gstBill) {
        gstBillService.validateToken(token);
        return ResponseEntity.ok(Map.of(
                "message", "GST Bill created successfully",
                "data", gstBillService.createBill(gstBill)
        ));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBill(@RequestHeader("Authorization") String token,@PathVariable String id, @RequestBody GstBill gstBill) {
        gstBillService.validateToken(token);
        return ResponseEntity.ok(Map.of(
                "message", "GST Bill updated successfully",
                "data", gstBillService.updateBill(id, gstBill)
        ));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getBillById(@RequestHeader("Authorization") String token,@PathVariable String id) {
        gstBillService.validateToken(token);
        return ResponseEntity.ok(Map.of(
                "message", "GST Bill fetched successfully",
                "data", gstBillService.getBillById(id)
        ));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBills(@RequestHeader("Authorization") String token) {
        gstBillService.validateToken(token);
        return ResponseEntity.ok(Map.of(
                "message", "All GST Bills fetched successfully",
                "data", gstBillService.getAllBills()
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBill(@RequestHeader("Authorization") String token,@PathVariable String id) {
        gstBillService.validateToken(token);
        gstBillService.deleteBill(id);
        return ResponseEntity.ok(Map.of(
                "message", "GST Bill deleted successfully"
        ));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(@RequestHeader("Authorization") String token) {
        gstBillService.validateToken(token);
        return ResponseEntity.ok(Map.of(
                "message", "GST Summary fetched successfully",
                "data", gstBillService.getSummary()
        ));
    }
}