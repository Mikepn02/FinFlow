package com.mikepn.banking.repository;

import com.mikepn.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ITransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findTransactionByAccountId(UUID accountId);
}
