package com.mikepn.banking.services;

import com.mikepn.banking.dtos.transaction.CreateTransactionDto;
import com.mikepn.banking.dtos.transaction.TransactionDto;
import com.mikepn.banking.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction withdraw(TransactionDto transactionDto);
    Transaction deposit(TransactionDto transactionDto);
    Transaction transfer(CreateTransactionDto createTransactionDto);

    Transaction getTransactionById(UUID transactionId);
    Transaction getTransactionByAccountId(UUID accountId);
    List<Transaction> getAllTransactions();
    Transaction reverseTransaction(UUID transactionId);
    double calculateTotalTransactions(UUID accountId);
}
