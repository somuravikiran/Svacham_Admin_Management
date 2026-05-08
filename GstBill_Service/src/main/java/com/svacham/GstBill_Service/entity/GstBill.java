package com.svacham.GstBill_Service.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "gst_bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GstBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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