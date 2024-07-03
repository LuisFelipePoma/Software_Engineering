package com.hampcode.bankingservice.model.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionReportDTO {
    private LocalDate date;
    private long transactionCount;
}
