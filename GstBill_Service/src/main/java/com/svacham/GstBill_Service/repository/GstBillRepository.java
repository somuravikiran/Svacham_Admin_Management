package com.svacham.GstBill_Service.repository;

import com.svacham.GstBill_Service.entity.GstBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GstBillRepository extends JpaRepository<GstBill, Long> {
}