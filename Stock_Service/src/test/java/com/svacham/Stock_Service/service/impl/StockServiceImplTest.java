package com.svacham.Stock_Service.service.impl;

import com.svacham.Stock_Service.dto.StockSummaryDto;
import com.svacham.Stock_Service.entity.Stock;
import com.svacham.Stock_Service.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private com.svacham.Stock_Service.service.impl.StockServiceImpl stockService;

    @Test
    void addStock_computesRemainingValueAndStatusAndSaves() {
        Stock s = new Stock();
        s.setItemName("Item A");
        s.setTotalStock(100.0);
        s.setUsedStock(30.0);
        s.setPurchasePricePerUnit(10.0);

        when(stockRepository.save(any(Stock.class))).thenAnswer(i -> i.getArgument(0));

        Stock saved = stockService.addStock(s);

        assertEquals(70.0, saved.getRemainingStock());
        assertEquals(700.0, saved.getStockValue());
        assertNotNull(saved.getCreatedAt());
        assertEquals("AVAILABLE", saved.getStatus());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void updateStock_existing_updatesComputedFieldsAndSaves() {
        Long id = 1L;
        Stock existing = new Stock();
        existing.setTotalStock(50.0);
        existing.setUsedStock(10.0);
        existing.setPurchasePricePerUnit(5.0);

        when(stockRepository.findById(id)).thenReturn(Optional.of(existing));
        when(stockRepository.save(any(Stock.class))).thenAnswer(i -> i.getArgument(0));

        Stock update = new Stock();
        update.setItemName("Updated");
        update.setTotalStock(20.0);
        update.setUsedStock(20.0);
        update.setPurchasePricePerUnit(2.0);

        Stock res = stockService.updateStock(id, update);

        assertEquals(0.0, res.getRemainingStock());
        assertEquals(0.0, res.getStockValue());
        assertEquals("OUT_OF_STOCK", res.getStatus());
        assertEquals("Updated", res.getItemName());
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void getAllStock_returnsList() {
        Stock a = new Stock(); a.setItemName("A");
        Stock b = new Stock(); b.setItemName("B");
        when(stockRepository.findAll()).thenReturn(List.of(a, b));

        List<Stock> list = stockService.getAllStock();

        assertEquals(2, list.size());
    }

    @Test
    void getStockById_notFound_throws() {
        when(stockRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> stockService.getStockById(99L));
        assertTrue(ex.getMessage().contains("Stock Not Found"));
    }

    @Test
    void deleteStock_callsRepository() {
        doNothing().when(stockRepository).deleteById(5L);

        stockService.deleteStock(5L);

        verify(stockRepository, times(1)).deleteById(5L);
    }

    @Test
    void getStockSummary_usesRepositoryAggregates() {
        when(stockRepository.sumRemainingStock()).thenReturn(150.0);
        when(stockRepository.sumUsedStock()).thenReturn(50.0);
        when(stockRepository.sumStockValue()).thenReturn(3000.0);
        when(stockRepository.countByStatus("LOW_STOCK")).thenReturn(3L);
        when(stockRepository.countByStatus("OUT_OF_STOCK")).thenReturn(1L);

        StockSummaryDto dto = stockService.getStockSummary();

        assertEquals(150.0, dto.getTotalStockAvailable());
        assertEquals(50.0, dto.getTotalUsedStock());
        assertEquals(3000.0, dto.getTotalInventoryValue());
        assertEquals(3L, dto.getLowStockItems());
        assertEquals(1L, dto.getOutOfStockItems());
    }

    @Test
    void getByCategory_andByStatus_callRepository() {
        Stock s = new Stock(); s.setItemCategory("Cat1");
        when(stockRepository.findByItemCategory("Cat1")).thenReturn(List.of(s));
        when(stockRepository.findByStatus("AVAILABLE")).thenReturn(List.of(s));

        var byCat = stockService.getByCategory("Cat1");
        var byStatus = stockService.getByStatus("AVAILABLE");

        assertEquals(1, byCat.size());
        assertEquals(1, byStatus.size());
    }
}

