package com.hampcode.bankingservice.mapper;

import com.hampcode.bankingservice.model.dto.AccountRequestDTO;
import com.hampcode.bankingservice.model.dto.AccountResponseDTO;
import com.hampcode.bankingservice.model.entities.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import java.util.List;

@Component
@AllArgsConstructor
public class AccountMapper {

    private final ModelMapper modelMapper;

    public Account convertToEntity(AccountRequestDTO accountRequestDTO){
        return modelMapper.map(accountRequestDTO, Account.class);
    }

    public AccountResponseDTO convertToDTO(Account account){
        return modelMapper.map(account, AccountResponseDTO.class);
    }

    public List<AccountResponseDTO>  convertToListDTO(List<Account> accounts){
        return accounts.stream()
                .map(this::convertToDTO)
                .toList();
    }

}
