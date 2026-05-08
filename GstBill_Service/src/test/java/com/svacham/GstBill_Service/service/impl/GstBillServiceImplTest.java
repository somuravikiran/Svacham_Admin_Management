package com.svacham.GstBill_Service.service.impl;

import com.svacham.GstBill_Service.dto.GstBillSummaryDto;
import com.svacham.GstBill_Service.entity.GstBill;
import com.svacham.GstBill_Service.repository.GstBillRepository;
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
class GstBillServiceImplTest {

    @Mock
    private GstBillRepository gstBillRepository;

    @InjectMocks
    private GstBillServiceImpl gstBillService;

    @Test
    void createBill_setsCreatedAtAndSaves() {
        GstBill bill = new GstBill();
        bill.setVendorName("Vendor A");
        bill.setBillAmount(1000.0);

        when(gstBillRepository.save(any(GstBill.class))).thenAnswer(i -> i.getArgument(0));

        GstBill saved = gstBillService.createBill(bill);

        assertNotNull(saved.getCreatedAt());
        assertEquals("Vendor A", saved.getVendorName());
        verify(gstBillRepository, times(1)).save(saved);
    }

    @Test
    void updateBill_existing_updatesAndSaves() {
        Long id = 1L;
        GstBill existing = new GstBill();
        existing.setVendorName("Old Vendor");
        existing.setBillAmount(500.0);

        when(gstBillRepository.findById(id)).thenReturn(Optional.of(existing));
        when(gstBillRepository.save(any(GstBill.class))).thenAnswer(i -> i.getArgument(0));

        GstBill update = new GstBill();
        update.setVendorName("New Vendor");
        update.setBillAmount(1500.0);
        update.setGstAmount(270.0);
        update.setTotalAmount(1770.0);
        update.setStatus("PAID");

        GstBill result = gstBillService.updateBill(id, update);

        assertEquals("New Vendor", result.getVendorName());
        assertEquals(1500.0, result.getBillAmount());
        assertEquals(270.0, result.getGstAmount());
        assertEquals(1770.0, result.getTotalAmount());
        assertEquals("PAID", result.getStatus());
        verify(gstBillRepository, times(1)).save(result);
    }

    @Test
    void getBillById_found_returnsBill() {
        GstBill bill = new GstBill();
        bill.setBillNumber("B-100");
        when(gstBillRepository.findById(2L)).thenReturn(Optional.of(bill));

        GstBill res = gstBillService.getBillById(2L);

        assertEquals("B-100", res.getBillNumber());
    }

    @Test
    void getBillById_notFound_throws() {
        when(gstBillRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> gstBillService.getBillById(99L));
        assertTrue(ex.getMessage().contains("GST Bill not found"));
    }

    @Test
    void getAllBills_returnsList() {
        GstBill b1 = new GstBill(); b1.setBillNumber("B1");
        GstBill b2 = new GstBill(); b2.setBillNumber("B2");
        when(gstBillRepository.findAll()).thenReturn(List.of(b1, b2));

        List<GstBill> all = gstBillService.getAllBills();

        assertEquals(2, all.size());
    }

    @Test
    void deleteBill_callsRepository() {
        doNothing().when(gstBillRepository).deleteById(5L);

        gstBillService.deleteBill(5L);

        verify(gstBillRepository, times(1)).deleteById(5L);
    }

    @Test
    void getSummary_computesAggregates() {
        GstBill p = new GstBill();
        p.setBillAmount(1000.0);
        p.setGstAmount(180.0);
        p.setTotalAmount(1180.0);
        p.setStatus("PAID");

        GstBill pend = new GstBill();
        pend.setBillAmount(500.0);
        pend.setGstAmount(90.0);
        pend.setTotalAmount(590.0);
        pend.setStatus("PENDING");

        GstBill part = new GstBill();
        part.setBillAmount(200.0);
        part.setGstAmount(36.0);
        part.setTotalAmount(236.0);
        part.setStatus("PARTIAL");

        when(gstBillRepository.findAll()).thenReturn(List.of(p, pend, part));

        GstBillSummaryDto dto = gstBillService.getSummary();

        assertEquals(3L, dto.getTotalBills());
        assertEquals(1700.0, dto.getTotalBillAmount());
        assertEquals(306.0, dto.getTotalGstAmount());
        assertEquals(2006.0, dto.getTotalFinalAmount());
        assertEquals(1L, dto.getPaidBills());
        assertEquals(1L, dto.getPendingBills());
        assertEquals(1L, dto.getPartialBills());
    }
}


