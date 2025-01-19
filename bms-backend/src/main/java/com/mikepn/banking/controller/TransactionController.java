package com.mikepn.banking.controller;


import com.mikepn.banking.domains.ApiResponse;
import com.mikepn.banking.dtos.transaction.CreateTransactionDto;
import com.mikepn.banking.dtos.transaction.TransactionDto;
import com.mikepn.banking.mapper.TransactionMapper;
import com.mikepn.banking.model.Transaction;
import com.mikepn.banking.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/transaction")
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction")
public class TransactionController {

    private final TransactionService service;
    private final TransactionMapper mapper;


    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionDto>> withdaw(TransactionDto dto) {
        var response = service.withdraw(dto);
        return new ApiResponse<>(
                mapper.toTransactionDto(response),
                "Withdraw successful",
                HttpStatus.OK
        ).toResponseEntity();
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionDto>> deposit(TransactionDto dto) {
        var response = service.deposit(dto);
        return new ApiResponse<>(
                mapper.toTransactionDto(response),
                "Deposit successful",
                HttpStatus.OK
        ).toResponseEntity();
    }


    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionDto>> transfer(CreateTransactionDto dto) {
        var response = service.transfer(dto);
        return new ApiResponse<>(
                mapper.toTransactionDto(response),
                "Transfer successful",
                HttpStatus.OK
        ).toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDto>> getTransactionById(@PathVariable UUID id) {
        var response = service.getTransactionById(id);
        return new ApiResponse<>(
                mapper.toTransactionDto(response),
                "successful",
                HttpStatus.OK
        ).toResponseEntity();
    }

    @GetMapping("/{account-id}")
    public ResponseEntity<ApiResponse<TransactionDto>> getTransactionByAccountId(@PathVariable(name = "account-id") UUID accountId) {
        var response = service.getTransactionById(accountId);
        return new ApiResponse<>(
                mapper.toTransactionDto(response),
                "successful",
                HttpStatus.OK
        ).toResponseEntity();
    }

    /*

    @GetMapping()
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactions() {
        var response = service.getAllTransactions();
        return new ApiResponse<>(
                mapper.toTransactionDto(response),
                "successful",
                HttpStatus.OK
        ).toResponseEntity();
    }

     */


}
