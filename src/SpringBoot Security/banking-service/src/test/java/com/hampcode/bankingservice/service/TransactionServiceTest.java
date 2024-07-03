package com.hampcode.bankingservice.service;

import com.hampcode.bankingservice.exceptions.InsufficientBalanceException;
import com.hampcode.bankingservice.exceptions.InvalidDateRangeException;
import com.hampcode.bankingservice.exceptions.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.TransactionMapper;
import com.hampcode.bankingservice.model.dto.AccountResponseDTO;
import com.hampcode.bankingservice.model.dto.TransactionReportDTO;
import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;
import com.hampcode.bankingservice.model.dto.TransactionResponseDTO;
import com.hampcode.bankingservice.model.entities.Account;
import com.hampcode.bankingservice.model.entities.Transaction;
import com.hampcode.bankingservice.repository.AccountRepository;
import com.hampcode.bankingservice.repository.TransactionRepository;
import com.hampcode.bankingservice.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void testGetTransactionsByValidAccountNumber() {
        // GIVEN
        String validAccountNumber = "123456";
        // Simulamos diferentes cantidades de transacciones
        List<Transaction> expectedTransactions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            expectedTransactions.add(new Transaction());
        }
        when(transactionRepository.findBySourceOrTargetAccountNumber(validAccountNumber)).thenReturn(expectedTransactions);

        // Convertimos las transacciones simuladas en DTO simulados
        List<TransactionResponseDTO> expectedResponseDTOs = new ArrayList<>();
        for (Transaction transaction : expectedTransactions) {
            expectedResponseDTOs.add(new TransactionResponseDTO());
        }
        when(transactionMapper.convertToListDTO(expectedTransactions)).thenReturn(expectedResponseDTOs);

        // WHEN
        // Actuamos al llamar al método que obtiene transacciones por número de cuenta
        List<TransactionResponseDTO> actualResponseDTOs = transactionService.getTransactionsByAccountNumber(validAccountNumber);

        // THEN
        // Verificamos que la lista de transacciones devuelta no esté vacía
        assertFalse(actualResponseDTOs.isEmpty(), "Se esperaba una lista de transacciones no vacía");
        // Verificamos que el tamaño de la lista de transacciones devuelta sea el mismo que el tamaño de la lista esperada
        assertEquals(expectedResponseDTOs.size(), actualResponseDTOs.size(), "Se esperaba recibir un listado no vacío de transacciones asociadas al número de cuenta especificado");
    }

    @Test
    void testGetTransactionsByValidAccountNumber_NoTransactions() {
        // GIVEN
        String validAccountNumber = "123456";
        // Configuramos el repositorio de transacciones para que devuelva una lista vacía
        when(transactionRepository.findBySourceOrTargetAccountNumber(validAccountNumber)).thenReturn(new ArrayList<>());

        // WHEN
        // Actuamos al llamar al método que obtiene transacciones por número de cuenta
        List<TransactionResponseDTO> actualResponseDTOs = transactionService.getTransactionsByAccountNumber(validAccountNumber);

        // THEN
        // Verificamos que la lista de transacciones devuelta esté vacía
        assertTrue(actualResponseDTOs.isEmpty(), "Se esperaba una lista de transacciones vacía");
    }


    @Test
    void testCreateTransaction_SuccessfulTransaction() {
        // GIVEN
        // Se establecen los datos válidos para la transacción
        TransactionRequestDTO validTransactionRequest = new TransactionRequestDTO();
        validTransactionRequest.setSourceAccountNumber("sourceAccountNumber");
        validTransactionRequest.setTargetAccountNumber("targetAccountNumber");
        validTransactionRequest.setAmount(new BigDecimal("100.00"));

        // Se establece un saldo inicial en las cuentas de origen y destino
        BigDecimal initialSourceAccountBalance = new BigDecimal("500.00");
        Account sourceAccount = new Account();
        sourceAccount.setBalance(initialSourceAccountBalance);

        BigDecimal initialTargetAccountBalance = new BigDecimal("200.00");
        Account targetAccount = new Account();
        targetAccount.setBalance(initialTargetAccountBalance);

        // Se crea la entidad de transacción
        Transaction transactionEntity = new Transaction();
        transactionEntity.setTransactionDate(LocalDate.now());
        transactionEntity.setSourceAccount(sourceAccount);
        transactionEntity.setTargetAccount(targetAccount);

        // Se establece la respuesta esperada
        TransactionResponseDTO expectedResponseDTO = new TransactionResponseDTO();
        expectedResponseDTO.setId(1L);
        expectedResponseDTO.setTargetAccount(new AccountResponseDTO());
        expectedResponseDTO.setAmount(new BigDecimal("100.00"));
        expectedResponseDTO.setDescription("Transaction description");

        // Se simula el comportamiento de los repositorios y el mapeador
        when(accountRepository.findByAccountNumber(validTransactionRequest.getSourceAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(validTransactionRequest.getTargetAccountNumber())).thenReturn(Optional.of(targetAccount));
        when(transactionMapper.convertToEntity(validTransactionRequest)).thenReturn(transactionEntity);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionEntity);
        when(transactionMapper.convertToDTO(transactionEntity)).thenReturn(expectedResponseDTO);

        // WHEN
        // Se realiza la transacción
        TransactionResponseDTO transactionResponseDTO = transactionService.createTransaction(validTransactionRequest);

        // THEN
        // Se verifica que se haya creado la transacción con éxito
        assertNotNull(transactionResponseDTO);
        assertEquals(expectedResponseDTO, transactionResponseDTO, "La transacción se debe crear correctamente");

        // Se verifica que el saldo de la cuenta de origen se haya actualizado correctamente
        BigDecimal expectedSourceAccountBalance = initialSourceAccountBalance.subtract(validTransactionRequest.getAmount());
        assertEquals(expectedSourceAccountBalance, sourceAccount.getBalance(), "El saldo de la cuenta de origen se debe actualizar correctamente");

        // Se verifica que el saldo de la cuenta de destino se haya actualizado correctamente
        BigDecimal expectedTargetAccountBalance = initialTargetAccountBalance.add(validTransactionRequest.getAmount());
        assertEquals(expectedTargetAccountBalance, targetAccount.getBalance(), "El saldo de la cuenta de destino se debe actualizar correctamente");
    }

    @Test
    void testCreateTransaction_SourceAccountNotExist() {
        // GIVEN
        // Se establecen los datos válidos para la transacción, pero la cuenta de origen no existe
        TransactionRequestDTO invalidTransactionRequest = new TransactionRequestDTO();
        invalidTransactionRequest.setSourceAccountNumber("sourceAccountNumber");
        invalidTransactionRequest.setTargetAccountNumber("targetAccountNumber");
        invalidTransactionRequest.setAmount(new BigDecimal("100.00"));

        // Se establece un saldo inicial en la cuenta de destino
        BigDecimal initialTargetAccountBalance = new BigDecimal("200.00");
        Account targetAccount = new Account();
        targetAccount.setBalance(initialTargetAccountBalance);

        // Se simula el comportamiento de los repositorios y el mapeador
        when(accountRepository.findByAccountNumber(invalidTransactionRequest.getSourceAccountNumber())).thenReturn(Optional.empty());

        // WHEN
        // Se realiza la transacción y se espera que lance una excepción BadRequestException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(invalidTransactionRequest);
        });

        // THEN
        // Se verifica que la excepción contenga el mensaje esperado
        assertEquals("La cuenta de origen no existe", exception.getMessage(), "Debe lanzarse una excepción cuando la cuenta de origen no existe");
    }

    @Test
    void testCreateTransaction_TargetAccountNotExist() {
        // GIVEN
        // Se establecen los datos válidos para la transacción, pero la cuenta de destino no existe
        TransactionRequestDTO invalidTransactionRequest = new TransactionRequestDTO();
        invalidTransactionRequest.setSourceAccountNumber("sourceAccountNumber");
        invalidTransactionRequest.setTargetAccountNumber("targetAccountNumber");
        invalidTransactionRequest.setAmount(new BigDecimal("100.00"));

        // Se establece un saldo inicial en la cuenta de origen
        BigDecimal initialSourceAccountBalance = new BigDecimal("500.00");
        Account sourceAccount = new Account();
        sourceAccount.setBalance(initialSourceAccountBalance);

        // Se simula el comportamiento de los repositorios y el mapeador
        when(accountRepository.findByAccountNumber(invalidTransactionRequest.getSourceAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(invalidTransactionRequest.getTargetAccountNumber())).thenReturn(Optional.empty());

        // WHEN
        // Se realiza la transacción y se espera que lance una excepción BadRequestException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(invalidTransactionRequest);
        });

        // THEN
        // Se verifica que la excepción contenga el mensaje esperado
        assertEquals("La cuenta de destino no existe", exception.getMessage(), "Debe lanzarse una excepción cuando la cuenta de destino no existe");
    }


    @Test
    void testCreateTransaction_InsufficientBalance() {
        // GIVEN
        // Se establecen los datos válidos para la transacción, pero el saldo en la cuenta de origen no es suficiente
        TransactionRequestDTO invalidTransactionRequest = new TransactionRequestDTO();
        invalidTransactionRequest.setSourceAccountNumber("123456789"); // Número de cuenta de origen
        invalidTransactionRequest.setTargetAccountNumber("987654321"); // Número de cuenta de destino
        invalidTransactionRequest.setAmount(new BigDecimal("600.00"));

        // Se establece un saldo inicial en la cuenta de origen menor que el monto de la transacción
        BigDecimal initialSourceAccountBalance = new BigDecimal("500.00");
        Account sourceAccount = new Account();
        sourceAccount.setBalance(initialSourceAccountBalance);

        // Se simula el comportamiento de los repositorios y el mapeador
        when(accountRepository.findByAccountNumber(invalidTransactionRequest.getSourceAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(invalidTransactionRequest.getTargetAccountNumber())).thenReturn(Optional.of(new Account())); // Simula una cuenta de destino válida

        // WHEN
        // Se realiza la transacción y se espera que lance una excepción InsufficientBalanceException
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.createTransaction(invalidTransactionRequest);
        });

        // THEN
        // Se verifica que la excepción contenga el mensaje esperado
        assertEquals("Saldo insuficiente en la cuenta de origen:" + sourceAccount.getAccountNumber(), exception.getMessage(), "Debe lanzarse una excepción cuando el saldo en la cuenta de origen es insuficiente");
        // Se verifica que el saldo de la cuenta de origen no haya sido modificado
        assertEquals(initialSourceAccountBalance, sourceAccount.getBalance(), "El saldo de la cuenta de origen no debe ser modificado");
    }

    @Test
    void testGenerateTransactionReport_Successful() {
        // Arrange
        String startDateStr = "2024-01-01";
        String endDateStr = "2024-01-31";
        String accountNumber = "123456789";
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Simulacion de Account Repository
        Account account = new Account();
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        //  Simulacion de  Transaction Repository
        List<Object[]> transactionCounts = new ArrayList<>();
        transactionCounts.add(new Object[]{"2024-01-01", 5}); 
        when(transactionRepository.getTransactionCountByDateRangeAndAccountNumber(any(LocalDate.class), any(LocalDate.class), eq(accountNumber))).thenReturn(transactionCounts);

        // Simulacion de  Transaction Mapper
        TransactionReportDTO transactionReportDTO = new TransactionReportDTO();
        when(transactionMapper.convertTransactionReportDTO(any(Object[].class))).thenReturn(transactionReportDTO);

        // Act
        List<TransactionReportDTO> report = transactionService.generateTransactionReport(startDateStr, endDateStr, accountNumber);

        // Assert
        assertNotNull(report, "El informe de transacciones no debe ser nulo");
        assertFalse(report.isEmpty(), "El informe de transacciones no debe estar vacío");
        assertEquals(1, report.size(), "Debe haber una entrada en el informe de transacciones");
        assertEquals(transactionReportDTO, report.get(0), "El informe de transacciones debe coincidir");
    }

    @Test
    void testGenerateTransactionReport_NoTransactions() {
        // Arrange
        String startDateStr = "2024-01-01";
        String endDateStr = "2024-01-31";
        String accountNumber = "123456789";
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Mock Account Repository
        Account account = new Account();
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        // Mock Transaction Repository
        List<Object[]> emptyTransactionCounts = new ArrayList<>();
        when(transactionRepository.getTransactionCountByDateRangeAndAccountNumber(any(LocalDate.class), any(LocalDate.class), eq(accountNumber))).thenReturn(emptyTransactionCounts);

        // Act
        List<TransactionReportDTO> report = transactionService.generateTransactionReport(startDateStr, endDateStr, accountNumber);

        // Assert
        assertNotNull(report, "El informe de transacciones no debe ser nulo");
        assertTrue(report.isEmpty(), "El informe de transacciones debe estar vacío");
    }


    @Test
    void testGenerateTransactionReport_InvalidDateRange() {
        // Arrange
        String startDateStr = "2024-01-31";
        String endDateStr = "2024-01-01"; // Fecha de inicio posterior a la fecha de finalización
        String accountNumber = "123456789";
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Act & Assert
        assertThrows(InvalidDateRangeException.class, () -> {
            transactionService.generateTransactionReport(startDateStr, endDateStr, accountNumber);
        }, "Debería lanzar una excepción InvalidDateRangeException");
    }

    @Test
    void testGenerateTransactionReport_InvalidAccountNumber() {
        // Arrange
        String startDateStr = "2024-01-01";
        String endDateStr = "2024-01-31";
        String accountNumber = "nonexistentAccount";
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Simulando que no se encuentra la cuenta en el repositorio
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.generateTransactionReport(startDateStr, endDateStr, accountNumber);
        }, "Debería lanzar una excepción ResourceNotFoundException");
    }

}
