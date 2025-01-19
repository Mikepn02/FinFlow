package com.mikepn.banking.dtos.account;

import com.mikepn.banking.enums.account.EAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private String accountNumber;
    private EAccountType accountType;
    private double balance;
    private LocalDateTime createdDate;
    private UUID customerId;
}
