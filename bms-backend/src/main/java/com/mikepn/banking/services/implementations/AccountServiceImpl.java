package com.mikepn.banking.services.implementations;

import com.mikepn.banking.dtos.account.AccountDto;
import com.mikepn.banking.model.Account;
import com.mikepn.banking.repository.IAccountRepository;
import com.mikepn.banking.services.AccountService;
import com.mikepn.banking.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final IAccountRepository repository;
    private final AuthenticationService authenticationService;

    @Override
    public Account createAccount(AccountDto dto) {
        String accountNumber = generateUniqueAccountNumber();
        var user = authenticationService.getLoggedInUser();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(dto.getAccountType())
                .balance(0.0)
                .createdDate(LocalDateTime.now())
                .customer(user)
                .build();
        return repository.save(account);
    }

    @Override
    public Account updateAccount(UUID accountId, AccountDto dto) {

        Account account = repository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if(dto.getAccountType() != null){
            account.setAccountType(dto.getAccountType());
        }

        return repository.save(account);
    }

    @Override
    public Account getAccountById(UUID accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public Optional<Account> getAccountsByCustomerId(UUID customerId) {
        return repository.findAccountByCustomerId(customerId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }



    @Override
    public void deleteAccount(UUID accountId) {

    }


    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%07d", (int) (Math.random() * 1_000_0000));
        } while (repository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

}
