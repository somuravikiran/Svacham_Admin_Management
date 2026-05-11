package com.svacham.Order_Service.service.impl;

import com.svacham.Order_Service.dto.OrderItemDto;
import com.svacham.Order_Service.dto.OrderRequestDto;
import com.svacham.Order_Service.dto.StateOrderSummaryDto;
import com.svacham.Order_Service.entity.Order;
import com.svacham.Order_Service.entity.OrderItem;
import com.svacham.Order_Service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_fromRequest_calculatesTotalsAndSaves() {
        // adapt test to call createOrder(Order) since service expects entity
        OrderItem item1 = new OrderItem();
        item1.setQuantity(2);
        item1.setUnitPrice(100.0);

        Order order = new Order();
        order.setClientId("1");
        order.setClientName("Client A");
        order.setItems(List.of(item1));

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order saved = orderService.createOrder(order);

        assertNotNull(saved);
        assertEquals(200.0, saved.getTotalAmount());
        assertEquals(200.0, saved.getPendingAmount());
        assertEquals("PENDING", saved.getOrderStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_fromOrder_setsSubtotalsAndStatus() {
        Order order = new Order();
        order.setPaidAmount(0.0);

        OrderItem item = new OrderItem();
        item.setQuantity(3);
        item.setUnitPrice(50.0);
        // no subTotal set intentionally

        order.setItems(new ArrayList<>(List.of(item)));

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.createOrder(order);

        assertNotNull(result.getItems());
        assertEquals(150.0, result.getTotalAmount());
        assertEquals(150.0, result.getPendingAmount());
        assertEquals("PENDING", result.getOrderStatus());
    }

    @Test
    void updateOrder_updatesExistingOrderAndRecalculates() {
        Order existing = new Order();
        existing.setTotalAmount(100.0);
        existing.setPaidAmount(100.0);
        existing.setPendingAmount(0.0);

        when(orderRepository.findById("1")).thenReturn(Optional.of(existing));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order update = new Order();
        update.setPaidAmount(50.0);
        OrderItem item = new OrderItem();
        item.setQuantity(2);
        item.setUnitPrice(60.0);
        update.setItems(new ArrayList<>(List.of(item)));

        Order res = orderService.updateOrder("1", update);

        assertEquals(120.0, res.getTotalAmount());
        assertEquals(50.0, res.getPaidAmount());
        assertEquals(70.0, res.getPendingAmount());
        assertEquals("PENDING", res.getOrderStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrderSummary_computesAggregates() {
        Order o1 = new Order(); o1.setTotalAmount(100.0); o1.setPendingAmount(10.0);
        Order o2 = new Order(); o2.setTotalAmount(200.0); o2.setPendingAmount(20.0);

        when(orderRepository.findAll()).thenReturn(List.of(o1, o2));

        StateOrderSummaryDto dto = orderService.getOrderSummary();

        assertEquals(300.0, dto.getTotalRevenue());
        assertEquals(30.0, dto.getTotalPending());
        assertEquals(2L, dto.getTotalOrders());
    }

    @Test
    void getOrderById_notFound_throws() {
        when(orderRepository.findById("99")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.getOrderById("99"));
        assertEquals("Order not found", ex.getMessage());
    }

    @Test
    void deleteOrder_callsRepository() {
        doNothing().when(orderRepository).deleteById("5");

        orderService.deleteOrder("5");

        verify(orderRepository, times(1)).deleteById("5");
    }
}


