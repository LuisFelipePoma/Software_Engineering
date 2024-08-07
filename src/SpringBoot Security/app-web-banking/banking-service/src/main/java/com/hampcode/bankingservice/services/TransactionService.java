package com.hampcode.bankingservice.services;

import com.hampcode.bankingservice.exceptions.InsufficientBalanceException;
import com.hampcode.bankingservice.exceptions.InternalServerException;
import com.hampcode.bankingservice.exceptions.InvalidDateRangeException;
import com.hampcode.bankingservice.exceptions.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.TransactionMapper;
import com.hampcode.bankingservice.model.dto.TransactionReportDTO;
import com.hampcode.bankingservice.model.dto.TransactionRequestDTO;
import com.hampcode.bankingservice.model.dto.TransactionResponseDTO;
import com.hampcode.bankingservice.model.entities.Account;
import com.hampcode.bankingservice.model.entities.Transaction;
import com.hampcode.bankingservice.repository.AccountRepository;
import com.hampcode.bankingservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional(readOnly =true )
    public List<TransactionResponseDTO> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions= transactionRepository.findBySourceOrTargetAccountNumber(accountNumber);
        return  transactionMapper.convertToListDTO(transactions);
    }

    @Transactional(rollbackFor = { InsufficientBalanceException.class, InternalServerException.class })
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO) {
        // Obtener las cuentas involucradas en la transacción y verificar si existen
        Account sourceAccount = accountRepository.findByAccountNumber(transactionDTO.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de origen no existe"));

        Account targetAccount = accountRepository.findByAccountNumber(transactionDTO.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("La cuenta de destino no existe"));

        // Verificar si el saldo de la cuenta de origen es suficiente para realizar la transacción
        BigDecimal amount = transactionDTO.getAmount();
        BigDecimal sourceAccountBalance = sourceAccount.getBalance();
        if (sourceAccountBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente en la cuenta de origen:"+sourceAccount.getAccountNumber());
        }

        // Realizar la transacción
        Transaction transaction = transactionMapper.convertToEntity(transactionDTO);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction = transactionRepository.save(transaction);

        // Actualizar los saldos de las cuentas
        BigDecimal newSourceAccountBalance = sourceAccountBalance.subtract(amount);
        BigDecimal newTargetAccountBalance = targetAccount.getBalance().add(amount);

        sourceAccount.setBalance(newSourceAccountBalance);
        targetAccount.setBalance(newTargetAccountBalance);

        // Guardar los cambios en las cuentas
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        return transactionMapper.convertToDTO(transaction);
    }




    @Transactional(readOnly =true )
    public List<TransactionReportDTO> generateTransactionReport(String startDateStr,
                                                                String endDateStr,
                                                                String accountNumber) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Validación de rango de fechas
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("La fecha de inicio no puede ser posterior a la fecha de finalización.");
        }

        // Validación de número de cuenta
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("El número de cuenta no existe en el sistema."));

        List<Object[]> transactionCounts = transactionRepository.getTransactionCountByDateRangeAndAccountNumber(startDate, endDate, accountNumber);

        // Generación del informe de transacciones
        List<TransactionReportDTO> transactionReports = transactionCounts.stream()
                .map(transactionMapper::convertTransactionReportDTO)
                .toList();
        return transactionReports;
    }
}
