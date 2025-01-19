package com.mikepn.banking.controller;


import com.mikepn.banking.domains.ApiResponse;
import com.mikepn.banking.dtos.account.AccountDto;
import com.mikepn.banking.mapper.AccountMapper;
import com.mikepn.banking.model.Account;
import com.mikepn.banking.services.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping("account")
@RequiredArgsConstructor
@Tag(name = "Account")
@RestController
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper mapper;


    @PostMapping
    public ResponseEntity<ApiResponse<AccountDto>> createAccount(@RequestBody AccountDto dto) {
         var account = accountService.createAccount(dto);
         return new ApiResponse<>(
                 mapper.toAccountDto(account),
                 "Account created successfully",
                 HttpStatus.CREATED
         ).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountDto>> getAccount(@PathVariable UUID id) {
        var account = accountService.getAccountById(id);
        return new ApiResponse<>(
                mapper.toAccountDto(account),
                "Account Successfully Retrieved",
                HttpStatus.OK
        ).toResponseEntity();
    }


    @GetMapping("/{customer-id}")
    public ResponseEntity<ApiResponse<AccountDto>> getAccountByCustomerId(@PathVariable UUID customerId) {
        var account = accountService.getAccountById(customerId);
        return new ApiResponse<>(
                mapper.toAccountDto(account),
                "Account belong to the customer successfully retrieved",
                HttpStatus.OK
        ).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountDto>> updateAccount(@PathVariable UUID id, @RequestBody AccountDto dto) {
        var account = accountService.updateAccount(id, dto);
        return new ApiResponse<>(
                mapper.toAccountDto(account),
                "Account updated successfully",
                HttpStatus.OK
        ).toResponseEntity();
    }
}
