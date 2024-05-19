package com.hampcode.bankingservice.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_number", nullable = false)
    private String accountNumber;
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
    @Column(name = "owner_name", nullable = false)
    private String ownerName;
    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "update_at")
    private LocalDate updatedAt;
}
