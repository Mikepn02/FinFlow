package com.mikepn.banking.services;

import com.mikepn.banking.dtos.account.AccountDto;
import com.mikepn.banking.model.Account;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    Account createAccount(AccountDto dto);
    Account updateAccount(UUID accountId, AccountDto dto);

    Account getAccountById(UUID accountId);
    Optional<Account> getAccountsByCustomerId(UUID customerId);
    List<Account> getAllAccounts();

    void deleteAccount(UUID accountId);

}
