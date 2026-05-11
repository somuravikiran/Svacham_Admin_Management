package com.svacham.GstBill_Service.repository;

import com.svacham.GstBill_Service.entity.GstBill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GstBillRepository extends MongoRepository<GstBill, String> {
}