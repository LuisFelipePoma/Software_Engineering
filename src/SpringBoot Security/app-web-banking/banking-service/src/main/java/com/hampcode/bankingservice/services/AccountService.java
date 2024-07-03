package com.hampcode.bankingservice.services;

import com.hampcode.bankingservice.exceptions.ResourceNotFoundException;
import com.hampcode.bankingservice.mapper.AccountMapper;
import com.hampcode.bankingservice.model.dto.AccountRequestDTO;
import com.hampcode.bankingservice.model.dto.AccountResponseDTO;
import com.hampcode.bankingservice.model.entities.Account;
import com.hampcode.bankingservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();
        return accountMapper.convertToListDTO(accounts);
    }

    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cuesta no encontrada con el numero:"+id));
        return accountMapper.convertToDTO(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAccountByUserId(String email){
        List<Account> accounts= accountRepository.findAllAccountsByUser(email);
        return accountMapper.convertToListDTO(accounts);
    }

    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO){
        Account account = accountMapper.convertToEntity(accountRequestDTO);
        account.setCreatedAt(LocalDate.now());
        accountRepository.save(account);
        return accountMapper.convertToDTO(account);
    }

    @Transactional
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO accountRequestDTO){
        Account account = accountRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cuesta no encontrada con el numero:"+id));

        account.setAccountNumber(accountRequestDTO.getAccountNumber());
        account.setBalance(accountRequestDTO.getBalance());
        //account.setOwnerName(accountRequestDTO.getOwnerName());
        //account.setOwnerEmail(accountRequestDTO.getOwnerEmail());
        account.setUpdatedAt(LocalDate.now());

        account = accountRepository.save(account);

        return  accountMapper.convertToDTO(account);
    }

    @Transactional
    public  void deleteAccount(Long id){
        accountRepository.deleteById(id);
    }
}
