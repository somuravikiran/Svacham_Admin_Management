package com.svacham.GstBill_Service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection="gst-bill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GstBill {

    @Id
    private String id;

    private String vendorName;

    private String billNumber;

    private String itemPurchased;

    private Double billAmount;

    private Double gstPercent;

    private Double gstAmount;

    private Double totalAmount;

    private LocalDate billDate;

    private String paymentMode;

    private String status; // PAID / PENDING / PARTIAL

    private String notes;

    private LocalDateTime createdAt;
}