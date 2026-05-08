package com.svacham.Stock_Service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;            // Mango Pickle / Garlic / Oil / Jar

    private Integer quantity;

    private String itemCategory;      // FINISHED_PICKLE / RAW_MATERIAL / PACKING

    private String stockUnit;         // KG / LITRE / PIECE / JAR

    private Double totalStock;

    private Double usedStock;

    private Double remainingStock;

    private Double purchasePricePerUnit;

    private Double sellingPricePerUnit;

    private Double stockValue;

    private String supplierName;

    private LocalDate stockAddedDate;

    private String status;            // AVAILABLE / LOW_STOCK / OUT_OF_STOCK

    private String notes;

    private LocalDateTime createdAt;
}