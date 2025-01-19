package com.mikepn.banking.dtos.transaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDto {

    private String from;
    private String to;
    private double amount;
}
