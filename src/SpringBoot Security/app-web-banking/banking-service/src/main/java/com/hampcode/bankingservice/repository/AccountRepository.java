package com.hampcode.bankingservice.repository;

import com.hampcode.bankingservice.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a WHERE a.customer.email = ?1")
    List<Account> findAllAccountsByUser(String email);
}
