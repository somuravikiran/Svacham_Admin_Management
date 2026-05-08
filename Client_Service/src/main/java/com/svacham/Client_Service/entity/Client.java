package com.svacham.Client_Service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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