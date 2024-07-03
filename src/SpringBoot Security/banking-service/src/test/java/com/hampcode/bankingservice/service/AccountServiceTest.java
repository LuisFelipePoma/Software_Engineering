package com.hampcode.bankingservice.service;

import com.hampcode.bankingservice.exceptions.BadRequestException;
import com.hampcode.bankingservice.exceptions.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.AccountMapper;
import com.hampcode.bankingservice.model.dto.AccountResponseDTO;
import com.hampcode.bankingservice.model.entities.Account;
import com.hampcode.bankingservice.repository.AccountRepository;
import com.hampcode.bankingservice.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @InjectMocks
    private AccountService accountService;

    @Test
    public void testGetAllAcounts(){
        //Arrange
        Account account_1 = new Account();
        account_1.setId(1L);
        Account account_2 = new Account();
        account_2.setId(2L);
        Account account_3 = new Account();
        account_3.setId(3L);
        List<Account> accounts = Arrays.asList(account_1, account_2, account_3);
        //Simular el comportamiento del accountRepository ==> List<Account> accounts = accountRepository.findAll();
        when(accountRepository.findAll()).thenReturn(accounts);

        AccountResponseDTO responseDTO_01 = new AccountResponseDTO();
        responseDTO_01.setId(1L);
        AccountResponseDTO responseDTO_02 = new AccountResponseDTO();
        responseDTO_02.setId(2L);
        AccountResponseDTO responseDTO_03 = new AccountResponseDTO();
        responseDTO_03.setId(3L);

        List<AccountResponseDTO> expectedResponseDTOs = Arrays.asList(responseDTO_01,responseDTO_02, responseDTO_03);
        //Simular el comportamiento del  accountMapper.convertToListDTO(accounts)
        when(accountMapper.convertToListDTO(accounts)).thenReturn(expectedResponseDTOs);


        //Act: Aca se realiza la prueba del metodo getAllAccounts()
        List<AccountResponseDTO> accountsResult = accountService.getAllAccounts();

        //Assert
        assertEquals(expectedResponseDTOs.size(), accountsResult.size());
    }

    @Test
    public void testGetAccountById_ExistingId(){
        //Arrange
        Long id = 1L;
        Account account = new Account();
        account.setId(id);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        AccountResponseDTO responseDTO = new AccountResponseDTO();
        responseDTO.setId(account.getId());
        when(accountMapper.convertToDTO(account)).thenReturn(responseDTO);

        //Act
        AccountResponseDTO result = accountService.getAccountById(id);

        //Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testGetAccountById_NonExistingId(){
        //Arrange
        Long id = 99999L;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class,
                ()->accountService.getAccountById(id));
    }


}
