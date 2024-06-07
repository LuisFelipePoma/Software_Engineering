package com.hampcode.bankingservice.services;

import com.hampcode.bankingservice.exceptions.BadRequestException;
import com.hampcode.bankingservice.exceptions.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.TransactionMapper;
import com.hampcode.bankingservice.model.dto.TransactionReportDTO;
import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;
import com.hampcode.bankingservice.model.dto.TransactionResponseDTO;
import com.hampcode.bankingservice.model.entities.Account;
import com.hampcode.bankingservice.model.entities.Transaction;
import com.hampcode.bankingservice.repository.AccountRepository;
import com.hampcode.bankingservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void testGetTransactionsByAccountNumber() {
        // Arrange
        String accountNumber = "123456";
        when (transactionRepository.findBySourceOrTargetAccountNumber(accountNumber)).thenReturn(Collections.emptyList());

        // Act
        List<TransactionResponseDTO> result = transactionService.getTransactionsByAccountNumber(accountNumber);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    public void testCreateTransaction_SuccessfulTransaction() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setSourceAccountNumber("source123");
        requestDTO.setTargetAccountNumber("target456");
        requestDTO.setAmount(BigDecimal.valueOf(100.0));
        requestDTO.setDescription("Test transaction");

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(BigDecimal.valueOf(500.0));

        Account targetAccount = new Account();
        targetAccount.setId(2L);
        targetAccount.setBalance(BigDecimal.valueOf(200.0));

        Transaction transaction = new Transaction();
        transaction.setId(1L);

        when(accountRepository.findByAccountNumber("source123")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("target456")).thenReturn(Optional.of(targetAccount));
        when(transactionMapper.convertToEntity(requestDTO)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.convertToDTO(transaction)).thenReturn(new TransactionResponseDTO());

        // Act
        TransactionResponseDTO result = transactionService.createTransaction(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(400.0), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(300.0), targetAccount.getBalance());
    }


    @Test
    public void testCreateTransaction_SourceAccountNotFound() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setSourceAccountNumber("source123");
        requestDTO.setTargetAccountNumber("target456");
        requestDTO.setAmount(BigDecimal.valueOf(100.0));
        requestDTO.setDescription("Test transaction");

        when(accountRepository.findByAccountNumber("source123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(requestDTO));
    }
    @Test
    public void testCreateTransaction_TargetAccountNotFound() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setSourceAccountNumber("source123");
        requestDTO.setTargetAccountNumber("target456");
        requestDTO.setAmount(BigDecimal.valueOf(100.0));
        requestDTO.setDescription("Test transaction");

        // Simular que la cuenta de origen existe pero la cuenta de destino no
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(BigDecimal.valueOf(500.0));

        when(accountRepository.findByAccountNumber("source123")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("target456")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(requestDTO));
    }



    @Test
    public void testCreateTransaction_InsufficientBalance() {
        // Arrange
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setSourceAccountNumber("source123");
        requestDTO.setTargetAccountNumber("target456");
        requestDTO.setAmount(BigDecimal.valueOf(600.0)); // Greater than source account balance
        requestDTO.setDescription("Test transaction");

        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(BigDecimal.valueOf(500.0));

        Account targetAccount = new Account();
        targetAccount.setId(2L);
        targetAccount.setBalance(BigDecimal.valueOf(200.0));

        when(accountRepository.findByAccountNumber("source123")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("target456")).thenReturn(Optional.of(targetAccount));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> transactionService.createTransaction(requestDTO));
    }

    @Test
    public void testGenerateTransactionReport() {
        // Arrange
        String startDateStr = "2024-01-01";
        String endDateStr = "2024-01-31";
        String accountNumber = "123456";

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Agregar datos simulados a transactionCounts
        List<Object[]> transactionCounts = new ArrayList<>(List.of(
                new Object[]{"2024-01-01", 5L},
                new Object[]{"2024-01-15", 3L}
        ));

        // Crear informes esperados simulados
        List<TransactionReportDTO> expectedReport = transactionCounts.stream()
                .map(data -> {
                    TransactionReportDTO report = new TransactionReportDTO();
                    report.setDate(LocalDate.parse((String) data[0]));
                    report.setTransactionCount((Long) data[1]); // Casting a Long
                    return report;
                })
                .collect(Collectors.toList());

        // Configurar el comportamiento simulado del repositorio
        when(transactionRepository.getTransactionCountByDateRangeAndAccountNumber(startDate, endDate, accountNumber))
                .thenReturn(transactionCounts);

        // Act
        List<TransactionReportDTO> result = transactionService.generateTransactionReport(startDateStr, endDateStr, accountNumber);

        // Assert
        assertEquals(expectedReport.size(), result.size());
    }


}
