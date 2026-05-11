package com.svacham.Client_Service.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection="clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    private String id;

    private String clientName;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;

    private Double totalAmount;
    private Double amountPaid;
    private Double balanceAmount;

    private LocalDate createdDate;
}