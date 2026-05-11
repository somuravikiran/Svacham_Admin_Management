package com.svacham.Stock_Service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    private String id;

    private String itemName;

    private Integer quantity;

    private String itemCategory;

    private String stockUnit;

    private Double totalStock;

    private Double usedStock;

    private Double remainingStock;

    private Double purchasePricePerUnit;

    private Double sellingPricePerUnit;

    private Double stockValue;

    private String supplierName;

    private LocalDate stockAddedDate;

    private String status;

    private String notes;

    private LocalDateTime createdAt;
}