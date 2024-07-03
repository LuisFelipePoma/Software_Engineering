package com.hampcode.bankingservice.controllers;

import com.hampcode.bankingservice.model.dto.TransactionReportDTO;
import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;
import com.hampcode.bankingservice.model.dto.TransactionResponseDTO;
import com.hampcode.bankingservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    //http://localhost:8080/api/v1/transactions/accounts/345678890
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccountNumber
             (@PathVariable String accountNumber) {
        List<TransactionResponseDTO> transactions = transactionService
                    .getTransactionsByAccountNumber(accountNumber);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    //http://localhost:8080/api/v1/transactions
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Validated  @RequestBody
                                                                    TransactionRequestDTO transactionDTO) {
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/v1/transactions/report?startDate=2024-01-01&endDate=2024-12-31&accountNumber=66667097778
    @GetMapping("/report")
    public ResponseEntity<List<TransactionReportDTO>> generateTransactionReport(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam("accountNumber") String accountNumber) {

        List<TransactionReportDTO> reportDTOs = transactionService.generateTransactionReport(startDateStr, endDateStr,accountNumber);
        return new ResponseEntity<>(reportDTOs, HttpStatus.OK);
    }

}
