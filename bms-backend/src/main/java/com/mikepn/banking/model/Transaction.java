package com.mikepn.banking.model;


import com.mikepn.banking.enums.transaction.ETransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "_transaction")
public class Transaction extends Base{

    @Enumerated(EnumType.STRING)
    private ETransactionType transactionType;

    private double amount;
    private LocalDateTime transactionDate;

    @ManyToOne
    private Account account;
}
