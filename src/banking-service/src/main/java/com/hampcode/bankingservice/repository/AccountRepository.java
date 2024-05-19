package com.hampcode.bankingservice.repository;

import com.hampcode.bankingservice.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
