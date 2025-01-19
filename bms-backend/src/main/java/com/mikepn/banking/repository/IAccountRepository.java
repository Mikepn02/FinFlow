package com.mikepn.banking.repository;

import com.mikepn.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IAccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findAccountByCustomerId(UUID customerId);
    boolean existsByAccountNumber(String accountNumber);
}
