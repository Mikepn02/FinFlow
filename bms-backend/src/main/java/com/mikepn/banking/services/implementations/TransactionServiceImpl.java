package com.mikepn.banking.services.implementations;

import com.mikepn.banking.dtos.transaction.CreateTransactionDto;
import com.mikepn.banking.dtos.transaction.TransactionDto;
import com.mikepn.banking.enums.transaction.ETransactionType;
import com.mikepn.banking.model.Account;
import com.mikepn.banking.model.Transaction;
import com.mikepn.banking.repository.IAccountRepository;
import com.mikepn.banking.repository.ITransactionRepository;
import com.mikepn.banking.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;


    @Override
    public Transaction withdraw(TransactionDto transactionDto) {
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        var accountBalance = account.getBalance();

        if(accountBalance < transactionDto.getAmount()) {
            throw new IllegalArgumentException("Not enough money to withdraw");
        }
        var newAmount = account.getBalance() - transactionDto.getAmount();

        account.setBalance(newAmount);
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionType(ETransactionType.WITHDRAW)
                .amount(newAmount)
                .account(account)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction deposit(TransactionDto transactionDto) {
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        var newAmount = account.getBalance() + transactionDto.getAmount();
        account.setBalance(newAmount);

        Transaction transaction = Transaction.builder()
                .transactionType(ETransactionType.DEPOSIT)
                .amount(newAmount)
                .account(account)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction transfer(CreateTransactionDto createTransactionDto) {
        Account from = accountRepository.findByAccountNumber(createTransactionDto.getFrom())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account to = accountRepository.findByAccountNumber(createTransactionDto.getTo())
                .orElseThrow(() -> new RuntimeException("Account not found"));


        if(from.getBalance() < createTransactionDto.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds in source account");
        }

        from.setBalance(from.getBalance() - createTransactionDto.getAmount());
        to.setBalance(to.getBalance() + createTransactionDto.getAmount());

        accountRepository.save(from);
        accountRepository.save(to);


        Transaction transaction = Transaction.builder()
                .transactionType(ETransactionType.TRANSFER)
                .account(from)
                .build();


        return transactionRepository.save(transaction);
    }



    @Override
    public Transaction getTransactionById(UUID transacionId) {
        Transaction transaction = transactionRepository.findById(transacionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return transaction;
    }

    @Override
    public Transaction getTransactionByAccountId(UUID accountId) {
        Transaction transaction = transactionRepository.findTransactionByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return transaction;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    @Override
    public Transaction reverseTransaction(UUID transacionId) {
        return null;
    }

    @Override
    public double calculateTotalTransactions(UUID accountId) {
        return 0;
    }
}
