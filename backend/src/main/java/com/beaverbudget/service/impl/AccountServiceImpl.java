package com.beaverbudget.service.impl;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.exceptions.ResourceNotFoundException;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.User;
import com.beaverbudget.persistence.AccountPersistenceService;
import com.beaverbudget.persistence.UserPersistenceService;
import com.beaverbudget.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountPersistenceService accountPersistenceService;
    private final UserPersistenceService userPersistenceService;


    @Override
    public Account createAccount(Account account) {
        if(isNull(account.getOwnerId())){
            throw new InvalidResourceException("Account must have an owner");
        }
        User accountOwner = userPersistenceService.findUserById(account.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        account.setOwner(accountOwner);
        return accountPersistenceService.saveAccount(account);
    }

    @Override
    public Account findAccountById(Integer accountId) {
        return accountPersistenceService.findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + accountId + " not found"));
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountPersistenceService.findAllAccounts();
    }

    @Override
    public Account updateAccount(Integer accountId, Account account) {
        Account existingAccount = accountPersistenceService.findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account with id " + accountId + " not found"));

        if (nonNull(account.getName())) {
            existingAccount.setName(account.getName());
        }
        if (nonNull(account.getOwnerId())) {
            userPersistenceService.findUserById(account.getOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + account.getOwnerId() + " not found"));
            existingAccount.setOwnerId(account.getOwnerId());
        }
        if (nonNull(account.getBalance())) {
            existingAccount.setBalance(account.getBalance());
        }
        if (nonNull(account.getCurrency())) {
            existingAccount.setCurrency(account.getCurrency());
        }

        return accountPersistenceService.saveAccount(existingAccount);
    }

    @Override
    public void deleteAccount(Integer accountId) {
        accountPersistenceService.deleteAccount(accountId);
    }

}
