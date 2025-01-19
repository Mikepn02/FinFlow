package com.mikepn.banking.mapper;


import com.mikepn.banking.dtos.account.AccountDto;
import com.mikepn.banking.model.Account;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountMapper {

    public Account toAccount(AccountDto dto) {
        if (dto == null) return null;


        return Account.builder()
                .accountType(dto.getAccountType())
                .accountNumber(dto.getAccountNumber())
                .createdDate(LocalDateTime.now())
                .balance(dto.getBalance())
                .build();
    }



    public AccountDto toAccountDto(Account account) {

        return AccountDto.builder()
                .accountType(account.getAccountType())
                .accountNumber(account.getAccountNumber())
                .createdDate(account.getCreatedDate())
                .balance(account.getBalance())
                .build();
    }
}
