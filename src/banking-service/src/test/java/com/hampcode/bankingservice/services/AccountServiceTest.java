package com.hampcode.bankingservice.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.hampcode.bankingservice.exceptions.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.AccountMapper;
import com.hampcode.bankingservice.model.dto.AccountRequestDTO;
import com.hampcode.bankingservice.model.dto.AccountResponseDTO;
import com.hampcode.bankingservice.model.entities.Account;
import com.hampcode.bankingservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testCreateAccount() {

        // Arrange
        AccountRequestDTO requestDTO = new AccountRequestDTO();
        requestDTO.setAccountNumber("12345");
        requestDTO.setBalance(BigDecimal.valueOf(100.0));
        requestDTO.setOwnerName("John Doe");
        requestDTO.setOwnerEmail("john@example.com");

        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber(requestDTO.getAccountNumber());
        account.setBalance(requestDTO.getBalance());
        account.setOwnerName(requestDTO.getOwnerName());
        account.setOwnerEmail(requestDTO.getOwnerEmail());
        account.setCreatedAt(LocalDate.now()); // Establecemos la fecha de creación aquí

        // Configuramos el mock de accountMapper
        when(accountMapper.convertToEntity(requestDTO)).thenReturn(account); // Asegúrate de que account no sea nullº
        when(accountMapper.convertToDTO(account)).thenReturn(new AccountResponseDTO(account.getId(), account.getAccountNumber(), account.getBalance(), account.getOwnerName(), account.getOwnerEmail()));

        // Act
        AccountResponseDTO result = accountService.createAccount(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getAccountNumber(), result.getAccountNumber());
        assertEquals(account.getBalance(), result.getBalance());
        assertEquals(account.getOwnerName(), result.getOwnerName());
        assertEquals(account.getOwnerEmail(), result.getOwnerEmail());

        // Verificamos que el método convertToEntity del mock accountMapper se haya llamado una vez con el requestDTO
        verify(accountMapper, times(1)).convertToEntity(requestDTO);
        // Verificamos que el método save del mock accountRepository se haya llamado una vez con el account
        verify(accountRepository, times(1)).save(account);
        // Verificamos que el método convertToDTO del mock accountMapper se haya llamado una vez con el account
            verify(accountMapper, times(1)).convertToDTO(account);
    }


    @Test
    public void testGetAllAccounts() {
        // Arrange
        Account account1 = new Account();
        account1.setId(1L);
        Account account2 = new Account();
        account2.setId(2L);
        List<Account> accountList = Arrays.asList(account1, account2);

        // Mocking repository
        when(accountRepository.findAll()).thenReturn(accountList);

        // Mocking mapper
        AccountResponseDTO responseDTO1 = new AccountResponseDTO();
        responseDTO1.setId(account1.getId());
        AccountResponseDTO responseDTO2 = new AccountResponseDTO();
        responseDTO2.setId(account2.getId());

        List<AccountResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);
        when(accountMapper.convertToListDTO(accountList)).thenReturn(expectedResponse);

        // Act
        List<AccountResponseDTO> result = accountService.getAllAccounts();

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());

        // Verify that repository and mapper methods were called
        verify(accountRepository, times(1)).findAll();
        verify(accountMapper, times(1)).convertToListDTO(accountList);
    }


    @Test
    public void testGetAccountById_ExistingId() {
        // Arrange
        Long id = 1L;
        Account account = new Account();
        account.setId(id);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        // Mocking mapper
        AccountResponseDTO responseDTO = new AccountResponseDTO();
        responseDTO.setId(account.getId());
        when(accountMapper.convertToDTO(account)).thenReturn(responseDTO);

        // Act
        AccountResponseDTO result = accountService.getAccountById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testGetAccountById_NonExistingId() {
        // Arrange
        Long id = 999L;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(id));
    }


    @Test
    public void testUpdateAccount() {
        // Arrange
        Long id = 1L;
        AccountRequestDTO requestDTO = new AccountRequestDTO();
        requestDTO.setAccountNumber("12345");
        requestDTO.setBalance(BigDecimal.valueOf(100.0));
        requestDTO.setOwnerName("John Doe");
        requestDTO.setOwnerEmail("john@example.com");

        Account existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setAccountNumber("Old Account Number");
        existingAccount.setBalance(BigDecimal.valueOf(50.0));
        existingAccount.setOwnerName("Jane Smith");
        existingAccount.setOwnerEmail("jane@example.com");
        existingAccount.setCreatedAt(LocalDate.now());

        Account updatedAccount = new Account();
        updatedAccount.setId(id);
        updatedAccount.setAccountNumber(requestDTO.getAccountNumber());
        updatedAccount.setBalance(requestDTO.getBalance());
        updatedAccount.setOwnerName(requestDTO.getOwnerName());
        updatedAccount.setOwnerEmail(requestDTO.getOwnerEmail());
        updatedAccount.setCreatedAt(existingAccount.getCreatedAt());
        updatedAccount.setUpdatedAt(LocalDate.now());

        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(updatedAccount);

        // Mocking mapper
        AccountResponseDTO responseDTO = new AccountResponseDTO();
        responseDTO.setId(updatedAccount.getId());
        responseDTO.setAccountNumber(updatedAccount.getAccountNumber());
        responseDTO.setBalance(updatedAccount.getBalance());
        responseDTO.setOwnerName(updatedAccount.getOwnerName());
        responseDTO.setOwnerEmail(updatedAccount.getOwnerEmail());
        when(accountMapper.convertToDTO(updatedAccount)).thenReturn(responseDTO);

        // Act
        AccountResponseDTO result = accountService.updateAccount(id, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(requestDTO.getAccountNumber(), result.getAccountNumber());
        assertEquals(requestDTO.getBalance(), result.getBalance());
        assertEquals(requestDTO.getOwnerName(), result.getOwnerName());
        assertEquals(requestDTO.getOwnerEmail(), result.getOwnerEmail());
    }


    @Test
    public void testDeleteAccount() {
        // Arrange
        Long id = 1L;

        // Act
        assertDoesNotThrow(() -> accountService.deleteAccount(id));

        // Assert
        verify(accountRepository, times(1)).deleteById(id);
    }

}
