package com.svacham.Order_Service.service;

import com.svacham.Order_Service.dto.AuthValidationResponseDto;
import com.svacham.Order_Service.dto.OrderRequestDto;
import com.svacham.Order_Service.dto.StateOrderSummaryDto;
import com.svacham.Order_Service.entity.Order;

import java.util.List;

public interface OrderService {

    AuthValidationResponseDto validateToken(String token);

    Order createOrder(String token, OrderRequestDto requestDto);

    Order createOrder(Order order);

    List<Order> getAllOrders();

    Order getOrderById(String id);

    Order updateOrder(String id, Order order);

    void deleteOrder(String id);

    StateOrderSummaryDto getOrderSummary();

}