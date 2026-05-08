package com.svacham.Order_Service.controller;

import com.svacham.Order_Service.dto.OrderRequestDto;
import com.svacham.Order_Service.dto.StateOrderSummaryDto;
import com.svacham.Order_Service.entity.Order;
import com.svacham.Order_Service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderValidationUtil.validateToken;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/add")
    public Order createOrder(@RequestHeader("Authorization") String token,@RequestBody OrderRequestDto requestDto){
        orderService.validateToken(token);
        return orderService.createOrder(token,requestDto);
    }

    @GetMapping("/all")
    public List<Order> getAllOrders(@RequestHeader("Authorization") String token){
        orderService.validateToken(token);
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@RequestHeader("Authorization") String token,@PathVariable Long id){
        orderService.validateToken(token);
//        validateToken(token);
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@RequestHeader("Authorization") String token,@PathVariable Long id){
        orderService.validateToken(token);
//        validateToken(token);
        orderService.deleteOrder(id);
        return "Order deleted successfully";
    }

    @GetMapping("/summary")
    public StateOrderSummaryDto getSummary(@RequestHeader("Authorization") String token){
        orderService.validateToken(token);
//        validateToken(token);
        return orderService.getOrderSummary();
    }
}