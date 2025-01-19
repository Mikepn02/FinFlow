package com.mikepn.banking.dtos.transaction;

import com.mikepn.banking.enums.transaction.ETransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private ETransactionType transactionType;
    private double amount;
    private UUID accountId;
}
