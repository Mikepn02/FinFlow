package com.mikepn.banking.mapper;

import com.mikepn.banking.dtos.transaction.TransactionDto;
import com.mikepn.banking.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionMapper {

    public Transaction toTransaction(TransactionDto dto) {
        if (dto == null) return null;


        return Transaction.builder()
                .transactionType(dto.getTransactionType())
                .amount(dto.getAmount())
                .transactionDate(LocalDateTime.now())
                .build();
    }



    public TransactionDto toTransactionDto(Transaction transaction) {

        return TransactionDto.builder()
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .accountId(transaction.getAccount().getId())
                .build();
    }
}
