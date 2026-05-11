package com.svacham.GstBill_Service.service;

import com.svacham.GstBill_Service.dto.AuthValidationResponseDto;
import com.svacham.GstBill_Service.dto.GstBillSummaryDto;
import com.svacham.GstBill_Service.entity.GstBill;

import java.util.List;

public interface GstBillService {

    AuthValidationResponseDto validateToken(String token);

    GstBill createBill(GstBill gstBill);

    GstBill updateBill(String id, GstBill gstBill);

    GstBill getBillById(String id);

    List<GstBill> getAllBills();

    void deleteBill(String id);

    GstBillSummaryDto getSummary();
}