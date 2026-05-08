package com.svacham.Order_Service.service.impl;
import com.svacham.Order_Service.dto.*;
import com.svacham.Order_Service.entity.Order;
import com.svacham.Order_Service.entity.OrderItem;
import com.svacham.Order_Service.external.ClientServiceFeign;
import com.svacham.Order_Service.external.StockClientService;
import com.svacham.Order_Service.repository.OrderRepository;
import com.svacham.Order_Service.service.OrderService;
import lombok.RequiredArgsConstructor;
import com.svacham.Order_Service.dto.ClientResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    private final ClientServiceFeign clientServiceFeign;

    private final StockClientService stockClientService;
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
    public Order createOrder(String token, OrderRequestDto requestDto) {

        // STEP 1: CLIENT VALIDATION (your existing logic stays same)
        ClientResponseDto client;

        try {
            client = clientServiceFeign.getClientById(token, requestDto.getClientId());
        } catch (Exception e) {
            throw new RuntimeException("Client not found in CLIENT-SERVICE");
        }

        Order order = new Order();
        order.setClientId(client.getId());
        order.setClientName(client.getClientName());
        order.setOrderDate(requestDto.getOrderDate());
        order.setDeliveryDate(requestDto.getDeliveryDate());
        order.setPaidAmount(requestDto.getPaidAmount());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemDto itemDto : requestDto.getItems()) {

            // STEP 2: STOCK CHECK
            Boolean available = stockClientService.checkStock(itemDto.getPickleType());

            if (!available) {
                throw new RuntimeException("Stock not available for: " + itemDto.getPickleType());
            }

            OrderItem item = new OrderItem();
            item.setPickleType(itemDto.getPickleType());
            item.setPackSizeKg(itemDto.getPackSizeKg());
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(itemDto.getUnitPrice());

            double subTotal = itemDto.getQuantity() * itemDto.getUnitPrice();
            item.setSubTotal(subTotal);

            item.setOrder(order);

            orderItems.add(item);
            totalAmount += subTotal;

            // STEP 3: REDUCE STOCK
            stockClientService.reduceStock(
                    itemDto.getPickleType(),
                    itemDto.getQuantity()
            );
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        if (order.getPaidAmount() == null) {
            order.setPaidAmount(0.0);
        }

        order.setPendingAmount(totalAmount - order.getPaidAmount());

        if (order.getPendingAmount() <= 0) {
            order.setOrderStatus("PAID");
        } else if (order.getPaidAmount() > 0) {
            order.setOrderStatus("PARTIAL");
        } else {
            order.setOrderStatus("PENDING");
        }

        return orderRepository.save(order);
    }
    @Override
    public Order createOrder(Order order) {
        // ensure items reference back to order and compute totals
        if (order.getItems() != null) {
            double total = 0.0;
            for (OrderItem item : order.getItems()) {
                if (item.getSubTotal() == null) {
                    double qty = item.getQuantity() != null ? item.getQuantity() : 0;
                    double price = item.getUnitPrice() != null ? item.getUnitPrice() : 0;
                    item.setSubTotal(qty * price);
                }
                total += item.getSubTotal();
                item.setOrder(order);
            }
            order.setTotalAmount(total);
        } else {
            if (order.getTotalAmount() == null) {
                order.setTotalAmount(0.0);
            }
        }

        if (order.getPaidAmount() == null) {
            order.setPaidAmount(0.0);
        }

        order.setPendingAmount(order.getTotalAmount() - order.getPaidAmount());

        if (order.getPendingAmount() == 0) {
            order.setOrderStatus("PAID");
        } else {
            order.setOrderStatus("PENDING");
        }

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        Order existing = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        existing.setClientId(order.getClientId());
        existing.setClientName(order.getClientName());
        existing.setOrderDate(order.getOrderDate());
        existing.setDeliveryDate(order.getDeliveryDate());
        existing.setPaidAmount(order.getPaidAmount());

        if (order.getItems() != null) {
            // set parent reference and recalculate totals
            double total = 0.0;
            for (OrderItem item : order.getItems()) {
                if (item.getSubTotal() == null) {
                    double qty = item.getQuantity() != null ? item.getQuantity() : 0;
                    double price = item.getUnitPrice() != null ? item.getUnitPrice() : 0;
                    item.setSubTotal(qty * price);
                }
                total += item.getSubTotal();
                item.setOrder(existing);
            }
            existing.setItems(order.getItems());
            existing.setTotalAmount(total);
        }

        if (existing.getPaidAmount() == null) {
            existing.setPaidAmount(0.0);
        }

        existing.setPendingAmount(existing.getTotalAmount() - existing.getPaidAmount());

        if (existing.getPendingAmount() == 0) {
            existing.setOrderStatus("PAID");
        } else {
            existing.setOrderStatus("PENDING");
        }

        return orderRepository.save(existing);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public StateOrderSummaryDto getOrderSummary() {

        List<Order> orders = orderRepository.findAll();

        double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        double totalPending = orders.stream().mapToDouble(Order::getPendingAmount).sum();
        long totalOrders = orders.size();

        return new StateOrderSummaryDto(totalRevenue,totalPending,totalOrders);
    }
}