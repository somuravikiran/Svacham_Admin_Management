package com.svacham.Order_Service.service;

import com.svacham.Order_Service.dto.AuthValidationResponseDto;
import com.svacham.Order_Service.dto.OrderRequestDto;
import com.svacham.Order_Service.dto.StateOrderSummaryDto;
import com.svacham.Order_Service.entity.Order;

import java.util.List;

public interface OrderService { Order createOrder(Order order);

    AuthValidationResponseDto validateToken(String token);

    Order createOrder(String token,OrderRequestDto requestDto);

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order updateOrder(Long id, Order order);

    void deleteOrder(Long id);

    StateOrderSummaryDto getOrderSummary();
}